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
package de.inselhome.tvrecorder.client.android.model;

import java.io.Serializable;

import java.util.Date;


/**
 * This class represent a tv show. A tv show currently consist of a title, a
 * description, a start and an end date.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvShow implements Serializable, Comparable {

    protected String title;
    protected String description;
    protected Date   start;
    protected Date   end;


    public TvShow() {
    }


    public TvShow(String title, String description, Date start) {
        this.title       = title;
        this.description = description;
        this.start       = start;
    }


    public TvShow(String title, String description, Date start, Date end) {
        this(title, description, start);
        this.end = end;
    }


    public String getTitle() {
        return title;
    }


    public String description() {
        return description;
    }


    public Date getStart() {
        return start;
    }


    public Date getEnd() {
        return end;
    }


    public void setEnd(Date end) {
        this.end = end;
    }


    public int compareTo(Object obj) {
        if (obj instanceof TvShow) {
            TvShow show = (TvShow) obj;
            return start.compareTo(show.start);
        }
        else {
            return -1;
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
