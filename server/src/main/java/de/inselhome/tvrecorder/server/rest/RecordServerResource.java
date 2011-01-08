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
package de.inselhome.tvrecorder.server.rest;

import java.util.Map;

import org.restlet.Context;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.rest.RecordResource;
import de.inselhome.tvrecorder.common.utils.DateUtils;

import de.inselhome.tvrecorder.server.backend.Backend;
import de.inselhome.tvrecorder.server.utils.MPlayerCmdProducer;
import de.inselhome.tvrecorder.server.utils.StartAtJobCreator;
import de.inselhome.tvrecorder.server.utils.StopAtJobCreator;


/**
 * The concrete implementation of a {@link RecordResource}.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class RecordServerResource
extends      ServerResource
implements   RecordResource
{
    /**
     * The relative path to this recource.
     */
    public static final String PATH = "/record";

    /**
     * The Logger.
     */
    private static Logger logger = Logger.getLogger(RecordResource.class);


    /**
     * This method is meant to add new jobs for recordings. The incoming job is
     * validated in {@link isJobValid(Job)}. If the validation passes, a new
     * <i>at</i> job is created and the <i>job</i> is stored in the database.
     *
     * @param job The incoming job to record.
     */
    @Post
    public void add(Job job) {
        logger.info("/record - add()");

        if (job == null) {
            logger.warn("Job to record is null!");
            throw new IllegalArgumentException("Could not create job - missing information.");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("=================== New record ==================");
            logger.debug("= Channel: " + job.getChannel().getDescription());
            logger.debug("= Start: " + job.getStart());
            logger.debug("= End: " + job.getEnd());
            logger.debug("= Out name: " + job.getName());
            logger.debug("=================================================");
        }

        if (!isJobValid(job)) {
            String msg = "The job is not valid!";
            logger.error(msg);

            throw new IllegalArgumentException(msg);
        }

        boolean success = new StartAtJobCreator(job, new MPlayerCmdProducer()).startJob();

        if (!success) {
            return;
        }

        Context context = getContext();
        Map attr        = context.getAttributes();
        Backend backend = (Backend) attr.get("backend");

        backend.insertJob(job);

        new StopAtJobCreator(job).startJob();
    }


    /**
     * This method validates the retrieved {@link Job}. The start date needs to
     * be smaller than the end date - this is verified by {@link
     * DateUtils.isEndGreaterThanStart(Date,Date)} - and the channel needs to
     * be available in the server - verified by {@link isChannelValid(Channel)}.
     *
     * @param job The retrieved {@link Job}.
     *
     * @return true, if every check was ok, otherwise false.
     */
    public boolean isJobValid(Job job) {
        return (DateUtils.isEndGreaterThanStart(job.getEnd(), job.getStart()) &&
                isChannelValid(job.getChannel()));
    }


    /**
     * This method validates the retrieved {@link Channel}. If <i>channel</i>
     * is not available in the server, we are not able to record the job, which
     * means that this method returns <i>false</i>. Otherwise - if the channel
     * is available in the server, this method will return <i>true</i>.
     *
     * @param channel The channel retrieved in this resource.
     *
     * @return true, if <i>channel</i> is valid, otherwise false.
     */
    protected boolean isChannelValid(Channel channel) {
        Context context = getContext();
        Map     attr    = context.getAttributes();

        Channel[] ch = (Channel[]) attr.get(TvRecorderServer.CHANNELS_KEY);

        if (ch == null) {
            return false;
        }

        String key = channel.getKey();
        for (Channel c: ch) {
            if (key.equals(c.getKey())) {
                return true;
            }
        }

        return false;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
