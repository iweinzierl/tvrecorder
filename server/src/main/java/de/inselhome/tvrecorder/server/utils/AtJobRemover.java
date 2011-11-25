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
package de.inselhome.tvrecorder.server.utils;

import java.io.IOException;

import org.apache.log4j.Logger;


/**
 * This {@link AtJobCreator} may be used to create an <i>at</i> job to start
 * recording a tv movie.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class AtJobRemover {

    private static final Logger logger = Logger.getLogger(AtJobRemover.class);


    public static void remove(int atJobId) {
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(getCommand(atJobId));
        }
        catch (IOException ioe) {
            logger.error("Error while removing at job: " + ioe.getMessage());
        }
    }


    protected static String getCommand(int atJobId) {
        return "atrm " + atJobId;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
