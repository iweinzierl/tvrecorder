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
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;

import de.inselhome.tvrecorder.common.objects.Channel;

import de.inselhome.tvrecorder.server.backend.Backend;
import de.inselhome.tvrecorder.server.config.Config;
import de.inselhome.tvrecorder.server.tvguide.TvShowManager;
import de.inselhome.tvrecorder.server.tvguide.xmltv.XmlTvShowManager;
import de.inselhome.tvrecorder.server.utils.DVBSChannelsParser;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvRecorderServer {

    /**
     * The default port of the REST server: 8282
     */
    public static final int DEFAULT_PORT = 8282;

    /**
     * The key that is used to store the channels in the context.
     */
    public static final String CHANNELS_KEY = "tvrecorder.channels";

    /**
     * The key that is used to store a TvShowManager in the context.
     */
    public static final String TVSHOW_MANAGER = "tvrecorder.tvshowmanager";



    private static final Logger logger =
        Logger.getLogger(TvRecorderServer.class);


    public TvRecorderServer() {
    }


    /**
     * Create REST components and start HTTP server.
     */
    public void start() {
        logger.debug("Create necessary components for HTTP server.");

        Config config = Config.getInstance();

        Backend backend = new Backend();

        String    path     = config.getProperty(Config.XPATH_CHANNELS_FILE);
        Channel[] channels = DVBSChannelsParser.parse(path);

        if (channels == null) {
            logger.error("Could not find any channels!");
            System.exit(1);
        }

        logger.info("There are " + channels.length + " channels available.");

        TvShowManager manager = new XmlTvShowManager();
        manager.start();

        RestApp app = new RestApp(backend, manager, channels);

        Component component = new Component();

        String portStr = config.getProperty(Config.XPATH_SERVER_PORT);
        int    port    = DEFAULT_PORT;
        if (portStr != null) {
            try {
                port = Integer.parseInt(portStr);
            }
            catch (NumberFormatException nfe) {
                logger.warn("Could not determine port configuration. " +
                            "Use standard port: " + port);
            }
        }

        component.getServers().add(Protocol.HTTP, port);

        String authType = config.getProperty(Config.XPATH_AUTH_TYPE);

        if (authType != null && authType.toLowerCase().equals("basicauth")) {
            logger.info("Prepare server for HTTP BasicAuth.");

            String login = config.getProperty(Config.XPATH_AUTH_LOGIN);
            String pass  = config.getProperty(Config.XPATH_AUTH_PASSWORD);

            ChallengeAuthenticator guard = new ChallengeAuthenticator(
                null, ChallengeScheme.HTTP_BASIC, "testRealm");

            MapVerifier mapVerifier = new MapVerifier();
            mapVerifier.getLocalSecrets().put(login, pass.toCharArray());
            guard.setVerifier(mapVerifier);

            component.getDefaultHost().attach(guard);
            guard.setNext(app);
        }
        else {
            logger.warn(
                "No authentication method set. The server is not secure! " +
                "It is absolutely recommended to use authentication!");

            component.getDefaultHost().attach(app);
        }

        try {
            logger.info(
                "Starting rest HTTP server on " +
                "localhost:" + port);

            component.start();
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
