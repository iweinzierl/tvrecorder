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
import de.inselhome.tvrecorder.common.utils.DateUtils;

import de.inselhome.tvrecorder.server.utils.CmdProducer;


/**
 * This {@link AtJobCreator} may be used to create an <i>at</i> job to start
 * recording a tv movie.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class StartAtJobCreator extends AtJobCreator {

    /** The {@link Logger} that is used in this class. */
    private static Logger logger = Logger.getLogger(StartAtJobCreator.class);

    /** The {@link CmdProducer} that creates the command line for the shell
     * script */
    protected CmdProducer cmdProducer;


    /**
     * The default constructor.
     *
     * @param job The {@link Job} that contains information about the recording.
     */
    public StartAtJobCreator(Job job, CmdProducer cmdProducer) {
        super(job);

        this.cmdProducer = cmdProducer;
    }


    /**
     * This method retrieves the start time of the recording.
     *
     * @return the start time of {@link job}.
     */
    public Date getStartDate() {
        return job.getStart();
    }


    /**
     * This method creates a new shell script with a command that is used to
     * start recording the job.
     *
     * @return the shell script.
     */
    public File createScriptFile() {
        File file = FileUtils.getFile(createFilename());

        if (file == null) {
            logger.error("Job creation failed: could not create job file.");
            return null;
        }

        String cmd = cmdProducer.produce(job);

        boolean success = FileUtils.writeStringToFile(file, cmd);

        if (!success) {
            FileUtils.deleteFile(file);
            return null;
        }

        return file;
    }


    /**
     * This method creates a filename for the shell script that contains of the
     * start time, the end time and if the output name if it is existing.
     *
     * @return the filename of the shell script.
     */
    public String createFilename() {
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtils.format(job.getStart(), DateUtils.TIMESPEC_FORMAT));
        sb.append("-");
        sb.append(DateUtils.format(job.getEnd(), DateUtils.TIMESPEC_FORMAT));

        String out = job.getName();
        if (out != null && !out.equals("")) {
            sb.append("-");
            sb.append(out);
        }

        sb.append(".sh");

        return DEFAULT_JOB_DIR + "/" + sb.toString();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
