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
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.objects.TvShow;


public class JSONUtils {


    private JSONUtils() {
    }


    public static JSONObject toJSON(ChannelWithTvGuide channel, boolean old) {
        Collection<TvShow> shows = channel.getSortedListing();
        JSONArray arr = new JSONArray();

        long current = System.currentTimeMillis();

        for (TvShow show: shows) {
            if (old || show.getStart().getTime() > current) {
                arr.put(toJSON(show));
            }
        }

        JSONObject obj = null;

        try {
            obj = new JSONObject();
            obj.put("key", channel.getKey());
            obj.put("description", channel.getDescription());
            obj.put("listing", arr);
        }
        catch (JSONException je) {
            return null;
        }

        return obj;
    }


    public static JSONObject toJSON(TvShow show) {
        JSONObject obj = null;

        try {
            obj = new JSONObject();
            obj.put("title", show.getTitle());
            obj.put("description", show.getDescription());
            obj.put("start", show.getStart().getTime());
            obj.put("end", show.getEnd().getTime());
            obj.put("length", show.getLength());

            String category = show.getCategory();
            obj.put("category", category != null ? category : "");
        }
        catch (JSONException je) {
            return null;
        }

        return obj;
    }


    public static JSONArray toJSON(List<Job> jobs) {
        JSONArray arr = new JSONArray();

        for (Job job: jobs) {
            JSONObject obj = toJSON(job);

            if (obj != null) {
                arr.put(obj);
            }
        }

        return arr;
    }


    public static JSONObject toJSON(Job job) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("name", job.getName());
            obj.put("channel", job.getChannel().getKey());
            obj.put("start", job.getStart().getTime());
            obj.put("end", job.getEnd().getTime());
        }
        catch (JSONException je) {
            return null;
        }

        return obj;
    }


    public static ChannelWithTvGuide[] tvGuideFromJSON(JSONArray arr)
    throws JSONException
    {
        int num = arr != null ? arr.length() : 0;

        ChannelWithTvGuide[] channels = new ChannelWithTvGuide[num];

        for (int i = 0; i < num; i++) {
            JSONObject obj = arr.getJSONObject(i);

            String key  = obj.getString("key");
            String desc = obj.getString("description");

            channels[i] = new ChannelWithTvGuide(key, desc);

            JSONArray jsonShows = obj.getJSONArray("listing");

            int len = jsonShows != null ? jsonShows.length() : 0;

            for (int j = 0; j < len; j++) {
                channels[i].addTvShow(tvShowFromJSON(jsonShows.getJSONObject(j)));
            }
        }

        return channels;
    }


    public static TvShow tvShowFromJSON(JSONObject obj)
    throws JSONException
    {
        String title = obj.getString("title");
        String desc  = obj.getString("description");
        Date   start = new Date(obj.getLong("start"));
        Date   end   = new Date(obj.getLong("end"));
        int    len   = obj.getInt("length");
        String categ = obj.getString("category");

        return new TvShow(title, desc, start, end, categ, len);
    }


    public static List<Job> jobsFromJSON(JSONArray arr)
    throws JSONException
    {
        int len = arr.length();

        List<Job> jobs = new ArrayList<Job>(len);

        for (int i = 0; i < len; i++) {
            jobs.add(jobFromJSON(arr.getJSONObject(i)));
        }

        return jobs;
    }


    public static Job jobFromJSON(JSONObject obj)
    throws JSONException
    {
        String name = obj.getString("name");
        String chan = obj.getString("channel");
        Date start  = new Date(obj.getLong("start"));
        Date end    = new Date(obj.getLong("end"));

        return new Job(start, end, new Channel(chan, chan), name);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
