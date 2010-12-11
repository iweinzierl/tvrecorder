/**
 * Copyright (C) 2010 Ingo Weinzierl (ingo_weinzierl@web.de)
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
package de.inselhome.tvrecorder.client.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * This helper class defines some functions to work with dates.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public final class DateUtils {

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String TIME_FORMAT = "HH:mm";

    protected static DateFormat dateFormat;
    protected static DateFormat timeFormat;

    static {
        dateFormat = new SimpleDateFormat(DATE_FORMAT);
        timeFormat = new SimpleDateFormat(TIME_FORMAT);
    }


    /**
     * This function will return a formatted time string of <i>cal</i>. This
     * function makes use of {@link TIME_FORMAT}.
     *
     * @param cal The Calendar object to format.
     *
     * @return the formatted time string.
     */
    public static final String formatTime(Calendar cal) {
        return timeFormat.format(cal.getTime());
    }


    /**
     * This funtion will return a formatted date string of <i>cal</i>. This
     * function makes use of {@link DATE_FORMAT}.
     *
     * @param cal The Calendar object to format.
     *
     * @return the formatted date string.
     */
    public static final String formatDate(Calendar cal) {
        return dateFormat.format(cal.getTime());
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
