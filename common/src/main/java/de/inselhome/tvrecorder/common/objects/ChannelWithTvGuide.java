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
package de.inselhome.tvrecorder.common.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class ChannelWithTvGuide extends Channel {

    protected Map<Date, TvShow> listing;


    public ChannelWithTvGuide() {
        listing = new HashMap<Date, TvShow>();
    }


    public ChannelWithTvGuide(Channel c) {
        super(c.key, c.description);
        listing = new HashMap<Date, TvShow>();
    }


    public ChannelWithTvGuide(String key, String description) {
        super(key, description);
        listing = new HashMap<Date, TvShow>();
    }


    public void addTvShow(TvShow show) {
        listing.put(show.getStart(), show);
    }


    public Map<Date, TvShow> getListing() {
        return listing;
    }


    public Collection<TvShow> getSortedListing() {
        if (listing != null && listing.size() > 0) {
            TreeMap<Date, TvShow> sorted = new TreeMap<Date, TvShow>(listing);
            return sorted.values();
        }

        return new ArrayList<TvShow>();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
