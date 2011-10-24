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
package de.inselhome.tvrecorder.client.gwt.server;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.inselhome.tvrecorder.client.gwt.shared.TvGuideFaker;
import de.inselhome.tvrecorder.client.gwt.shared.model.Channel;

import de.inselhome.tvrecorder.client.gwt.client.services.TvGuideService;


public class TvGuideServiceImpl
extends      RemoteServiceServlet
implements   TvGuideService
{
    private static Logger logger = Logger.getLogger(TvGuideServiceImpl.class);


    public List<Channel> getTvGuide() {
        logger.info("TvGuideServiceImpl.getTvGuide");

        List<Channel> channels = TvGuideFaker.getChannels(true);

        int size = channels != null ? channels.size() : 0;

        logger.info("Found " + size + " channels.");

        return channels;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
