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
package de.inselhome.tvrecorder.client;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.restlet.resource.ClientResource;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.rest.ChannelsResource;

import de.inselhome.tvrecorder.client.ui.AddJobForm;
import de.inselhome.tvrecorder.client.activities.addjob.OnChannelsUpdatedListener;
import de.inselhome.tvrecorder.client.activities.setup.TvRecorderSettings;
import de.inselhome.tvrecorder.client.activities.tvguide.TvGuide;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvRecorder extends Activity implements OnChannelsUpdatedListener {

    /**
     * The {@link AddJobForm} that allows adding new jobs.
     */
    protected AddJobForm addJobForm;

    protected List<OnChannelsUpdatedListener> listeners;

    protected ProgressDialog dialog;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TvR [TvRecorderActivity]", "onCreate() - create view");

        listeners = new ArrayList<OnChannelsUpdatedListener>();

        if (!Config.checkPreferences(this)) {
            startActivity(new Intent(this, TvRecorderSettings.class));
        }

        addJobForm = new AddJobForm(this);
        setContentView(addJobForm);

        addOnChannelsUpdatedListener(this);
        addOnChannelsUpdatedListener(addJobForm);

        updateChannels();
    }


    public void addOnChannelsUpdatedListener(OnChannelsUpdatedListener l) {
        if (l != null) {
            listeners.add(l);
        }
    }


    protected void fireOnChannelsUpdated(Channel[] channels) {
        for (OnChannelsUpdatedListener l : listeners) {
            Log.d("TvR [TvRecorder]", "Listener: " + l);
            l.onChannelsUpdated(channels);
        }
    }


    protected void updateChannels() {
        Resources resources = getResources();

        dialog = ProgressDialog.show(
            this,
            resources.getString(R.string.addjob_load_progress_title),
            resources.getString(R.string.addjob_load_progress_text),
            true);

        Log.d("TvR [TvRecorder]", "updateChannels()");

        final TvRecorder tvrecorder = this;

        new AsyncTask<Void, Void, Channel[]>() {
            protected Channel[] doInBackground(Void... v1) {
                ClientResource cr = Config.getClientResource(
                    tvrecorder, ChannelsResource.PATH);

                ChannelsResource resource = cr.wrap(ChannelsResource.class);
                return resource.retrieve();
            }

            protected void onPostExecute(Channel[] channels) {
                Log.d("TvR [TvRecorder]", "HTTP request finished.");
                fireOnChannelsUpdated(channels);
            }
        }.execute();
    }


    public void onChannelsUpdated(Channel[] channels) {
        dialog.dismiss();
        Log.i("TvR [TvRecorder]", "Found " + channels.length + " channels");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("TvR [TvRecorder]", " - onCreateOptionsMenu()");
        menu.add(R.string.tvrecorder_contextmenu_quit);
        menu.add(R.string.tvrecorder_contextmenu_settings);
        menu.add(R.string.tvrecorder_contextmenu_tvguide);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();

        Log.d(
            "TvR [TvRecorder]",
            "onOptionsItemSelected(): selected '" + title + "'");

        Resources res = getResources();

        String quit  = res.getString(R.string.tvrecorder_contextmenu_quit);
        String sett  = res.getString(R.string.tvrecorder_contextmenu_settings);
        String guide = res.getString(R.string.tvrecorder_contextmenu_tvguide);
        Log.d("TvR [TvRecorder]", "onOptionsItemSelected(): settings = " + sett);

        if (title.equals(quit)) {
            finish();
            return true;
        }
        else if (title.equals(sett)) {
            Log.d("TvR [TvRecorder]", "onOptionsItemSelected(): goto settings");
            startActivity(new Intent(this, TvRecorderSettings.class));
            return true;
        }
        else if (title.equals(guide)) {
            Log.d("TvR [TvRecorder]", "onOptionsItemSelected(): goto tv guide");
            startActivity(new Intent(this, TvGuide.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * This method retrieves the {@link AddJobForm}.
     *
     * @return the {@link AddJobForm}.
     */
    public AddJobForm getAddJobForm() {
        return addJobForm;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
