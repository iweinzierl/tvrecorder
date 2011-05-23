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

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import de.inselhome.tvrecorder.client.ui.AddJobForm;
import de.inselhome.tvrecorder.client.activities.setup.TvRecorderSettings;
import de.inselhome.tvrecorder.client.activities.tvguide.TvGuide;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvRecorder
extends      Activity
{
    /**
     * The {@link AddJobForm} that allows adding new jobs.
     */
    protected AddJobForm addJobForm;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TvR [TvRecorderActivity]", "onCreate() - create view");

        if (!Config.checkPreferences(this)) {
            startActivity(new Intent(this, TvRecorderSettings.class));
        }

        addJobForm = new AddJobForm(this);
        setContentView(addJobForm);

        addJobForm.refresh();
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
