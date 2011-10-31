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

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;


public class DateUtils {

    public static final String DATE_FORMAT = "EEE, MMM  d";

    public static final String TIME_FORMAT = "HH:mm";


    public static String format(Date date, String format) {
        DateTimeFormat df = DateTimeFormat.getFormat(format);
        return df.format(date);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
