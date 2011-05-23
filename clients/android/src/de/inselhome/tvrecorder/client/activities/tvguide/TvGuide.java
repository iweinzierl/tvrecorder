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
package de.inselhome.tvrecorder.client.activities.tvguide;

import java.util.Collection;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import de.inselhome.tvrecorder.client.R;
import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.objects.TvShow;


public class TvGuide extends Activity implements TvGuideUpdateListener {

    protected RetrieveChannelsHandler handler;

    protected LinearLayout rootLayout;
    protected Spinner      channelList;
    protected ListView     tvShows;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TvR [TvGuide]", "onCreate()");

        channelList = new Spinner(this);
        tvShows     = new ListView(this);
        handler     = new RetrieveChannelsHandler(this);
        handler.addTvGuideUpdateListener(this);

        rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.addView(channelList);
        rootLayout.addView(tvShows);

        setContentView(rootLayout);

        updateTvGuide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("TvR [TvGuide]", " - onCreateOptionsMenu()");
        menu.add(R.string.tvguide_update_list);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();

        Log.d("TvR [TvGuide]", "onOptionsItemSelected(): '" + title + "'");

        Resources res = getResources();

        String update = res.getString(R.string.tvguide_update_list);

        if (title.equals(update)) {
            updateTvGuide();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * This method triggers the update process of the TvShows.
     */
    protected void updateTvGuide() {
        Thread t = new Thread(handler);
        t.start();
    }


    /**
     * This method is used to update the tv guide list.
     *
     * @param event The event that stores the updated channel list.
     */
    public void onTvGuideUpdate(TvGuideUpdateEvent event) {
        Log.d("TvR [TvGuide]", "onTvGuideUpdate");

        final ChannelWithTvGuide[] channels = event.getChannels();
        final Activity             activity = this;

        rootLayout.post(new Runnable() {
            public void run() {
                ArrayAdapter adapter = new ArrayAdapter(
                    activity,
                    android.R.layout.simple_spinner_dropdown_item,
                    channels);

                channelList.setAdapter(adapter);
            }
        });

        channelList.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(
                    AdapterView parent,
                    View        view,
                    int         position,
                    long        id)
                {
                    ChannelWithTvGuide c = (ChannelWithTvGuide)
                        parent.getItemAtPosition(position);

                    updateTvShows(c);
                }


                public void onNothingSelected(AdapterView view) {
                    // do nothing
                }
            });
    }


    protected void updateTvShows(final ChannelWithTvGuide channel) {
        Log.d("TvR [TvGuide]", "Update channel: " + channel);

        Collection<TvShow> tmp = channel.getSortedListing();

        final Activity activity = this;
        final TvShow[] shows    = (TvShow[]) tmp.toArray(new TvShow[tmp.size()]);

        rootLayout.post(new Runnable() {
            public void run() {
                ArrayAdapter adapter = new ArrayAdapter(
                    activity,
                    R.layout.list_item,
                    shows);

                tvShows.setAdapter(adapter);
            }
        });
    }


    public void onTvGuideUpdateFailed(Exception e) {
        // TODO DO SOMETHING
    }
}
