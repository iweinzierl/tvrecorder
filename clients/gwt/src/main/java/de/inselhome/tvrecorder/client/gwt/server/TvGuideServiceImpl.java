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
package de.inselhome.tvrecorder.client.gwt.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.restlet.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.inselhome.tvrecorder.client.gwt.shared.model.Channel;

import de.inselhome.tvrecorder.client.gwt.shared.JSONHelper;

import de.inselhome.tvrecorder.client.gwt.client.services.TvGuideService;

import de.inselhome.tvrecorder.client.gwt.server.HttpClient.ResponseHandler;


public class TvGuideServiceImpl
extends      TvRecorderService
implements   TvGuideService
{
    private static Logger logger = Logger.getLogger(TvGuideServiceImpl.class);


    public List<Channel> getTvGuide() {
        logger.info("TvGuideServiceImpl.getTvGuide");

        HttpClient http = getClient("tvguide");

        String json = null;

        try {
            json = (String) http.get(new ResponseHandler() {
                @Override
                public Object handle(Response response)
                throws IOException
                {
                    return response.getEntity().getText();
                }
            });
        }
        catch (IOException ioe) {
            logger.error(ioe, ioe);
        }

        return parseResponse(json);
    }


    protected List<Channel> parseResponse(String json) {
        JSONArray array = new JSONArray();

        try {
            array = new JSONArray(json);
            return JSONHelper.tvGuideFromJSON(array);
        }
        catch (JSONException je) {
            logger.debug("String is no JSONArray.");
        }

        try {
            array.put(new JSONObject(json));
            return JSONHelper.tvGuideFromJSON(array);
        }
        catch (JSONException je) {
            logger.debug("String is no JSONObject.");
        }

        return new ArrayList<Channel>();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
