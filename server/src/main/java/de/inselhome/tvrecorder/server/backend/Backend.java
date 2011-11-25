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
package de.inselhome.tvrecorder.server.backend;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.Job;


/**
 * @author <a href="mailto:ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class Backend {

    public static final String SQL_SELECT_ALL_JOBS = "SELECT * FROM jobs";

    public static final String SQL_SELECT_QUEUED_JOBS =
        "SELECT * FROM jobs where start >= ? OR end >= ?";

    public static final String SQL_INSERT_NEW_JOB  =
        "INSERT INTO jobs (start, end, channel, name, atJobId_start, atJobId_end) VALUES (?, ?, ?, ?, ?, ?)";

    public static final String SQL_FIND_AT_JOB_ID =
        "SELECT atJobId_start, atJobId_end FROM jobs WHERE start = ? AND end = ? AND channel = ?";

    public static final String SQL_REMOVE_JOB =
        "DELETE FROM jobs WHERE start = ? AND end = ? AND channel = ?";

    private static Logger logger = Logger.getLogger(Backend.class);


    private class JobCreator {
        public Job create(ResultSet rs)
        throws SQLException
        {
            String channel = rs.getString("channel");
            String name    = rs.getString("name");
            Date   start   = rs.getDate("start");
            Date   end     = rs.getDate("end");

            // TODO we should not create new Channels on our own, but use a
            // parser or factory checking if there is a Channel existing
            // with such key.
            Channel chan = new Channel(channel, channel);

            return new Job(start, end, chan, name);
        }
    } // end of JobCreator


    public Backend() {
    }


    /**
     *  This method fetches all jobs from database and returns them as array.
     *
     *  @return all jobs stored in the database.
     */
    public Job[] loadJobs() {
        List<Job> jobs = new ArrayList<Job>();

        try {
            Connection connection = DBConnection.getConnection();
            Statement statement   = connection.createStatement();

            JobCreator creator = new JobCreator();

            ResultSet rs = statement.executeQuery(SQL_SELECT_ALL_JOBS);
            while(rs.next()) {
                jobs.add(creator.create(rs));
            }

            return (Job[]) jobs.toArray(new Job[jobs.size()]);
        }
        catch (SQLException sqle) {
            logger.error(sqle.getLocalizedMessage());
        }
        catch (ClassNotFoundException cnfe) {
            logger.error(cnfe.getLocalizedMessage());
        }

        return null;
    }


    /**
     *  This method fetches all jobs from database and returns them as array.
     *
     *  @return queued jobs stored in the database.
     */
    public Job[] loadQueuedJobs(java.util.Date date) {
        List<Job> jobs = new ArrayList<Job>();

        try {
            Connection connection       = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQL_SELECT_QUEUED_JOBS);

            statement.setDate(1, new Date(date.getTime()));
            statement.setDate(2, new Date(date.getTime()));

            JobCreator creator = new JobCreator();

            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                jobs.add(creator.create(rs));
            }

            return jobs.toArray(new Job[jobs.size()]);
        }
        catch (SQLException sqle) {
            logger.error(sqle.getLocalizedMessage());
        }
        catch (ClassNotFoundException cnfe) {
            logger.error(cnfe.getLocalizedMessage());
        }

        return null;
    }


    /**
     * This methods inserts an existing {@link Job} into the database.
     *
     * @param The job that we want to insert.
     */
    public void insertJob(Job job, int jobId_start, int jobId_end) {
        try {
            Connection connection       = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQL_INSERT_NEW_JOB);

            statement.setDate(1, new Date(job.getStart().getTime()));
            statement.setDate(2, new Date(job.getEnd().getTime()));
            statement.setString(3, job.getChannel().getKey());
            statement.setString(4, job.getName());
            statement.setInt(5, jobId_start);
            statement.setInt(6, jobId_end);

            statement.executeUpdate();
        }
        catch (SQLException sqle) {
            logger.error(sqle.getLocalizedMessage());
        }
        catch (ClassNotFoundException cnfe) {
            logger.error(cnfe.getLocalizedMessage());
        }
    }


    public int[] findAtJobIds(Job job) {
        int[] jobIds = new int[] { -1, -1 };

        try {
            Connection connection       = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQL_FIND_AT_JOB_ID);

            if (logger.isDebugEnabled()) {
                logger.debug("Try to find atJobId for:");
                logger.debug("  start:   " + job.getStart().getTime());
                logger.debug("  end:     " + job.getEnd().getTime());
                logger.debug("  channel: " + job.getChannel().getKey());
            }

            statement.setLong(1, job.getStart().getTime());
            statement.setLong(2, job.getEnd().getTime());
            statement.setString(3, job.getChannel().getKey());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                jobIds[0] = rs.getInt("atJobId_start");
                jobIds[1] = rs.getInt("atJobId_end");

                if (jobIds[0] > 0 && jobIds[1] > 0) {
                    break;
                }
            }

            connection.close();
        }
        catch (SQLException sqle) {
            logger.error(sqle.getLocalizedMessage());
        }
        catch (ClassNotFoundException cnfe) {
            logger.error(cnfe.getLocalizedMessage());
        }

        return jobIds;
    }


    public int removeJob(Job job) {
        try {
            Connection connection       = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQL_REMOVE_JOB);

            if (logger.isDebugEnabled()) {
                logger.debug("Try to remove job:");
                logger.debug("  start:   " + job.getStart().getTime());
                logger.debug("  end:     " + job.getEnd().getTime());
                logger.debug("  channel: " + job.getChannel().getKey());
                logger.debug("  name:    " + job.getName());
            }

            statement.setLong(1, job.getStart().getTime());
            statement.setLong(2, job.getEnd().getTime());
            statement.setString(3, job.getChannel().getKey());

            int count = statement.executeUpdate();

            connection.close();

            return count;
        }
        catch (SQLException sqle) {
            logger.error(sqle.getLocalizedMessage());
        }
        catch (ClassNotFoundException cnfe) {
            logger.error(cnfe.getLocalizedMessage());
        }

        return -1;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
