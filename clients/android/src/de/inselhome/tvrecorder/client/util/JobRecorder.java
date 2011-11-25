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
package de.inselhome.tvrecorder.client.util;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONArray;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.rest.RecordResource;
import de.inselhome.tvrecorder.common.utils.JSONUtils;

import de.inselhome.tvrecorder.client.Config;
import de.inselhome.tvrecorder.client.R;


public class JobRecorder {

    private static final String TAG = "TvR [JobRecorder]";

    protected ProgressDialog progress;

    protected Context context;


    public JobRecorder(Context context) {
        this.context = context;
    }


    public void record(final Job toRecord) {
        List<Job> toRecordList = new ArrayList<Job>();
        toRecordList.add(toRecord);

        record(toRecordList);
    }


    /**
     * This method should be used to send new <i>Jobs</i>s to the TvRecorder
     * server. Before the <i>Job</i> in <i>toRecord</i> are sent to the server,
     * a ProgressDialog is displayed. The UI thread is not blocked during Http
     * request! After the request is finished, the ProgressDialog is removed and
     * the number of successfully recorded jobs is displayed. If there occured
     * an error (e.g. no internet connection is available), an AlertDialog is
     * displayed.
     *
     * @param toRecord The list of jobs that should be sent to TvRecorder
     * server.
     */
    public void record(final List<Job> toRecord) {
        beforeRecordShows();

        new AsyncTask<Void, Void, List<Job>>() {
            protected List<Job> doInBackground(Void... v) {
                ClientResource cr =
                    Config.getClientResource(context, RecordResource.PATH);

                try {
                    JSONArray json = JSONUtils.toJSON(toRecord);

                    Representation res =
                        cr.post(new StringRepresentation(json.toString()));

                    afterRecordShows();

                    return JSONUtils.jobsFromJSON(new JSONArray(res.getText()));
                }
                catch (Exception e) {
                    Log.e(TAG, "INTERNAL SERVER ERROR");
                }

                afterRecordShows();

                return null;
            }

            protected void onPostExecute(List<Job> jobs) {
                if (jobs == null) {
                    displayError(
                        "Error",
                        "Error while recording jobs.");

                    return;
                }

                displayInformation(toRecord, jobs);
            }
        }.execute();
    }


    protected void beforeRecordShows() {
        Resources res = context.getResources();
        progress = ProgressDialog.show(
            context,
            res.getString(R.string.jobrecorder_record_progress_title),
            res.getString(R.string.jobrecorder_record_progress_text),
            true);
    }


    protected void afterRecordShows() {
        if (progress != null) {
            progress.dismiss();
        }
    }


    protected void displayInformation(List<Job> toRecord, List<Job> recorded) {
        String info = "Successfully recorded " + recorded.size() + " / " + toRecord.size() + " jobs.";
        Toast popup = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        popup.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        popup.show();
    }


    protected void displayError(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(text);

        builder.show();
    }
}
