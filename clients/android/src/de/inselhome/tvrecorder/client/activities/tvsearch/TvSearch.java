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
package de.inselhome.tvrecorder.client.android.activities.tvsearch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.objects.TvShow;
import de.inselhome.tvrecorder.common.utils.JobBuilder;

import de.inselhome.tvrecorder.client.android.R;
import de.inselhome.tvrecorder.client.android.activities.tvguide.TvGuideDataStore;
import de.inselhome.tvrecorder.client.android.activities.tvguide.TvShowsAdapter;
import de.inselhome.tvrecorder.client.android.util.JobRecorder;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvSearch
extends      Activity
{
    private static final String TAG = "TvR [TvSearch]";


    public static final int OPTIONS_RECORD_SELECTED = 0;
    public static final int OPTIONS_RECORD_ALL      = 1;


    protected TextView searchresult;
    protected ListView resultList;

    protected ChannelWithTvGuide[] channels;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        channels = TvGuideDataStore.get(this, false, false);
        final TvShow[] shows = getAllTvShows();

        setContentView(R.layout.tvsearch);

        searchresult = (TextView) findViewById(R.id.searchresults);
        searchresult.setText(
            "Found " + (shows != null ? shows.length : 0) + " results found.");

        EditText searchfield = (EditText) findViewById(R.id.searchtext);
        searchfield.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    filterList(s.toString());
                }
                else {
                    displayShows(shows);
                }
            }

            public void beforeTextChanged(CharSequence cs, int s, int c, int a){
                // do nothing
            }

            public void onTextChanged(CharSequence cs, int s, int b, int c) {
                // do nothing
            }
        });

        resultList = (ListView) findViewById(R.id.resultlist);

        displayShows(shows);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");

        menu.add(
            0,
            OPTIONS_RECORD_SELECTED,
            0,
            R.string.tvsearch_options_record_selected);

        menu.add(
            0,
            OPTIONS_RECORD_ALL,
            0,
            R.string.tvsearch_options_record_all);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        int id = item.getItemId();

        TvShowsAdapter adapter = (TvShowsAdapter) resultList.getAdapter();

        if (id == OPTIONS_RECORD_SELECTED) {
            recordShows(adapter.getSelectedTvShows());
            return true;
        }
        else if (id == OPTIONS_RECORD_ALL) {
            recordShows(adapter.getAllTvShows());
            return true;
        }

        return false;
    }


    protected void recordShows(List<TvShow> shows) {
        if (shows == null || shows.isEmpty()) {
            return;
        }

        JobBuilder  builder = new JobBuilder();
        final int[] success = new int[] { 0 };

        for (TvShow show: shows) {
            Channel channel = show.getChannel();

            if (channel != null) {
                if(builder.addJob(channel, show)) {
                    success[0] = success[0] + 1;
                }
            }
        }

        new JobRecorder(this).record(builder.get());
    }


    protected TvShow[] getAllTvShows() {
        ArrayList<TvShow> shows = new ArrayList<TvShow>();

        for (ChannelWithTvGuide channel: channels) {
            for (TvShow show: channel.getSortedListing()) {
                show.setChannel(channel);
                shows.add(show);
            }
        }

        return shows.toArray(new TvShow[shows.size()]);
    }


    protected void filterList(String search) {
        ArrayList<TvShow> shows = new ArrayList<TvShow>();

        for (ChannelWithTvGuide channel: channels) {
            for (TvShow show: channel.getSortedListing()) {
                if (show.getTitle().indexOf(search) >= 0) {
                    shows.add(show);
                }
            }
        }

        displayShows(shows.toArray(new TvShow[shows.size()]));
    }


    protected void displayShows(TvShow[] shows) {
        int num = shows != null ? shows.length : 0;

        searchresult.setText("Found " + num + " results.");

        TvShow[] data = num > 0
            ? shows
            : new TvShow[0];

        resultList.setAdapter(new TvShowsAdapter(
            this,
            R.layout.tvguide_list,
            data));
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
