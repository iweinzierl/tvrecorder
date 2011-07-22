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

import java.io.File;

import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.utils.DateUtils;

import de.inselhome.tvrecorder.server.config.Config;


/**
 * This class implements {@link CmdProducer} and produces a command line for
 * <i>MPlayer</i>.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class MPlayerCmdProducer implements CmdProducer {

    /**
     * The default constructor.
     */
    public MPlayerCmdProducer() {
    }


    /**
     * This method produces a command line that is used for <i>MPlayer</i>.
     *
     * @param job The job that contains some information used in the command
     * line (like output name).
     *
     * @return the command line.
     */
    public String produce(Job job) {
        String outname = job.getName().trim();
        if (outname == null || outname.equals("")) {
            outname = DateUtils.format(job.getStart(), DateUtils.OUTPUT_FORMAT);
        }

        Config config = Config.getInstance();
        String outdir = config.getProperty(Config.XPATH_OUTPUT_DIRECTORY);
        File   record = new File(outdir, outname);

        StringBuilder sb = new StringBuilder();
        sb.append("mplayer ");
        sb.append("-dumpstream ");
        sb.append("-dumpfile ");
        sb.append(record.getAbsolutePath());
        sb.append(".mpeg ");
        sb.append("dvb://");
        sb.append(job.getChannel().getKey());

        return sb.toString();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
