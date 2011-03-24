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
package de.inselhome.tvrecorder.client.activities.setup;

import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import de.inselhome.tvrecorder.client.R;
import de.inselhome.tvrecorder.client.ui.KeyValueItem;
import de.inselhome.tvrecorder.client.ui.TextDialog;


/**
 * This {@link Activity} is used to create and show a ui that allows the user to
 * edit preferences used in this TvRecorder application.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvRecorderSettings
extends      Activity
{
    SharedPreferences preferences;

    /**
     * This method creates the user interface. Each preference defined in
     * <code>R.array.tvrecorder_settings</code> is displayed by a {@link
     * KeyValueItem}.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TvR [TvRecorderSettings]", "onCreate()");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Resources res      = getResources();
        String[]  strings  = res.getStringArray(R.array.tvrecorder_settings);
        Map       settings = preferences.getAll();

        for (String key: strings) {
            String value = (String) settings.get(key);

            int id = res.getIdentifier(
                key, "string", "de.inselhome.tvrecorder.client");

            String label      = res.getString(id);
            KeyValueItem item = new KeyValueItem(this, label, value);

            item.setOnClickListener(
                new SettingsOnClickListener(item, key, value, label));

            layout.addView(item);
        }

        setContentView(layout);
    }


    /**
     * This listener is used as callback for saving a preference after the user
     * has edited it.
     *
     * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
     */
    private class SettingsOnClickListener implements View.OnClickListener {
        protected KeyValueItem item;
        protected String       label;
        protected String       value;
        protected String       setting;

        /**
         * The default constructor.
         *
         * @param item The {@link KeyValueItem} that needs to be updated after
         * editing the preference.
         * @param setting The key that specifies the preference.
         * @param value The current value of the preference.
         * @param label The title for the {@link TextDialog}.
         */
        public SettingsOnClickListener(
            KeyValueItem item, String setting, String value, String label)
        {
            this.item    = item;
            this.setting = setting;
            this.value   = value;
            this.label   = label;
        }

        /**
         * If this method is called, a {@link TextDialog} is shown to edit the
         * preference specified by {@link setting}. The current value is
         * displayed in the text field.
         *
         * @param view the view.
         */
        public void onClick(View view) {
            TextDialog d = new TextDialog(
                TvRecorderSettings.this,
                label,
                value != null ? value : "n/a");

            d.setOnOkListener(new TextDialog.OnOkListener() {
                public void onOk(String text) {
                    Log.d(
                        "TvR [TvRecorderSettings]",
                        "save settings for '" + setting + "': " + text);

                    Editor edit = preferences.edit();
                    edit.putString(setting, text);
                    edit.commit();

                    item.setValue(text);
                }
            });
            d.show();
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
