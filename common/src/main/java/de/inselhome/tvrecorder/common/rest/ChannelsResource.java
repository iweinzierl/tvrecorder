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
package de.inselhome.tvrecorder.common.rest;

import org.restlet.resource.Get;

import de.inselhome.tvrecorder.common.objects.Channel;


/**
 * This class represents the interface description of a resource to retrieve
 * channels that are supported by the server. It consists of a single method
 * retrieve that retrieves an array of supported channels.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public interface ChannelsResource {

    public static final String PATH = "/channels";

    @Get
    public Channel[] retrieve();
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
