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
package de.inselhome.tvrecorder.common.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.objects.TvShow;


public final class JobBuilder {

    protected List<Job> jobs;

    public JobBuilder() {
        jobs = new ArrayList<Job>();
    }


    public boolean addJob(Job job) {
        jobs.add(job);
        return true;
    }


    public boolean addJob(Date start, Date end, Channel channel, String name) {
        if (start != null && end != null && channel != null) {
            jobs.add(new Job(start, end, channel, name));
            return true;
        }

        return false;
    }


    public boolean addJob(Channel channel, TvShow show) {
        if (channel != null && show != null) {
            return addJob(
                show.getStart(),
                show.getEnd(),
                channel,
                show.getTitle());
        }

        return false;
    }


    public JSONArray toJSON() {
        JSONArray array = new JSONArray();

        for (Job job: jobs) {
            array.put(JSONUtils.toJSON(job));
        }

        return array;
    }


    public List<Job> get() {
        return jobs;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
