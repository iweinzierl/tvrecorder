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

import org.apache.log4j.Logger;

import org.restlet.Component;
import org.restlet.data.Protocol;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvRecorderServer {

    /**
     * The default port of the REST server: 8282
     */
    public static final int DEFAULT_PORT = 8282;


    private static final Logger logger =
        Logger.getLogger(TvRecorderServer.class);


    public TvRecorderServer() {
    }


    /**
     * Create REST components and start HTTP server.
     */
    public void start() {
        logger.debug("Create necessary components for HTTP server.");

        RestApp app = new RestApp();

        Component component = new Component();

        component.getServers().add(Protocol.HTTP, DEFAULT_PORT);
        component.getDefaultHost().attach(app);

        try {
            logger.info(
                "Starting rest HTTP server on " +
                "localhost:" + DEFAULT_PORT);

            component.start();
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
