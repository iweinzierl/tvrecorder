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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class RssFeed implements Serializable {

    protected List<RssItem> items;

    protected Date lastCreated;


    public RssFeed() {
        items = new ArrayList<RssItem>();
    }


    public RssFeed(int capacity) {
        items = new ArrayList<RssItem>(capacity);
    }


    public void addItem(RssItem item) {
        items.add(item);
    }


    public RssItem getItem(int idx) {
        if (idx < getItemSize()) {
            return items.get(idx);
        }

        return null;
    }


    public int getItemSize() {
        return items.size();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
