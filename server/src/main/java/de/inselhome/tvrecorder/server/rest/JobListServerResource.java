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

import java.util.Date;

import org.apache.log4j.Logger;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;

import org.json.JSONArray;

import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.rest.JobListResource;
import de.inselhome.tvrecorder.common.utils.JSONUtils;

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

        for (Job job: jobs) {
            arr.put(JSONUtils.toJSON(job));
        }

        return new JsonRepresentation(arr);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
