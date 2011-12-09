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
package de.inselhome.tvrecorder.client.android.activities.setup;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;

import de.inselhome.tvrecorder.client.android.Config;
import de.inselhome.tvrecorder.client.android.R;
import de.inselhome.tvrecorder.client.android.services.TvGuideUpdateService;


/**
 * This {@link PreferenceActivity} is used to create and show a ui that allows
 * the user to edit preferences used in this TvRecorder application.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvRecorderSettings
extends      PreferenceActivity
{
    /**
     * This method creates the user interface. Each preference defined in
     * <code>R.array.tvrecorder_settings</code> is displayed by a {@link
     * KeyValueItem}.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TvR [TvRecorderSettings]", "onCreate()");
        addPreferencesFromResource(R.xml.preferences);

        Preference autoUpdate = (Preference)
            findPreference(Config.SETTINGS_TVGUIDE_AUTO_UPDATE);

        autoUpdate.setOnPreferenceChangeListener(
            new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference pref, Object val) {
                    String key = pref.getKey();

                    if (key.equals(Config.SETTINGS_TVGUIDE_AUTO_UPDATE)) {
                        if (Boolean.valueOf(String.valueOf(val))) {
                            restartTvGuideUpdateService();
                        }
                    }

                    return true;
                }
        });
    }


    protected void restartTvGuideUpdateService() {
        Log.i("TvR [TvRecorderSettings]", "Start TvGuideUpdateService.");

        Intent service = new Intent(this, TvGuideUpdateService.class);
        startService(service);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
