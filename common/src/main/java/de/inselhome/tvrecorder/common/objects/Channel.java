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
package de.inselhome.tvrecorder.common.objects;

import java.io.Serializable;


/**
 * This class represents a channel on the tv. For simplicity, this class
 * currently just contains a key and a description. The description might be
 * used to display in the gui, the key is used to identify the channel and needs
 * to be unique.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class Channel
implements   Serializable
{
    /**
     * The unique key of this channel.
     */
    protected String key;

    /**
     * The description of the channel. This should be a human readable string.
     */
    protected String description;


    public Channel() {
    }


    /**
     * The standard constructor that creates a new Channel object made of a key
     * and a description.
     *
     * @param key the key.
     * @param description the description.
     */
    public Channel(String key, String description) {
        this.key         = key;
        this.description = description;
    }


    /**
     * Retrieves the key.
     *
     * @return the key.
     */
    public String getKey() {
        return key;
    }


    /**
     * Retrieves the description.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
