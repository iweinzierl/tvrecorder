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
package de.inselhome.tvrecorder.common.objects;

import java.io.Serializable;
import java.util.Date;


/**
 * This class represents a job for recording. A {@link Job} is made of a start
 * and an end time, a channel and a name - where the name should be optional.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class Job
implements   Serializable
{
    /**
     * The start time.
     */
    protected Date start;

    /**
     * The end time.
     */
    protected Date end;

    /**
     * The channel.
     */
    protected Channel  channel;

    /**
     * A name for the Job.
     */
    protected String   name;


    public Job() {
    }


    /**
     * This is the default constructor to create new jobs.
     *
     * @param start The start time.
     * @param end   The end time.
     * @param channel The channel used for the recording.
     * @param name The name of the job.
     */
    public Job(Date start, Date end, Channel channel, String name) {
        this.start   = start;
        this.end     = end;
        this.channel = channel;
        this.name    = name;
    }


    /**
     * Retrieves the start time.
     *
     * @return the start time.
     */
    public Date getStart() {
        return start;
    }


    /**
     * Retrieves the end time.
     *
     * @return the end time.
     */
    public Date getEnd() {
        return end;
    }


    /**
     * Retrieves the channel.
     *
     * @return the channel.
     */
    public Channel getChannel() {
        return channel;
    }


    /**
     * Retrieves the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
