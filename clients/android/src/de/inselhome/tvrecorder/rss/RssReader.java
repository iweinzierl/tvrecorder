/**
 * Copyright (C) 2011 Ingo Weinzierl (ingo_weinzierl@web.de)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 */
package de.inselhome.tvrecorder.rss;

import java.io.InputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import java.net.URL;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import de.inselhome.tvrecorder.common.utils.XMLUtils;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class RssReader {

    public static final String DEFAULT_DATE_FORMAT =
        "EEE, d MMM yyyy HH:mm:ss Z";

    public static final Locale DEFAULT_LOCALE = Locale.US;

    public static final String BASE_URL =
        "http://tv.intern.de/rss/rss.php?site=Suche&m=3&k=alle&q=alle&t=1&s=";

    public static final String[][] CHANNELS = {
        new String[] {"ard",  "ARD"},
        new String[] {"zdf",  "ZDF"},
        new String[] {"rtl",  "RTL"},
        new String[] {"sat1", "SAT1"},
        new String[] {"pro7", "PRO7"},
        new String[] {"rtl2", "RTL2"},
        new String[] {"kaka", "KABEL1"},
        new String[] {"vox",  "VOX"}
    };


    public static RssFeed getFeed(URL url) {
        Document raw = getDocumentFromURL(url);
        return parseFeedDocument(raw);
    }


    public static Document getDocumentFromURL(URL url) {
        try {
            InputStream is  = url.openStream();
            Document    doc = XMLUtils.parseDocument(is);

            return doc;
        }
        catch (IOException ioe) {
            Log.e("TvR [RssReader]", ioe.getMessage());
        }

        return null;
    }


    public static RssFeed parseFeedDocument(Document doc) {
        Node     channel = RssXMLUtils.getChannel(doc);
        NodeList items   = RssXMLUtils.getItems(channel);

        // TODO Parse "lastBuild" date
        RssFeed feed = new RssFeed();

        int num = items != null ? items.getLength() : 0;

        for (int i = 0; i < num; i++) {
            RssItem item = parseItem(items.item(i));

            if (item != null) {
                feed.addItem(item);
            }
        }

        Log.d(
            "TvR [RssReader]",
            "Feed contains " + feed.getItemSize() + " items.");

        return feed;
    }


    public static RssItem parseItem(Node item) {
        String title = (String) XMLUtils.xpath(
            item,
            "link/text()",
            XPathConstants.STRING);

        String description = (String) XMLUtils.xpath(
            item,
            "description/text()",
            XPathConstants.STRING);

        String pubDate = (String) XMLUtils.xpath(
            item,
            "pubDate/text()",
            XPathConstants.STRING);

        if (title != null && title.length() > 0
            && pubDate != null && pubDate.length() > 0) {

            SimpleDateFormat sdf = new SimpleDateFormat(
                DEFAULT_DATE_FORMAT,
                DEFAULT_LOCALE);

            try {
                Date date = sdf.parse(pubDate);

                return new RssItem(title, description, date);
            }
            catch (ParseException pe) {
                Log.e("TvR [RssReader]", pe.getMessage());
            }
        }

        return null;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
