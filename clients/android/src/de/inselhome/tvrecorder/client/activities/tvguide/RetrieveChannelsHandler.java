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

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import org.restlet.resource.ClientResource;

import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.rest.TvGuideResource;

import de.inselhome.tvrecorder.client.Config;


public class RetrieveChannelsHandler
implements   Runnable
{
    protected TvGuide tvGuide;

    protected List<TvGuideUpdateListener> listeners;


    public RetrieveChannelsHandler(TvGuide tvGuide) {
        this.tvGuide   = tvGuide;
        this.listeners = new ArrayList<TvGuideUpdateListener>();
    }


    public void addTvGuideUpdateListener(TvGuideUpdateListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }


    protected void fireTvGuideUpdateEvent(ChannelWithTvGuide[] channels) {
        TvGuideUpdateEvent event = new TvGuideUpdateEvent(channels);

        for (TvGuideUpdateListener listener: listeners) {
            listener.onTvGuideUpdate(event);
        }
    }


    public void run() {
        Log.d("TvR [RetrieveChannelsHandler]", "run() - retrieve channels");

        ClientResource cr = Config.getClientResource(
            tvGuide, TvGuideResource.PATH);

        TvGuideResource      resource = cr.wrap(TvGuideResource.class);

        try {
            ChannelWithTvGuide[] channels = resource.retrieve();
            fireTvGuideUpdateEvent(channels);
        }
        catch (Exception e) {
            Log.e("TvR [RetrieveChannelsHandler]", e.getMessage());
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
