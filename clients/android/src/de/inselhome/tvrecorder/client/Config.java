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

import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;

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

    public static final String SETTINGS_AUTH_USER   = "settings_auth_user";

    public static final String SETTINGS_AUTH_PASS   = "settings_auth_pass";

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
            if (setting.indexOf("auth") >= 0) {
                continue;
            }

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
     * This method returns the login name that has been set in the settings
     * activity for user authentication.
     *
     * @param context The Context.
     *
     * @return the login name or null.
     */
    protected static final String getLogin(Context context) {
        String login = getPreference(context, SETTINGS_AUTH_USER, null);

        return login != null && !login.equals("") ? login : null;
    }


    /**
     * This method returns the password that has been set in the settings
     * activity for user authentication.
     *
     * @param context The Context.
     *
     * @return the configured password or null.
     */
    protected static final String getPassword(Context context) {
        String pass = getPreference(context, SETTINGS_AUTH_PASS, null);
        return pass != null && !pass.equals("") ? pass : null;
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


    public static final ClientResource getClientResource(
        Context context, String res)
    {
        String url   = getServerResource(context, res);
        String login = getLogin(context);
        String pass  = getPassword(context);

        ClientResource cr = new ClientResource(url);

        if (login != null && pass != null) {
            Log.i(
                "TvR [Config]",
                "getClientResource() - prepare HTTP BasicAuth");
            cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, login, pass);
        }

        return cr;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
