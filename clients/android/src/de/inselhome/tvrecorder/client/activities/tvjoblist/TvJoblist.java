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
package de.inselhome.tvrecorder.client.activities.tvjoblist;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import de.inselhome.tvrecorder.client.R;
import de.inselhome.tvrecorder.client.data.JobProvider;

import de.inselhome.tvrecorder.common.objects.Job;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvJoblist
extends      Activity
{
    private static final String TAG = "TvR [TvJoblist]";

    protected ListView joblist;

    protected ProgressDialog progress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        setContentView(R.layout.tvjoblist);

        joblist = (ListView) findViewById(R.id.joblist);

        updateJobs();
    }


    protected void updateJobs() {
        beforeUpdateJobs();

        try {
            new AsyncTask<Void, Void, List<Job>>() {
                protected List<Job> doInBackground(Void... v) {
                    return JobProvider.getJobs(TvJoblist.this);
                }

                protected void onPostExecute(List<Job> jobs) {
                    Log.d(TAG, "HTTP request finished.");
                    displayJobs(jobs);
                    afterUpdateJobs();
                }
            }.execute();
        }
        catch (Exception e) {
            Log.e(TAG, "INTERNAL SERVER ERROR");
            afterUpdateJobs();
        }
    }


    protected void beforeUpdateJobs() {
        Resources res = getResources();
        progress = ProgressDialog.show(
            this,
            res.getString(R.string.tvjoblist_load_progress_title),
            res.getString(R.string.tvjoblist_load_progress_text),
            true);
    }


    protected void afterUpdateJobs() {
        if (progress != null) {
            progress.dismiss();
        }
    }


    protected void displayJobs(List<Job> jobs) {
        Job[] data = jobs != null
            ? (Job[]) jobs.toArray(new Job[jobs.size()])
            : new Job[0];

        joblist.setAdapter(new TvJoblistAdapter(
            this,
            R.layout.tvjob_list,
            data));
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
