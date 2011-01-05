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
package de.inselhome.tvrecorder.server.utils;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.common.objects.Job;


/**
 * This {@link AtJobCreator} may be used to stop running jobs. An <i>at</i> job
 * is created that calle {@link DEFAULT_STOP_SCRIPT}.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class StopAtJobCreator extends AtJobCreator {

    /** The default script to stop running recordings. */
    public static final String DEFAULT_STOP_SCRIPT = "contrib/stop_records.sh";

    /** The logger that is used in this class */
    private static Logger logger = Logger.getLogger(StartAtJobCreator.class);


    /**
     * The default constructor that calls the super constructor with the {@link
     * Job}.
     *
     * @param job The job that needs to be stopped.
     */
    public StopAtJobCreator(Job job) {
        super(job);
    }


    /**
     * This method returns the stop time of the record {@link Job}.
     *
     * @return the stop time.
     */
    public Date getStartDate() {
        return job.getEnd();
    }


    /**
     * This method returns {@link DEFAULT_STOP_SCRIPT}.
     *
     * @return {@link DEFAULT_STOP_SCRIPT}.
     */
    public File createScriptFile() {
        File file = new File(DEFAULT_STOP_SCRIPT);

        if (file.exists() && file.canRead()) {
            return file;
        }

        return null;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
