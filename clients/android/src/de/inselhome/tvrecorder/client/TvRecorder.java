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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import de.inselhome.tvrecorder.client.ui.AddJobForm;


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

        addJobForm = new AddJobForm(this);
        setContentView(addJobForm);

        addJobForm.refresh();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("TvR [TvRecorder]", " - onCreateOptionsMenu()");
        menu.add("Quit");
        menu.add("Settings");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();

        Log.d(
            "TvR [TvRecorder]",
            "onOptionsItemSelected(): selected '" + title + "'");

        if (title.equals("Quit")) {
            finish();
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
