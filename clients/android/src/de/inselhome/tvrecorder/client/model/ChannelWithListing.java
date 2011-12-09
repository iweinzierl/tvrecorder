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
package de.inselhome.tvrecorder.client.model;

import java.util.Iterator;
import java.util.TreeSet;

import de.inselhome.tvrecorder.common.objects.Channel;


/**
 * Inherits from <i>Channel</i>. The difference to <i>Channel</i> is, that this
 * channel knows about its tv shows.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class ChannelWithListing extends Channel {

    protected TreeSet<TvShow> listing;


    public ChannelWithListing() {
        super();
        listing = new TreeSet<TvShow>();
    }


    public ChannelWithListing(String key, String description) {
        super(key, description);
        listing = new TreeSet<TvShow>();
    }


    public void addTvShow(TvShow show) {
        listing.add(show);
    }


    public int getTvShowCount() {
        return listing.size();
    }


    public Iterator<TvShow> getTvShows() {
        return listing.iterator();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
