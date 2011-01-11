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

    public static final String DATETIME_FORMAT = "dd.MM.yyyy HH:mm";

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    public static final String TIME_FORMAT = "HH:mm";


    /**
     * This function formats a given {@link Date} based on a given format.
     *
     * @param date the date to format.
     * @param format the format to use.
     *
     * @return the formatted date/time or null, if <i>date</i> or <i>format</i>
     * are null.
     */
    public static String format(Date date, String format) {
        if (date == null || format == null) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(format);

        return df.format(date);
    }


    /**
     * This method compares two dates with a precision of minutes.
     *
     * @param end An end date.
     * @param start A start date.
     *
     * @return true, if <i>end</i> is greater that that <i>start</i>, otherwise
     * false.
     */
    public static boolean isEndGreaterThanStart(Date end, Date start) {
        long endTime   = end.getTime() / (1000 * 60);
        long startTime = start.getTime() / (1000 * 60);

        return endTime <= startTime ? false : true;
    }


    /**
     * This method checks, if timerange specified by <i>secondStart</i> and
     * <i>secondEnd</i> collides with the timerange specified by
     * <i>firstStart</i> and <i>firstEnd</i>.
     *
     * @param firstStart the start point of the first timerange.
     * @param firstEnd   the end point of the first timerange.
     * @param secondStart the start point of the first timerange.
     * @param secondEnd   the end point of the first timerange.
     *
     * @return true, if there is a collision, otherwise false.
     */
    public static boolean doesTimerangesCollide(
        Date firstStart, Date firstEnd, Date secondStart, Date secondEnd)
    {
        long firstStartTime  = firstStart.getTime();
        long firstEndTime    = firstEnd.getTime();
        long secondStartTime = secondStart.getTime();
        long secondEndTime   = secondEnd.getTime();

        return !(secondStartTime > firstEndTime || secondEndTime < firstEndTime);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
