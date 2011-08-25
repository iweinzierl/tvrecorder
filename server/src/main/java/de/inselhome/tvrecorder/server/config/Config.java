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
package de.inselhome.tvrecorder.server.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.server.utils.FileUtils;
import de.inselhome.tvrecorder.server.utils.XMLUtils;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class Config {

    public static final String CONFIG_FILE =
        System.getProperty("tvrecorder.config", "conf/config.xml");

    public static final String XPATH_SERVER_PORT =
        "/tvrecorder/server/port/text()";

    public static final String XPATH_OUTPUT_DIRECTORY =
        "/tvrecorder/record/output-directory/text()";

    public static final String XPATH_DATABASE_URL =
        "/tvrecorder/database/url/text()";

    public static final String XPATH_CHANNELS_FILE =
        "/tvrecorder/channels/config-file/text()";

    public static final String XPATH_AUTH_TYPE =
        "/tvrecorder/authentication/@type";

    public static final String XPATH_AUTH_LOGIN =
        "/tvrecorder/authentication/login/text()";

    public static final String XPATH_AUTH_PASSWORD =
        "/tvrecorder/authentication/password/text()";

    public static final String XPATH_DATETIME_FORMAT =
        "/tvrecorder/tvguide/datetime-format/text()";

    public static final String XPATH_UPDATE_INTERVAL =
        "/tvrecorder/tvguide/update-interval/text()";


    private static Logger logger = Logger.getLogger(Config.class);


    /** The only instance of this class. */
    protected static Config INSTANCE;

    /** A map to buffer the config options. */
    protected Map    properties;


    /**
     * An object of this class cannot be instantiated directly. Use {@link
     * getInstance()} to do so.
     */
    private Config() {
    }


    /**
     * This method returns the only instance of this class.
     *
     * @return the singleton instance.
     */
    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }

        return INSTANCE;
    }


    /**
     * This method retrieves the config file as {@link File}.
     *
     * @return the config file.
     */
    public File getConfigFile() {
        return FileUtils.getFile(CONFIG_FILE);
    }


    /**
     * This method retrieves the config property specified by {@link xpath}.
     * After the first time, this property is read, it is stored in {@link
     * properties}. The next time, it is no longer necessary to search the xpath
     * in the xml configuration again.
     *
     * @param xpath the path to the property.
     *
     * @return the property as string.
     */
    public String getProperty(String xpath) {
        if (properties == null) {
            properties = new HashMap();
        }

        String prop = (String) properties.get(xpath);

        if (prop == null) {
            logger.debug("Read property from config file: " + xpath);

            Object document = XMLUtils.parse(getConfigFile());

            prop = XMLUtils.getXPathAsString(document, xpath, null);

            if (prop != null) {
                properties.put(xpath, prop);
            }
        }

        return prop;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
