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

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * This class contains some static functions to access the configuration.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class Config {

    public static final String SETTINGS_SERVER_URL =
        "prefs_server_url";

    public static final String SETTINGS_SERVER_PORT =
        "prefs_server_port";

    public static final String SETTINGS_AUTH =
        "prefs_server_authentication";

    public static final String SETTINGS_AUTH_USER =
        "prefs_server_user";

    public static final String SETTINGS_AUTH_PASS =
        "prefs_server_passwd";

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
    protected static Object getPreference(Context c, String key, String def) {
        SharedPreferences prefs =
            PreferenceManager.getDefaultSharedPreferences(c);

        Map settings = prefs.getAll();

        Object pref = settings.get(key);

        return pref != null ? pref : def;
    }


    protected static boolean getPreferenceAsBool(
        Context c, String key, boolean def
    ) {
        Object pref = getPreference(c, key, null);

        return pref != null ? (Boolean) pref : def;
    }


    /**
     * This function retrieves the server url.
     *
     * @param context The context.
     *
     * @return the server url as string.
     */
    public static final String getServer(Context context) {
        String url  = (String) getPreference(context, SETTINGS_SERVER_URL, "");
        String port = (String) getPreference(context, SETTINGS_SERVER_PORT, "");

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
        String login = (String)getPreference(context, SETTINGS_AUTH_USER, null);

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
        String pass = (String) getPreference(context, SETTINGS_AUTH_PASS, null);
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


    public static final Locale getLocale(Context context) {
        return Locale.GERMAN;
    }


    public static final TimeZone getTimeZone(Context context) {
        return TimeZone.getTimeZone("Berlin");
    }


    public static final ClientResource getClientResource(
        Context context, String res)
    {
        String url = getServerResource(context, res);

        ClientResource cr = new ClientResource(url);

        if (getPreferenceAsBool(context, SETTINGS_AUTH, false)) {
            String login = getLogin(context);
            String pass  = getPassword(context);


            Log.i(
                "TvR [Config]",
                "getClientResource() - prepare HTTP BasicAuth");
            cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, login, pass);
        }

        return cr;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
