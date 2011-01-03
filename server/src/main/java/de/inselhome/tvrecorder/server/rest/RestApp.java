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

import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import de.inselhome.tvrecorder.server.backend.Backend;


/**
 * This class represents the actual HTTP server. The supported ServerResources
 * should be added in {@link createRoot}.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class RestApp
extends      Application
{
    /**
     * The logger.
     */
    private static final Logger logger = Logger.getLogger(RestApp.class);

    protected Backend backend;


    /**
     * Creates a new {@link RestApp}.
     */
    public RestApp(Backend backend) {
        this.backend = backend;
    }


    /**
     * In this method the ServerResources are added.
     *
     * @return a {@link Router}.
     */
    @Override
    public Restlet createRoot() {

        Context context = getContext();

        ConcurrentMap map = context.getAttributes();
        map.put("backend", backend);

        Router router = new Router(context);

        logger.info(
            "Add service ChannelsResource: " + ChannelsServerResource.PATH);
        router.attach(ChannelsServerResource.PATH, ChannelsServerResource.class);

        logger.info(
            "Add service RecordResource: " + RecordServerResource.PATH);
        router.attach(RecordServerResource.PATH, RecordServerResource.class);

        return router;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
