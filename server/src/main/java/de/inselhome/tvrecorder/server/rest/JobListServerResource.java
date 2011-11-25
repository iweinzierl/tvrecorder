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
package de.inselhome.tvrecorder.server.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import org.json.JSONArray;
import org.json.JSONException;

import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.rest.JobListResource;
import de.inselhome.tvrecorder.common.utils.JSONUtils;

import de.inselhome.tvrecorder.server.backend.Backend;
import de.inselhome.tvrecorder.server.utils.AtJobRemover;

/**
 * The concrete implementation of a {@link TvGuideResource}.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class JobListServerResource
extends      TvRecorderResource
implements   JobListResource
{
    private static final Logger logger =
        Logger.getLogger(JobListServerResource.class);


    /**
     * This method returns a JsonRepresentation of queued jobs or
     * an EmptyRepresentation if there are no queued jobs in the database.
     *
     * @return a JsonRepresentation with Jobs or an EmptyRepresentation.
     */
    @Override
    public Representation get() {
        logger.info(PATH + " - get()");

        Job[] jobs = getBackend().loadQueuedJobs(new Date());

        if (jobs == null || jobs.length == 0) {
            logger.debug("No jobs in queue found.");

            return new EmptyRepresentation();
        }

        logger.debug("Found " + jobs.length + " jobs in queue.");

        JSONArray arr = new JSONArray();

        for (Job job: orderJobs(jobs)) {
            arr.put(JSONUtils.toJSON(job));
        }

        return new StringRepresentation(arr.toString());
    }


    @Override
    public Representation post(Representation job) {
        logger.info(PATH + " - post()");

        List<Job> toRemove = null;

        try {
            String json = job.getText();
            toRemove = JSONUtils.jobsFromJSON(new JSONArray(json));

            logger.debug("Try to remove " + toRemove.size() + " jobs.");
        }
        catch (JSONException je) {
            logger.error("Error while parsing job: " + je.getMessage());
        }
        catch (IOException ioe) {
            logger.error("Error while parsing job: " + ioe.getMessage());
        }

        if (toRemove == null) {
            return new StringRepresentation("Error: no job.");
        }

        Backend backend = getBackend();

        List<Job> removed = new ArrayList<Job>();

        for (Job j: toRemove) {
            int[] atJobIds = backend.findAtJobIds(j);
            logger.info(
                "Try to remove at job: " + atJobIds[0] + " & " + atJobIds[1]);

            if (atJobIds[0] > 0 && atJobIds[1] > 0) {
                AtJobRemover.remove(atJobIds[0]);
                AtJobRemover.remove(atJobIds[1]);

                if (backend.removeJob(j) <= 0) {
                    logger.warn("Job was not removed!");
                    continue;
                }

                removed.add(j);
            }
        }

        return new StringRepresentation(JSONUtils.toJSON(removed).toString());
    }


    protected static Collection<Job> orderJobs(Job[] jobs) {
        TreeMap<Date, Job> sorted = new TreeMap<Date, Job>();

        for (Job job: jobs) {
            sorted.put(job.getStart(), job);
        }

        return sorted.values();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
