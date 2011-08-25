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
package de.inselhome.tvrecorder.server.tvguide.xmltv;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;

import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.objects.TvShow;
import de.inselhome.tvrecorder.common.utils.XMLUtils;

import de.inselhome.tvrecorder.server.backend.Backend;
import de.inselhome.tvrecorder.server.config.Config;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdateEvent;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdateListener;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdater;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class XmlTvShowUpdater implements TvShowUpdater {

    private static Logger logger = Logger.getLogger(XmlTvShowUpdater.class);


    protected List<ChannelWithTvGuide> updated;

    protected List<TvShowUpdateListener> listeners;

    protected Map<String, ChannelWithTvGuide> channels;

    protected File xmltv;

    protected DateFormat dateFormat;

    protected long interval;


    public static final String XPATH_CHANNELS = "/tv/channel";
    public static final String XPATH_SHOWS    = "/tv/programme";


    public XmlTvShowUpdater(File xmltv, long interval) {
        this.updated   = new ArrayList<ChannelWithTvGuide>();
        this.listeners = new ArrayList<TvShowUpdateListener>();
        this.channels  = new HashMap<String, ChannelWithTvGuide>();
        this.xmltv     = xmltv;
        this.interval  = interval;
    }


    public void start() {
        logger.info("Started XmlTvShowUpdater...");

        initDateTimeFormat();
        parseChannels();

        logger.info("Run TvShowUpdater each: " + interval + "ms.");

        Thread updater = new Thread("XmlTvShowUpdater-Thread") {
            public void run() {
                do {
                    runUpdate();

                    try {
                        Thread.sleep(interval);
                    }
                    catch (InterruptedException ie) {
                        logger.warn(ie, ie);
                    }
                }
                while (true);
            }
        };
        updater.start();
    }


    protected void initDateTimeFormat() {
        Config cfg    = Config.getInstance();
        String format = cfg.getProperty(Config.XPATH_DATETIME_FORMAT);

        dateFormat = new SimpleDateFormat(format);
    }


    protected void parseChannels() {
        Document doc   = XMLUtils.parseDocument(xmltv);
        NodeList nodes = (NodeList) XMLUtils.xpath(
            doc, XPATH_CHANNELS, XPathConstants.NODESET);

        int num = nodes != null ? nodes.getLength() : 0;

        for (int i = 0; i < num; i++) {
            Element e    = (Element) nodes.item(i);
            String  id   = e.getAttribute("id");
            String  name = (String) XMLUtils.xpath(
                e, "display-name/text()", XPathConstants.STRING);

            if (id == null || id.length() == 0) {
                continue;
            }

            name = name != null ? name : id;

            channels.put(id, new ChannelWithTvGuide(id, name));
        }

        logger.info("XmlTv file contains " + channels.size() + " channels.");
    }


    public void addTvShowUpdateListener(TvShowUpdateListener listener) {
        listeners.add(listener);
    }


    protected synchronized void addUpdated(ChannelWithTvGuide update) {
        updated.add(update);
    }


    protected synchronized void clearUpdated() {
        updated.clear();
    }


    protected void runUpdate() {
        clearUpdated();

        Document doc = XMLUtils.parseDocument(xmltv);

        logger.info("Start parsing XmlTv TvGuide.");
        long start = System.currentTimeMillis();

        if (doc == null) {
            logger.warn("Could not parse xmltv file '" + xmltv.getAbsolutePath() + "'");
            return;
        }

        NodeList shows = (NodeList) XMLUtils.xpath(
            doc, XPATH_SHOWS, XPathConstants.NODESET);

        int num = shows != null ? shows.getLength() : 0;

        for (int i = 0; i < num; i++) {
            processTvShow((Element) shows.item(i));
        }

        long end = System.currentTimeMillis();

        logger.info("Found " + num + " TvShows in xmltv file.");
        logger.info("Scan took " + (end-start) + " ms.");

        Collection<ChannelWithTvGuide> result = channels.values();
        updated = new ArrayList<ChannelWithTvGuide>(result.size());

        for (ChannelWithTvGuide c: result) {
            updated.add(c);
        }

        fireTvShowsChanged(updated);
    }


    protected void processTvShow(Element e) {
        String channelId = e.getAttribute("channel");
        String startStr  = e.getAttribute("start");
        String endStr    = e.getAttribute("stop");
        String title     = (String) XMLUtils.xpath(
            e, "title/text()", XPathConstants.STRING);
        String desc      = (String) XMLUtils.xpath(
            e, "desc/text()",  XPathConstants.STRING);

        ChannelWithTvGuide channel = channels.get(channelId);

        if (channel == null) {
            logger.warn("No such channel found: " + channelId);
            return;
        }

        if (title == null || startStr == null) {
            logger.warn("No title or start time given.");
            return;
        }

        try {
            channel.addTvShow(new TvShow(
                title,
                desc,
                dateFormat.parse(startStr),
                dateFormat.parse(endStr)));
        }
        catch (ParseException pe) {
            logger.warn(pe, pe);
        }
    }


    protected void fireTvShowsChanged(List<ChannelWithTvGuide> channels) {
        logger.debug("Inform TvShowUpdateListener that channels have changed.");

        for (TvShowUpdateListener listener: listeners) {
            listener.onTvShowUpdate(new TvShowUpdateEvent(channels));
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
