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
package de.inselhome.tvrecorder.common.rest;

import org.restlet.resource.Get;

import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;


public interface TvGuideResource {

    public static final String PATH = "/tvguide";

    @Get
    public ChannelWithTvGuide[] retrieve();
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
