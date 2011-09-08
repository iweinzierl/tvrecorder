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
package de.inselhome.tvrecorder.client.activities.tvguide;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import org.json.JSONArray;
import org.json.JSONException;

import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.rest.TvGuideResource;
import de.inselhome.tvrecorder.common.utils.JSONUtils;

import de.inselhome.tvrecorder.client.Config;
import de.inselhome.tvrecorder.client.database.ChannelSQLiteHelper;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvGuideDataStore {

    public static final String TAG = "TvR [TvGuideDataStore]";


    public static ChannelWithTvGuide[] get(Context context) {
        return get(context, false);
    }


    public static ChannelWithTvGuide[] get(Context context, boolean forceHttp) {
        ChannelSQLiteHelper db = new ChannelSQLiteHelper(context);

        int updateInterval = Config.getPreferenceAsInteger(
            context, Config.SETTINGS_TVGUIDE_UPDATE_INTERVAL, null);

        if (db.needsUpdate(updateInterval) || forceHttp) {
            Log.d(TAG,"Database needs update from server");
            return getFromServerAndUpdate(context, db);
        }
        else {
            List<ChannelWithTvGuide> tmps = db.getAll();
            Log.d(TAG, "Found " + tmps.size() + " channels in DB!");

            if (tmps != null && !tmps.isEmpty()) {
                return (ChannelWithTvGuide[])
                    tmps.toArray(new ChannelWithTvGuide[tmps.size()]);
            }
            else {
                return getFromServerAndUpdate(context, db);
            }
        }
    }


    protected static void updateDatabase(
        ChannelSQLiteHelper  db,
        ChannelWithTvGuide[] cs)
    {
        db.clean();

        for (ChannelWithTvGuide c: cs) {
            db.insert(c);
        }

        db.doIt();
    }


    protected static ChannelWithTvGuide[] getFromServerAndUpdate(
        Context             context,
        ChannelSQLiteHelper db)
    {
        ChannelWithTvGuide[] cs = getFromServer(context);

        if (cs == null || cs.length == 0) {
            Log.d(TAG, "Received no channels via Http from server.");
            return null;
        }

        Log.d(TAG, "Received " + cs.length + " channels via http from server.");
        updateDatabase(db, cs);

        return cs;
    }


    protected static ChannelWithTvGuide[] getFromServer(Context context) {
        ClientResource cr = Config.getClientResource(
            context, TvGuideResource.PATH);

        try {
            Representation repr = cr.get(MediaType.APPLICATION_JSON);

            Log.d(TAG, "HTTP request finished successfully.");

            String json = repr.getText();
            JSONArray a = new JSONArray(json);

            return JSONUtils.tvGuideFromJSON(a);
        }
        catch (ResourceException e) {
            Log.e(TAG, "No channels found: " + e.getMessage());
        }
        catch (IOException ioe) {
            Log.e(TAG, "Broken JSON representation: " + ioe.getMessage());
        }
        catch (JSONException je) {
            Log.e(TAG, "Error while parsing TvGuide JSON: " + je.getMessage());
        }

        return null;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
