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

    public static final String SQL_INSERT_NEW_JOB  =
        "INSERT INTO jobs (start, end, channel, name) VALUES (?, ?, ?, ?)";

    private static Logger logger = Logger.getLogger(Backend.class);


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

            ResultSet rs = statement.executeQuery(SQL_SELECT_ALL_JOBS);
            while(rs.next()) {
                String channel = rs.getString("channel");
                String name    = rs.getString("name");
                Date   start   = rs.getDate("start");
                Date   end     = rs.getDate("end");

                // TODO we should not create new Channels on our own, but use a
                // parser or factory checking if there is a Channel existing
                // with such key.
                Channel chan = new Channel(channel, channel);

                jobs.add(new Job(start, end, chan, name));
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
     * This methods inserts an existing {@link Job} into the database.
     *
     * @param The job that we want to insert.
     */
    public void insertJob(Job job) {
        try {
            Connection connection       = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                SQL_INSERT_NEW_JOB);

            statement.setDate(1, new Date(job.getStart().getTime()));
            statement.setDate(2, new Date(job.getEnd().getTime()));
            statement.setString(3, job.getChannel().getKey());
            statement.setString(4, job.getName());

            statement.executeUpdate();
        }
        catch (SQLException sqle) {
            logger.error(sqle.getLocalizedMessage());
        }
        catch (ClassNotFoundException cnfe) {
            logger.error(cnfe.getLocalizedMessage());
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
