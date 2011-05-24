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
package de.inselhome.tvrecorder.server;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.log4j.PropertyConfigurator;

import de.inselhome.tvrecorder.server.rest.TvRecorderServer;


/**
 * Starting point of the TvRecorder server.
 *
 * @author <a href="mailto:ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class App {

    /**
     * The path to the logging configuration relative to {@link CONFIG_DIR}.
     * The value of this attribute can be defined by system property
     * <i>tvrecorder.server.logging</i>. If this property is not set, the value
     * will be <i>log4j.properties</i>.
     */
    public static final String LOGGING_PROPERTIES =
        System.getProperty("tvrecorder.server.logging", "log4j.properties");

    /**
     * The path to the configuration directory. The value of this attribute can
     * be modified via system property <i>tvrecorder.server.config.dir</i>. If
     * the system property is not set, the value will be <i>conf</i>.
     */
    public static final String CONFIG_DIR =
        System.getProperty("tvrecorder.server.config.dir", "conf");

    /**
     * Initialize logging configuration.
     */
    public static final void configureLogging() {
        File config     = new File(CONFIG_DIR);
        File properties = new File(config, LOGGING_PROPERTIES);

        if (properties.isFile() && properties.canRead()) {
            try {
                PropertyConfigurator.configure(properties.toURI().toURL());
            }
            catch (MalformedURLException mue) {
                mue.printStackTrace(System.err);
            }
        }
    }


    /**
     * The entry point of the application. This method triggers the
     * initialization of the logging, creates the HTTP server and starts it.
     *
     * @param args Arguments.
     */
    public static void main(String[] args) {

        configureLogging();

        TvRecorderServer server = new TvRecorderServer();

        server.start();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
