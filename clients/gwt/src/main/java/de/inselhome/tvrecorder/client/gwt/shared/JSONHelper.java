package de.inselhome.tvrecorder.client.gwt.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.inselhome.tvrecorder.client.gwt.shared.model.DefaultChannel;
import de.inselhome.tvrecorder.client.gwt.shared.model.DefaultTvShow;
import de.inselhome.tvrecorder.client.gwt.shared.model.Channel;
import de.inselhome.tvrecorder.client.gwt.shared.model.TvShow;


public class JSONHelper {

    private static Logger logger = Logger.getLogger(JSONHelper.class);


    public static List<Channel> tvGuideFromJSON(JSONArray arr)
    throws JSONException
    {
        long start = System.currentTimeMillis();

        int num = arr != null ? arr.length() : 0;

        List<Channel> channels = new ArrayList<Channel>(num);

        for (int i = 0; i < num; i++) {
            JSONObject obj = arr.getJSONObject(i);

            String key  = obj.getString("key");
            String desc = obj.getString("description");

            Channel channel = new DefaultChannel(key, desc);

            JSONArray jsonShows = obj.getJSONArray("listing");

            int len = jsonShows != null ? jsonShows.length() : 0;

            for (int j = 0; j < len; j++) {
                channel.addTvShow(tvShowFromJSON(jsonShows.getJSONObject(j)));
            }

            channels.add(channel);
        }

        long end = System.currentTimeMillis();

        logger.debug("Parsing TvGuide took " + (end-start) + "ms.");

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

        return new DefaultTvShow(title, desc, categ, start, end, len);
    }
}
