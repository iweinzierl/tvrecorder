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

import java.util.List;

import org.json.JSONArray;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.utils.JSONUtils;


/**
 * The concrete implementation of a {@link TvGuideResource}.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvGuideServerResource
extends      TvRecorderResource
{
    /**
     * The relative path to this recource.
     */
    public static final String PATH = "/tvguide";

    /**
     * The Logger.
     */
    private static Logger logger = Logger.getLogger(TvGuideServerResource.class);



    @Override
    public Representation get() {
        logger.info(PATH + " - get()");

        List<ChannelWithTvGuide> tvguide = getTvGuide();

        int num = tvguide != null ? tvguide.size() : 0;
        logger.debug("Found " + num + " channels with TvShows.");

        JSONArray arr = new JSONArray();

        for (ChannelWithTvGuide channel: tvguide) {
            arr.put(JSONUtils.toJSON(channel));
        }

        String json = arr.toString();

        return new StringRepresentation(json);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
