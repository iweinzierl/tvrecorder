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
package de.inselhome.tvrecorder.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class DateUtils {

    public static final String TIMESPEC_FORMAT = "yyyyMMddHHmm";

    public static final String OUTPUT_FORMAT = "dd-MM-yyyy_HH-mm";


    public static String format(Date date, String format) {
        if (date == null || format == null) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(format);

        return df.format(date);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
