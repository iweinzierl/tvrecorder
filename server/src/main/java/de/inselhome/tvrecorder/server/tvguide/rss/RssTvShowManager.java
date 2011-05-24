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
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdateEvent;
import de.inselhome.tvrecorder.server.tvguide.TvShowUpdateListener;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class RssTvShowManager implements TvShowUpdateListener {

    private static Logger logger = Logger.getLogger(RssTvShowManager.class);


    public static void main(String[] args) {
        App.configureLogging();

        logger.debug("Start RssTvShowManager as standalone application.");

        RssTvShowUpdater updater = new RssTvShowUpdater(null);
        updater.addTvShowUpdateListener(new RssTvShowManager());
        updater.start();
    }


    public void onTvShowUpdate(TvShowUpdateEvent event) {
        List<ChannelWithTvGuide> channels = event.getUpdated();

        dumpChannels(channels);
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
