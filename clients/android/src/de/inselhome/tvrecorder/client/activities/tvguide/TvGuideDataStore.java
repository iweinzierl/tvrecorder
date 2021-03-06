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
package de.inselhome.tvrecorder.client.android.activities.tvguide;

import java.io.IOException;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import de.inselhome.tvrecorder.client.android.Config;
import de.inselhome.tvrecorder.client.android.R;
import de.inselhome.tvrecorder.client.android.database.ChannelSQLiteHelper;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvGuideDataStore {

    public static final String TAG = "TvR [TvGuideDataStore]";


    public static ChannelWithTvGuide[] get(Context context) {
        return get(context, false);
    }


    public static ChannelWithTvGuide[] get(Context context, boolean forceHttp) {
        return get(context, forceHttp, false);
    }


    public static ChannelWithTvGuide[] get(
        Context   context,
        boolean   forceHttp,
        boolean   allowHttp
    ) {
        ChannelSQLiteHelper db = new ChannelSQLiteHelper(context);

        int updateInterval = Config.getPreferenceAsInteger(
            context, Config.SETTINGS_TVGUIDE_UPDATE_INTERVAL, -1);

        if (allowHttp && (db.needsUpdate(updateInterval) || forceHttp)) {
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
            else if (allowHttp) {
                return getFromServerAndUpdate(context, db);
            }
            else {
                return new ChannelWithTvGuide[0];
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

        notify(context, cs);

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

            if (repr == null) {
                Log.i(TAG, "Empty response received!");
                return new ChannelWithTvGuide[0];
            }

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


    protected static void notify(Context context, ChannelWithTvGuide[] channels) {
        NotificationManager mNotificationManager = (NotificationManager)
            context.getSystemService(Context.NOTIFICATION_SERVICE);

        Resources r = context.getResources();

        String ticker = r.getString(R.string.tvguide_notification_ticker);
        String title  = r.getString(R.string.tvguide_notification_title);
        String text   = r.getString(R.string.tvguide_notification_text);

        text = text.replace("$CHANNELS", String.valueOf(channels.length));
        text = text.replace("$SHOWS", String.valueOf(numShows(channels)));

        int icon  = R.drawable.icon;
        long when = System.currentTimeMillis();

        Notification   notification = new Notification(icon, ticker, when);
        Intent   notificationIntent = new Intent(context, TvGuide.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, 0);

        notification.setLatestEventInfo(context, title, text, contentIntent);

        mNotificationManager.notify(1, notification);
    }


    public static int numShows(ChannelWithTvGuide[] channels) {
        int num = 0;

        for (ChannelWithTvGuide channel: channels) {
            num += channel.getSortedListing().size();
        }

        return num;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
