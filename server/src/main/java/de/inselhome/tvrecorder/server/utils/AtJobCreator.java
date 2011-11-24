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

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.utils.DateUtils;


/**
 * This is the abstract base class to create new <i>at</i> jobs.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public abstract class AtJobCreator {

    /** The regular expression used to find the at job id. */
    public static final Pattern JOB_REGEX =
        Pattern.compile(".*job\\s(\\d+)\\sat.*");


    /** The default directoy where the jobs are saved in. */
    public static final String DEFAULT_JOB_DIR = "jobs";

    /** The logger that is used in this class. */
    private static Logger logger = Logger.getLogger(StartAtJobCreator.class);

    /** The job */
    protected Job job;


    /**
     * This constructor need to be called by subclasses.
     *
     * @param job the job.
     */
    protected AtJobCreator(Job job) {
        this.job = job;
    }


    /**
     * This method is abstract and needs to be implemented by subclasses. It
     * should return a shell script that is called by <i>at</i>.
     *
     * @return the shell script.
     */
    public abstract File createScriptFile();


    /**
     * This method is abstract and needs to be implemented by subclasses. It
     * should return the date when the <i>at</i> job is executed-
     *
     * @return the date when the <i>at</i> job is executed.
     */
    public abstract Date getStartDate();


    /**
     * This method should be called to create the <i>at</i> job. After calling
     * this method, a shell script is created by {@link createScriptFile()} that
     * is executed by the job and the <i>at</i> job is created.
     *
     * @return the at job id or -1 if at job creation failed.
     */
    public int startJob() {
        File file = createScriptFile();

        if (file != null) {
            logger.info("The job file has been created successfully.");

            int jobId = createAtJob(getStartDate(), file);

            if (jobId < 0) {
                logger.error("Job creation failed.");

                return jobId;
            }

            logger.info("Job has been created successfully.");

            return jobId;
        }

        return -1;
    }


    /**
     * This method creates the <i>at</i> job that executes <i>file</i> at
     * <i>date</i>.
     *
     * @param date the execution time.
     * @param file the shell script that is executed.
     *
     * @return the at job id or -1 if at job creation failed.
     */
    public int createAtJob(Date date, File file) {
        if (date == null || file == null) {
            logger.error("Job creation failed: job or jobfile null.");
            return -1;
        }

        String timespec = DateUtils.format(date, DateUtils.TIMESPEC_FORMAT);

        if (timespec == null) {
            logger.error("Job creation failed: could not create timespec.");
            return -1;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("at -t ");
        sb.append(timespec);
        sb.append(" -f ");
        sb.append(file.getAbsolutePath());

        Runtime runtime = Runtime.getRuntime();

        try {
            logger.debug("Try to execute the command: " + sb.toString());
            Process proc = runtime.exec(sb.toString());

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(proc.getErrorStream()));

            String line  = null;
            int    jobId = -1;

            while ((line = reader.readLine()) != null) {
                if (jobId < 0) {
                    jobId = extractJobId(line);
                }
            }

            try {
                proc.waitFor();

                logger.debug("Exit value of job creation: " + proc.exitValue());

                return jobId;
            }
            catch (InterruptedException ie) {
                logger.error(ie.getLocalizedMessage());
            }
        }
        catch (IOException ioe) {
            logger.error(ioe.getLocalizedMessage());
        }

        return -1;
    }


    protected static int extractJobId(String line) {
        Matcher m = JOB_REGEX.matcher(line);

        int jobId = -1;

        if (m.matches()) {
            if (m.groupCount() > 0) {
                String group = m.group(1);

                try {
                    jobId = Integer.valueOf(group);

                    logger.debug("Created at job: " + jobId);
                }
                catch (NumberFormatException nfe) {
                    // this should never happen
                    logger.debug("Found group which is no Integer: " + group);
                }
            }
        }

        return jobId;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
