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
package de.inselhome.tvrecorder.client.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;


public class DefaultChannel implements Channel {

    protected String key;
    protected String name;

    protected List<TvShow> tvshows;


    public DefaultChannel(String key, String name) {
        this.key     = key;
        this.name    = name;
        this.tvshows = new ArrayList<TvShow>();
    }


    public String getKey() {
        return key;
    }


    public String getName() {
        return name;
    }


    public void addTvShow(TvShow tvshow) {
        tvshows.add(tvshow);
    }


    public void addTvShows(List<TvShow> tvshows) {
        this.tvshows.addAll(tvshows);
    }


    public List<TvShow> getTvShows() {
        return tvshows;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
