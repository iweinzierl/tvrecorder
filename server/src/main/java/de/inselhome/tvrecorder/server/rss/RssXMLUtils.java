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
package de.inselhome.tvrecorder.server.rss;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.inselhome.tvrecorder.common.utils.XMLUtils;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class RssXMLUtils {

    public static final String XPATH_CHANNEL = "/rss/channel";
    public static final String XPATH_ITEMS   = "/rss/channel/item";


    private RssXMLUtils() {
        // no instances allowed
    }


    public static synchronized Node getChannel(Document feed) {
        return (Node) XMLUtils.xpath(feed, XPATH_CHANNEL, XPathConstants.NODE);
    }


    public static synchronized NodeList getItems(Node channel) {
        return (NodeList) XMLUtils.xpath(
            channel,
            XPATH_ITEMS,
            XPathConstants.NODESET);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
