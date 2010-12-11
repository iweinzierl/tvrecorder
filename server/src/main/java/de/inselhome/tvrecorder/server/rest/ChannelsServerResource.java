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

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.rest.ChannelsResource;


/**
 * The concrete implementation of {@link ChannelsResource}.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class ChannelsServerResource
extends      ServerResource
implements   ChannelsResource
{
    /**
     * The relative path to this resource.
     */
    public static final String PATH = "/channels";

    /**
     * The logger.
     */
    private static Logger logger = Logger.getLogger(ChannelsResource.class);


    /**
     * This method currently just returns a static list of channels. In the
     * future, this method should retrieve the content of a channels
     * configuration file.
     *
     * @return a list of Channels.
     */
    @Get
    public Channel[] retrieve() {
        logger.info("/channels - retrieve()");
        Channel[] channels = new Channel[5];
        channels[0]        = new Channel("ard", "ARD");
        channels[1]        = new Channel("zdf", "ZDF");
        channels[2]        = new Channel("rtl", "RTL");
        channels[3]        = new Channel("sat1", "SAT.1");
        channels[4]        = new Channel("pro7", "Pro 7");

        return channels;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
