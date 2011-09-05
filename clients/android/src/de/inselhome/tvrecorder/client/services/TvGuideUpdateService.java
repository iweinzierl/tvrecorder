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
package de.inselhome.tvrecorder.client.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

import de.inselhome.tvrecorder.client.Config;
import de.inselhome.tvrecorder.client.activities.tvguide.TvGuideDataStore;


public class TvGuideUpdateService
extends      IntentService
implements   OnSharedPreferenceChangeListener
{
    protected int     updateIntervalHours;
    protected boolean active;


    public TvGuideUpdateService() {
        super("TvGuideUpdateService");
        active = true;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("TvR [TvGuideUpdateService]", "onHandleIntent");

        updateIntervalHours = Config.getPreferenceAsInteger(
            this, Config.SETTINGS_TVGUIDE_UPDATE_INTERVAL, -1);

        SharedPreferences prefs =
            PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        while(active) {
            Log.d("TvR [TvGuideUpdateService]", "UPDATE TvGuide");

            if (updateIntervalHours <= 0) {
                Log.e("TvR [TvGuideUpdateService]", "No interval given. End!");
                return;
            }

            try {
                TvGuideDataStore.get(this, true);
                Thread.sleep(updateIntervalHours * 1000 * 60 * 60);
            }
            catch (InterruptedException ie) {
                Log.w("TvR [TvGuideUpdateService]", ie);
            }
        }

        Log.i("TvR [TvGuideUpdateService]", "Service terminated.");
    }


    @Override
    public void onSharedPreferenceChanged(
        SharedPreferences pref, String key
    ) {
        if (key.equals(Config.SETTINGS_TVGUIDE_AUTO_UPDATE)) {
            boolean autoUpdate = pref.getBoolean(key, false);

            if (!autoUpdate) {
                active = false;
            }
        }
        else if (key.equals(Config.SETTINGS_TVGUIDE_UPDATE_INTERVAL)) {
            Log.d("TvR [TvGuideUpdateService]", "Change update interval.");
            updateIntervalHours = Integer.parseInt(pref.getString(key, "0"));
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
