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
package de.inselhome.tvrecorder.server.rest;

import java.util.List;
import java.util.Map;

import org.restlet.Context;
import org.restlet.resource.ServerResource;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;

import de.inselhome.tvrecorder.server.backend.Backend;
import de.inselhome.tvrecorder.server.tvguide.TvShowManager;


/**
 * The concrete implementation of a {@link RecordResource}.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvRecorderResource
extends      ServerResource
{
    public Map getAttr() {
        Context context = getContext();
        return context.getAttributes();
    }


    public Backend getBackend() {
        Map attr = getAttr();

        return (Backend) attr.get("backend");
    }


    public Channel[] getChannels() {
        Map attr = getAttr();

        return (Channel[]) attr.get(TvRecorderServer.CHANNELS_KEY);
    }


    public List<ChannelWithTvGuide> getTvGuide() {
        Map attr = getAttr();

        TvShowManager manager = (TvShowManager)
            attr.get(TvRecorderServer.TVSHOW_MANAGER);

        return manager.getChannels();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
