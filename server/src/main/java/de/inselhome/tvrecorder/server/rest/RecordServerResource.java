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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.utils.DateUtils;
import de.inselhome.tvrecorder.common.utils.JSONUtils;

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
extends      TvRecorderResource
{
    /**
     * The relative path to this recource.
     */
    public static final String PATH = "/record";

    /**
     * The Logger.
     */
    private static Logger logger = Logger.getLogger(RecordServerResource.class);


    /**
     * This method is meant to add new jobs for recordings. The incoming job is
     * validated in {@link isJobValid(Job)}. If the validation passes, a new
     * <i>at</i> job is created and the <i>job</i> is stored in the database.
     *
     * @param job The incoming job to record.
     */
    @Override
    public Representation post(Representation job) {
        logger.info("/record - post()");

        List<Job> success = new ArrayList<Job>();

        try {
            JSONArray items = readJobItems(job.getText());
            int    numItems = items != null ? items.length() : 0;

            logger.debug("Found " + numItems + " items for recording.");

            for (int i = 0; i < numItems; i++) {
                JSONObject json = items.getJSONObject(i);

                String name  = json.getString("name");
                String chan  = json.getString("channel");
                Date   start = new Date(json.getLong("start"));
                Date   end   = new Date(json.getLong("end"));

                Job j = new Job(start, end, new Channel(chan, chan), name);
                if (doRecord(j)) {
                    success.add(j);
                }
            }

            String response = JSONUtils.toJSON(success).toString();
            return new StringRepresentation(response);
        }
        catch (IOException ioe) {
            logger.error("Broken JSON representation: " + ioe.getMessage());
        }
        catch (JSONException je) {
            logger.error("Error while parsing JSON Job: " + je.getMessage());
        }

        return new StringRepresentation("ERROR");
    }


    /**
     * Reads job items from <i>json</i> string. The string might be one of
     * JSONArray (for multiple job items) or JSONObject (for a single job item).
     *
     * @param json A JSON string.
     *
     * @return an array of Job items.
     */
    protected JSONArray readJobItems(String json) {
        JSONArray array = new JSONArray();

        try {
            array = new JSONArray(json);
            return array;
        }
        catch (JSONException je) {
            logger.debug("String is no JSONArray.");
        }

        try {
            array.put(new JSONObject(json));
            return array;
        }
        catch (JSONException je) {
            logger.debug("String is no JSONObject.");
        }

        logger.warn("Unable to read Job items from string.");

        return array;
    }


    protected boolean doRecord(Job job) {
        if (job == null) {
            logger.warn("Job to record is null!");
            return false;
        }

        prepareJob(job);

        if (logger.isDebugEnabled()) {
            logger.debug("=================== New record ==================");
            logger.debug("= Channel: " + job.getChannel().getDescription());
            logger.debug("= Start: " + job.getStart());
            logger.debug("= End: " + job.getEnd());
            logger.debug("= Out name: " + job.getName());
            logger.debug("=================================================");
        }

        if (!isJobValid(job)) {
            logger.error("The job '" + job.getName() + "' is not valid!");
            return false;
        }

        int jobId = new StartAtJobCreator(job, new MPlayerCmdProducer()).startJob();

        if (jobId < 0) {
            return false;
        }

        int endJobId = new StopAtJobCreator(job).startJob();

        Backend backend = getBackend();
        backend.insertJob(job, jobId, endJobId);

        return true;
    }


    protected void prepareJob(Job job) {
        logger.debug("Add 1 second to Job's start time.");

        Date start = job.getStart();
        start.setTime(start.getTime() + 1000);
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

        if (!DateUtils.isEndGreaterThanStart(job.getEnd(), job.getStart())) {
            logger.warn("Start time needs to be earlier than end time.");
            return false;
        }

        if (!isChannelValid(job.getChannel())) {
            logger.warn("Channel is not valid.");
            return false;
        }

        if (!isDateValid(job.getStart(), job.getEnd())) {
            logger.warn("Date is not valid.");
            return false;
        }

        return true;
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
        Channel[] ch = getChannels();

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


    /**
     *
     */
    protected boolean isDateValid(Date start, Date end) {
        Backend backend = getBackend();
        Job[]   queue   = backend.loadQueuedJobs(new Date());

        if (queue == null || queue.length == 0) {
            logger.debug("No queued jobs.");
            return true;
        }

        logger.debug("Found " + queue.length + " queued job(s).");

        for (Job job: queue) {
            if (logger.isDebugEnabled()) {
                logger.debug("  test: "
                    + DateUtils.format(job.getStart(), DateUtils.XMLTV_FORMAT)
                    + " - "
                    + DateUtils.format(job.getEnd(), DateUtils.XMLTV_FORMAT));
            }

            if (DateUtils.doesTimerangesCollide(
                job.getStart(), job.getEnd(), start, end)) {
                return false;
            }
        }

        return true;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
