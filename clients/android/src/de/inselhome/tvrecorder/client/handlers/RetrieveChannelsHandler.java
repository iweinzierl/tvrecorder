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
package de.inselhome.tvrecorder.client.handlers;

import android.util.Log;

import org.restlet.resource.ClientResource;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.rest.ChannelsResource;

import de.inselhome.tvrecorder.client.Config;
import de.inselhome.tvrecorder.client.TvRecorder;


/**
 * This handler retrieves channels by a server and updates the channel list in
 * the {@link TvRecorder}.
 */
public class RetrieveChannelsHandler
implements   Runnable
{
    /**
     * The {@link TvRecorder}.
     */
    protected TvRecorder tvrecorder;


    /**
     * The default constructor to create a new {@link RetrieveChannelsHandler}
     * object.
     *
     * @param tvrecorder The {@link TvRecorder}.
     */
    public RetrieveChannelsHandler(TvRecorder tvrecorder) {
        this.tvrecorder = tvrecorder;
    }


    /**
     * This method retrieves channels of a remote server and updates the channel
     * list of {@link tvrecorder} with these channels.
     */
    public void run() {
        try {
            Log.d(
                "TvR [RetrieveChannelsHandler]",
                "run() - retrieve channels");

            ClientResource cr = Config.getClientResource(
                tvrecorder, ChannelsResource.PATH);

            ChannelsResource resource = cr.wrap(ChannelsResource.class);
            Channel[] channels = resource.retrieve();

            Log.i(
                "TvR [RetrieveChannelsHandler]",
                "run() - Found " + channels.length + " channels");

            tvrecorder.getAddJobForm().updateChannels(channels);
        }
        catch (Exception e) {
            Log.e(
                "TvR [RetrieveChannelsHandler]",
                "run() [Exception] - " + e.getMessage());
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
