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
package de.inselhome.tvrecorder.client.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import org.json.JSONArray;
import org.json.JSONException;

import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.rest.JobListResource;
import de.inselhome.tvrecorder.common.utils.JSONUtils;

import de.inselhome.tvrecorder.client.Config;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class JobProvider {

    private static final String TAG = "TvR [JobProvider]";


    protected static List<Job> getFromServer(Context context) {
        ClientResource cr = Config.getClientResource(
            context, JobListResource.PATH);

        try {
            Representation r = cr.get(MediaType.APPLICATION_JSON);

            Log.d(TAG, "Http request finished successfully.");

            String json = r.getText();

            if (json == null || json.length() == 0) {
                Log.d(TAG, "No queued jobs existing.");
                return new ArrayList<Job>();
            }

            return JSONUtils.jobsFromJSON(new JSONArray(json));
        }
        catch (JSONException je) {
            Log.e(TAG, "Error while parsing JSON jobs: " + je.getMessage());
        }
        catch (IOException ioe) {
            Log.e(TAG, "Error while parsing JSON jobs: " + ioe.getMessage());
        }

        return null;
    }


    protected static List<Job> removeFromServer(Context c, List<Job> jobs) {
        ClientResource cr = Config.getClientResource(c, JobListResource.PATH);

        Log.d(TAG, "Try to remove " + jobs.size() + " jobs.");

        JSONArray j = JSONUtils.toJSON(jobs);

        Representation r = cr.post(new StringRepresentation(j.toString()));
        Log.d(TAG, "Finished HTTP request.");

        try {
            return JSONUtils.jobsFromJSON(new JSONArray(r.getText()));
        }
        catch (JSONException je) {
            Log.e(TAG, "Error while parsing removed jobs: " + je.getMessage());
        }
        catch (IOException ioe) {
            Log.e(TAG, "Error while parsing removed jobs: " + ioe.getMessage());
        }

        return null;
    }


    public static List<Job> getJobs(Context context) {
        // XXX Should we store a list of jobs locally?
        return getFromServer(context);
    }


    public static List<Job> removeJobs(Context context, List<Job> jobs) {
        return removeFromServer(context, jobs);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
