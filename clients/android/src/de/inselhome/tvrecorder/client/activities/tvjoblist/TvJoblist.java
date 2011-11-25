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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
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


    public static final int CONTEXT_REMOVE_SINGLE = 1;
    public static final int CONTEXT_REMOVE_ALL    = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        setContentView(R.layout.tvjoblist);

        joblist = (ListView) findViewById(R.id.joblist);

        registerForContextMenu(joblist);

        updateJobs();
    }


    @Override
    public void onCreateContextMenu(ContextMenu m, View v, ContextMenuInfo mi) {
        super.onCreateContextMenu(m, v, mi);

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) mi;

        Job job = (Job) joblist.getAdapter().getItem(info.position);

        m.setHeaderTitle(job.getName());
        m.add(
            0,
            CONTEXT_REMOVE_SINGLE,
            0,
            R.string.tvjoblist_context_remove_single);

        m.add(
            0,
            CONTEXT_REMOVE_ALL,
            0,
            R.string.tvjoblist_context_remove_all);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == CONTEXT_REMOVE_SINGLE) {
            AdapterContextMenuInfo info =
                (AdapterContextMenuInfo) item.getMenuInfo();

            Job job = (Job) joblist.getAdapter().getItem(info.position);

            List<Job> toRemove = new ArrayList<Job>();
            toRemove.add(job);

            removeJobs(toRemove);

            return true;
        }
        else if (id == CONTEXT_REMOVE_ALL) {
            TvJoblistAdapter a = (TvJoblistAdapter) joblist.getAdapter();
            removeJobs(a.getJobs());
        }

        return super.onContextItemSelected(item);
    }


    protected void updateJobs() {
        beforeUpdateJobs();

        new AsyncTask<Void, Void, List<Job>>() {
            protected List<Job> doInBackground(Void... v) {
                try {
                    return JobProvider.getJobs(TvJoblist.this);
                }
                catch (Exception e) {
                    Log.e(TAG, "INTERNAL SERVER ERROR");
                    afterUpdateJobs();
                }

                return null;
            }

            protected void onPostExecute(List<Job> jobs) {
                Log.d(TAG, "HTTP request finished.");

                if (jobs != null && jobs.size() > 0) {
                    displayJobs(jobs);
                }
                else if (jobs != null && jobs.size() == 0) {
                    displayInformation(
                        R.string.tvjoblist_no_jobs_title,
                        R.string.tvjoblist_no_jobs_text
                    );
                }
                else {
                    displayInformation(
                        R.string.tvjoblist_load_failed_title,
                        R.string.tvjoblist_load_failed_text
                    );
                }

                afterUpdateJobs();
            }
        }.execute();
    }


    protected void removeJobs(final List<Job> toRemove) {
        // TODO ADD PROGRESS DIALOG

        new AsyncTask<Void, Void, List<Job>>() {
            protected List<Job> doInBackground(Void... v) {
                try {
                    return JobProvider.removeJobs(TvJoblist.this, toRemove);
                }
                catch (Exception e) {
                    Log.e(TAG, "INTERNAL SERVER ERROR");
                    // TODO REMOVE PROGRESS DIALOG
                }

                return null;
            }

            protected void onPostExecute(List<Job> jobs) {
                Log.d(TAG, "HTTP request finished.");
                TvJoblistAdapter a = (TvJoblistAdapter) joblist.getAdapter();
                a.removeJobs(jobs);

                // TODO DISPLAY REMOVED JOBS
                // TODO REMOVE PROGRESS DIALOG
            }
        }.execute();
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
        joblist.setAdapter(new TvJoblistAdapter(
            this,
            R.layout.tvjob_list,
            jobs));
    }


    protected void displayInformation(int titleId, int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TvJoblist.this);
        builder.setTitle(titleId);
        builder.setMessage(messageId);

        builder.show();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
