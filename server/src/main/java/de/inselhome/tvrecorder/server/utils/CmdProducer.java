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

import de.inselhome.tvrecorder.common.objects.Job;


/**
 * This interface describes methods to create command lines.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public interface CmdProducer {

    /** The default directory that contains the records. */
    public static final String DEFAULT_OUTPUT_DIR = "records/";

    /**
     * This method should produce the command line.
     *
     * @return the command line.
     */
    public String produce(Job job);
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
