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

import de.inselhome.tvrecorder.client.ui.AddJobForm;


public class TvRecorder
extends      Activity
{
    /**
     * The URL to the RecordResource of the server.
     */
    public static final String URL_SERVER_RECORD =
        "http://my-server/record";

    /**
     * The URL to the ChannelsResource of the server.
     */
    public static final String URL_SERVER_CHANNELS =
        "http://my-server/channels";


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
