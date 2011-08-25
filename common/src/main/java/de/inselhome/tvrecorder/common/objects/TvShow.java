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

import java.io.Serializable;

import java.util.Date;

import de.inselhome.tvrecorder.common.utils.DateUtils;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvShow implements Serializable, Comparable {

    protected String title;
    protected String description;
    protected String imageUrl;
    protected Date   start;
    protected Date   end;


    public TvShow() {
    }


    public TvShow(String title, String description, Date start) {
        this(title, description, start, null);
    }


    public TvShow(String title, String description, Date start, Date end) {
        this.title       = title;
        this.description = description;
        this.start       = start;
        this.end         = end;
    }


    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }


    public Date getStart() {
        return start;
    }


    public Date getEnd() {
        return end;
    }


    public int compareTo(Object obj) {
        return obj instanceof TvShow
            ? start.compareTo(((TvShow) obj).start)
            : 1;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtils.format(start, DateUtils.DATETIME_FORMAT));
        sb.append("  -  ");
        sb.append(title);

        return sb.toString();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
