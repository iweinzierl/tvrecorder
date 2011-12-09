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
package de.inselhome.tvrecorder.client.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import de.inselhome.tvrecorder.client.android.Config;
import de.inselhome.tvrecorder.client.android.activities.tvguide.TvGuideDataStore;
import de.inselhome.tvrecorder.client.android.database.ChannelSQLiteHelper;


public class TvGuideUpdateReceiver extends BroadcastReceiver {

    private static final String TAG = "TvR [TvGuideUpdateReceiver]";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TvR [TvGuideUpdateReceiver]", "Received broadcast intent.");

        NetworkInfo netInfo = (NetworkInfo)
            intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

        if (netInfo == null) {
            Log.d(TAG, "No NetworkInfo retrieved.");
            return;
        }

        if (!netInfo.isAvailable()) {
            Log.d(TAG, "No network connection available.");
            return;
        }

        Log.d(TAG, "Type:" + netInfo.getType());
        Log.d(TAG, "Type name: " + netInfo.getTypeName());
        Log.d(TAG, "Network available: " + netInfo.isAvailable());
        Log.d(TAG, "Network connected: " + netInfo.isConnected());
        Log.d(TAG, "Roaming: " + netInfo.isRoaming());


        if (doUpdate(context, netInfo)) {
            TvGuideDataStore.get(context, true, true);
        }
        else {
            Log.d(TAG, "No Update necessary/possible.");
        }
    }


    /**
     * Determines if this instance should update the TvGuide. True means, that
     * an update is desired. The return value depends on the current
     * configuration plus the state of the network connection.
     *
     * @param context The Context object received from broadcast.
     * @param info The NetworkInfo object.
     *
     * @return true, if the update is desired.
     */
    protected boolean doUpdate(Context context, NetworkInfo info) {
        boolean autoUpdate = Config.getPreferenceAsBool(
            context, Config.SETTINGS_TVGUIDE_AUTO_UPDATE, false);

        boolean onlyWifiUpdate = Config.getPreferenceAsBool(
            context, Config.SETTINGS_TVGUIDE_WIFI_UPDATE, false);

        boolean roamingAllowed = Config.getPreferenceAsBool(
            context, Config.SETTINGS_TVGUIDE_ROAMING, false);

        int updateInterval = Config.getPreferenceAsInteger(
            context, Config.SETTINGS_TVGUIDE_UPDATE_INTERVAL, -1);

        Log.d(TAG, "Setting: auto update = " + autoUpdate);
        Log.d(TAG, "Setting: wifi required = " + onlyWifiUpdate);
        Log.d(TAG, "Setting: roaming allowed = " + roamingAllowed);

        if (!autoUpdate) {
            return false;
        }

        int type = info.getType();

        if (onlyWifiUpdate && type == ConnectivityManager.TYPE_MOBILE) {
            return false;
        }

        if (!roamingAllowed && info.isRoaming()) {
            return false;
        }

        ChannelSQLiteHelper helper = new ChannelSQLiteHelper(context);
        return helper.needsUpdate(updateInterval);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
