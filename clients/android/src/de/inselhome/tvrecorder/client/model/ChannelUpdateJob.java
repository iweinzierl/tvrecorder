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
package de.inselhome.tvrecorder.client.android.model;

import java.io.Serializable;
import java.net.URL;

import de.inselhome.tvrecorder.common.objects.Channel;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class ChannelUpdateJob implements Serializable {

    protected Channel channel;

    protected URL url;


    public ChannelUpdateJob(Channel channel, URL url) {
        this.channel = channel;
        this.url     = url;
    }


    public Channel getChannel() {
        return channel;
    }


    public URL getURL() {
        return url;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
