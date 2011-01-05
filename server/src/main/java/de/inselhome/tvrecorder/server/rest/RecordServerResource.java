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

import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.rest.RecordResource;

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
     * This method is meant to add new jobs for recordings.
     * TODO This method is currently not implemented!
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
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
