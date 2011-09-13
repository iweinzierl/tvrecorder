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
package de.inselhome.tvrecorder.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import de.inselhome.tvrecorder.client.activities.setup.TvRecorderSettings;
import de.inselhome.tvrecorder.client.activities.tvguide.TvGuide;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class MainMenu extends Activity {

    public static final String TAG = "TvR [MainMenu]";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.menu);

        View tvrecorderBtn = findViewById(R.id.tvrecorder);
        tvrecorderBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                startActivity(
                    new Intent(MainMenu.this, TvRecorder.class));
            }
        });

        View tvguideBtn = findViewById(R.id.tvguide);
        tvguideBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                startActivity(
                    new Intent(MainMenu.this, TvGuide.class));
            }
        });

        View settingsBtn = findViewById(R.id.settings);
        settingsBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                startActivity(
                    new Intent(MainMenu.this, TvRecorderSettings.class));
            }
        });
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
