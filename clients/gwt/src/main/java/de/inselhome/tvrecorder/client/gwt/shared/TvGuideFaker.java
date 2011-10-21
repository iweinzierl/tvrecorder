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
package de.inselhome.tvrecorder.client.gwt.shared;

import java.util.ArrayList;
import java.util.List;

import de.inselhome.tvrecorder.client.gwt.shared.model.Channel;
import de.inselhome.tvrecorder.client.gwt.shared.model.DefaultChannel;
import de.inselhome.tvrecorder.client.gwt.shared.model.DefaultTvShow;
import de.inselhome.tvrecorder.client.gwt.shared.model.TvShow;


public class TvGuideFaker {

    public static List<Channel> getChannels(boolean withTvShows) {
        List<Channel> channels = new ArrayList<Channel>();

        Channel ard = new DefaultChannel("ARD", "ARD");
        Channel zdf = new DefaultChannel("ZDF", "ZDF");
        Channel rtl = new DefaultChannel("RTL", "RTL");
        Channel sat = new DefaultChannel("SAT.1", "SAT.1");
        Channel pro = new DefaultChannel("PRO 7", "PRO 7");

        if (withTvShows) {
            ard.addTvShows(getTvShows());
            zdf.addTvShows(getTvShows());
            rtl.addTvShows(getTvShows());
            sat.addTvShows(getTvShows());
            pro.addTvShows(getTvShows());
        }

        channels.add(ard);
        channels.add(zdf);
        channels.add(rtl);
        channels.add(sat);
        channels.add(pro);

        return channels;
    }


    public static List<TvShow> getTvShows() {
        List<TvShow> tvshows = new ArrayList<TvShow>();

        tvshows.add(new DefaultTvShow(
            "Simpsons",
            "Episode 1"
        ));

        tvshows.add(new DefaultTvShow(
            "Simpsons",
            "Episode 2"
        ));

        tvshows.add(new DefaultTvShow(
            "Simpsons",
            "Episode 3"
        ));

        tvshows.add(new DefaultTvShow(
            "Simpsons",
            "Episode 4"
        ));

        return tvshows;
    }
}
