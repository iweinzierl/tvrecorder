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

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;


/**
 * This class contains some static functions to access the configuration.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class Config {

    /**
     * This function retrieves the server url.
     *
     * @param context The context.
     *
     * @return the server url as string.
     */
    public static final String getServer(Context context) {
        Resources res = context.getResources();

        String url  = res.getString(R.string.server_url);
        String port = res.getString(R.string.server_port);

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
