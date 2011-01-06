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
package de.inselhome.tvrecorder.server.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.inselhome.tvrecorder.common.objects.Channel;

import de.inselhome.tvrecorder.server.utils.FileUtils;


/**
 * This class defines some helper functions to parse the {@link Channel}s from
 * a MPlayer DVB-S channels configuration file.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class DVBSChannelsParser {

    /** The logger that is used in this class for logging. */
    private static Logger logger = Logger.getLogger(DVBSChannelsParser.class);


    /**
     * This function takes the filename of a MPlayer DVB-S channels
     * configuration file and parses all contained channels.
     *
     * @param filename The filename of the channels config file.
     *
     * @return an array of {@link Channel}s.
     */
    public static Channel[] parse(String filename) {
        if (filename == null) {
            logger.error("Cannot parse channels file. Filename is null.");
            return null;
        }

        List<Channel> channels = new ArrayList<Channel>();
        String[]      lines    = FileUtils.readLines(filename);

        for (String line: lines) {
            Channel channel = extractChannel(line);

            if (channel != null) {
                channels.add(channel);
            }
        }

        return (Channel[]) channels.toArray(new Channel[channels.size()]);
    }


    /**
     * This method is used to extract a {@link Channel} from a single line of a
     * MPlayer DVB-S configuration file. It is called by {@link parse(String)}.
     *
     * @param line a line of a MPlayer channels config file.
     *
     * @return the {@link Channel}.
     */
    public static Channel extractChannel(String line) {
        if (line == null) {
            logger.warn("Cannot extract Channel. Line is empty.");
            return null;
        }

        String[] cols = line.split(":");
        if (cols != null && cols[0] != null && !cols[0].equals("")) {
            return new Channel(cols[0], cols[0]);
        }

        return null;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
