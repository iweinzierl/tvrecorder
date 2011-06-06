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
package de.inselhome.tvrecorder.server.tvguide.rss;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.objects.TvShow;

import de.inselhome.tvrecorder.server.App;
import de.inselhome.tvrecorder.server.tvguide.TvShowManager;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdateEvent;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdateListener;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class RssTvShowManager implements TvShowManager, TvShowUpdateListener {

    private static Logger logger = Logger.getLogger(RssTvShowManager.class);


    protected RssTvShowUpdater updater;

    protected List<ChannelWithTvGuide> channels;

    protected boolean isStandalone;


    public static void main(String[] args) {
        App.configureLogging();

        logger.debug("Start RssTvShowManager as standalone application.");

        RssTvShowManager manager = new RssTvShowManager();
        manager.isStandalone     = true;

        manager.start();
    }


    public RssTvShowManager() {
        updater      = new RssTvShowUpdater(null);
        isStandalone = false;
        updater.addTvShowUpdateListener(this);
    }


    public void start() {
        updater.start();
    }


    public List<ChannelWithTvGuide> getChannels() {
        //if (updater.inProgress()) {
        //    // TODO WAIT UNTIL THE UPDATE ENDS
        //}

        return channels;
    }


    public void onTvShowUpdate(TvShowUpdateEvent event) {
        List<ChannelWithTvGuide> channels = event.getUpdated();

        this.channels = channels;

        if (isStandalone) {
            dumpChannels(channels);
        }
    }

    public static void dumpChannels(List<ChannelWithTvGuide> channels) {
        for (ChannelWithTvGuide c: channels) {
            logger.debug("+++++++++++++++++ " + c.getDescription() + " +++++++++++++++++");

            Collection<TvShow> tvshows = c.getSortedListing();

            for (TvShow show: tvshows) {
                logger.debug(show);
            }
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
