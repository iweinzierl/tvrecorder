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

import java.util.Date;


public class DefaultTvShow implements TvShow {

    protected String title;
    protected String description;
    protected String category;
    protected String imageUrl;

    protected Date start;
    protected Date end;

    protected int length;


    public DefaultTvShow(String title, String description) {
        this.title       = title;
        this.description = description;
    }


    public DefaultTvShow(String title, String description, String category) {
        this(title, description);
        this.category = category;
    }


    public DefaultTvShow(
        String title,
        String description,
        String category,
        Date   start
    ) {
        this(title, description, category);
        this.start = start;
    }


    public DefaultTvShow(
        String title,
        String description,
        String category,
        Date   start,
        Date   end
    ) {
        this(title, description, category, start);
        this.end = end;
    }


    public DefaultTvShow(
        String title,
        String description,
        String category,
        Date   start,
        Date   end,
        int    length
    ) {
        this(title, description, category, start, end);
        this.length = length;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public int getLength() {
        return length;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
