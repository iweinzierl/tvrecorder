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
import java.util.List;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;

import de.inselhome.tvrecorder.server.config.Config;
import de.inselhome.tvrecorder.server.tvguide.TvShowManager;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdateEvent;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdateListener;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class XmlTvShowManager implements TvShowManager, TvShowUpdateListener {

    private static Logger logger = Logger.getLogger(XmlTvShowManager.class);


    protected XmlTvShowUpdater updater;

    protected List<ChannelWithTvGuide> channels;


    public static final String XPATH_XMLTV_FILE =
        "/tvrecorder/tvguide/xmltv/text()";


    public XmlTvShowManager() {
        Config cfg    = Config.getInstance();
        String interv = cfg.getProperty(Config.XPATH_UPDATE_INTERVAL);
        String xmltv  = cfg.getProperty(XPATH_XMLTV_FILE);

        updater = new XmlTvShowUpdater(new File(xmltv), Long.parseLong(interv));
        updater.addTvShowUpdateListener(this);
    }


    public void start() {
        updater.start();
    }


    public List<ChannelWithTvGuide> getChannels() {
        return channels;
    }


    public void onTvShowUpdate(TvShowUpdateEvent event) {
        List<ChannelWithTvGuide> channels = event.getUpdated();

        logger.info("Update TvShows on " + channels.size() + " channels.");

        this.channels = channels;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
