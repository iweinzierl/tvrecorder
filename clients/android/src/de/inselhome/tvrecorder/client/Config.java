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

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * This class contains some static functions to access the configuration.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class Config {

    public static final String SETTINGS_SERVER_URL  = "settings_host_url";

    public static final String SETTINGS_SERVER_PORT = "settings_host_port";

    /**
     * This method retrieves the value of the preference specified by
     * <i>key</i>.
     *
     * @param c the {@link Context}
     * @param key the key that specifies the preference to return
     * @param def the default value if the preference specified by <i>key</i> is
     * not existing
     *
     * @return the preference value or <i>def</i>.
     */
    protected static String getPreference(Context c, String key, String def) {
        SharedPreferences prefs =
            PreferenceManager.getDefaultSharedPreferences(c);

        Map settings = prefs.getAll();

        String pref = (String) settings.get(key);

        return pref != null ? pref : def;
    }


    /**
     * This function is called to check if all necessary preferences are set.
     *
     * @param c the {@link Context}
     *
     * @return true, if all necessary preferences are set, otherwise false.
     */
    public static boolean checkPreferences(Context c) {
        Resources res     = c.getResources();
        String[] settings = res.getStringArray(R.array.tvrecorder_settings);

        for (String setting: settings) {
            String pref = getPreference(c, setting, null);

            if (pref == null || pref.equals("")) {
                return false;
            }
        }

        return true;
    }

    /**
     * This function retrieves the server url.
     *
     * @param context The context.
     *
     * @return the server url as string.
     */
    public static final String getServer(Context context) {
        String url  = getPreference(context, SETTINGS_SERVER_URL, "");
        String port = getPreference(context, SETTINGS_SERVER_PORT, "");

        return url + ":" + port;
    }


    /**
     * This function retrieves the url for a specific ServerResource <i>res</i>.
     *
     * @param context The context.
     * @param res The ServerResource path.
     *
     * @return the url to a specific ServerResource.
     */
    public static final String getServerResource(Context context, String res) {
        return getServer(context) + res;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
