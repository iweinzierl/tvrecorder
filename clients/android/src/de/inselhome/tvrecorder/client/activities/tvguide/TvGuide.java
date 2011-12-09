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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;



import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.objects.TvShow;
import de.inselhome.tvrecorder.common.utils.JobBuilder;

import de.inselhome.tvrecorder.client.R;
import de.inselhome.tvrecorder.client.ui.ChannelView;
import de.inselhome.tvrecorder.client.util.JobRecorder;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvGuide
extends      Activity
implements   TvGuideUpdateListener {

    protected List<TvGuideUpdateListener> listeners;
    protected List<ChannelWithTvGuide>    channels;

    protected Channel selectedChannel;

    protected LinearLayout channelList;
    protected ListView     tvShows;

    protected ProgressDialog progress;


    private static final String TAG = "TvR [TvGuide]";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TvR [TvGuide]", "onCreate()");

        listeners   = new ArrayList<TvGuideUpdateListener>();

        addTvGuideUpdateListener(this);

        updateTvGuide();

        initLayout();
    }


    protected void initLayout() {
        setContentView(R.layout.tvguide);

        channelList = (LinearLayout) findViewById(R.id.channels);
        tvShows     = (ListView) findViewById(R.id.tvguide);

        Log.d("TvR [TvGuide]", "register tv show list for context menu");
        registerForContextMenu(tvShows);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("TvR [TvGuide]", " - onCreateOptionsMenu()");
        menu.add(R.string.tvguide_update_list);
        menu.add(R.string.tvguide_record_shows);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();

        Log.d("TvR [TvGuide]", "onOptionsItemSelected(): '" + title + "'");

        Resources res = getResources();

        String update = res.getString(R.string.tvguide_update_list);
        String record = res.getString(R.string.tvguide_record_shows);

        if (title.equals(update)) {
            updateTvGuide(true, true);
            return true;
        }
        else if (title.equals(record)) {
            TvShowsAdapter adapter = (TvShowsAdapter) tvShows.getAdapter();

            if (adapter != null) {
                recordShows(adapter.getSelectedTvShows());
            }
        }

        return super.onOptionsItemSelected(item);
    }


    public void addTvGuideUpdateListener(TvGuideUpdateListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Log.d("TvR [TvGuide]", "onCreateContextMenu");

        AdapterContextMenuInfo info  = (AdapterContextMenuInfo) menuInfo;
        TvShowsAdapter         shows = (TvShowsAdapter) tvShows.getAdapter();
        TvShow                 show  = (TvShow) shows.getItem(info.position);

        getMenuInflater().inflate(R.menu.tvguide_list_context, menu);

        menu.setHeaderTitle(show.getTitle());
    }


    protected void updateTvGuide() {
        updateTvGuide(false);
    }


    protected void updateTvGuide(final boolean allowHttp) {
        updateTvGuide(allowHttp, false);
    }


    /**
     * This method triggers the update process of the TvShows.
     */
    protected void updateTvGuide(
        final boolean allowHttp,
        final boolean forceHttp
    ) {
        Resources resources = getResources();

        progress = ProgressDialog.show(
            this,
            resources.getString(R.string.tvguide_load_progress_title),
            resources.getString(R.string.tvguide_load_progress_text),
            true);

        new AsyncTask<Void, Void, ChannelWithTvGuide[]>() {
            protected ChannelWithTvGuide[] doInBackground(Void... v) {
                return TvGuideDataStore.get(TvGuide.this, forceHttp, allowHttp);
            }

            protected void onPostExecute(ChannelWithTvGuide[] channels) {
                Log.d("TvR [TvGuide]", "HTTP request finished.");
                fireTvGuideUpdateEvent(channels);
            }
        }.execute();
    }


    protected void fireTvGuideUpdateEvent(ChannelWithTvGuide[] channels) {
        TvGuideUpdateEvent event = new TvGuideUpdateEvent(channels);

        for (TvGuideUpdateListener listener: listeners) {
            listener.onTvGuideUpdate(event);
        }
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

        if (channels == null) {
            Log.w("TvR [TvGuide]", "No channels with TvShows found!");
            progress.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error);
            builder.setMessage(R.string.error_no_channels);
            builder.show();

            return;
        }

        channelList.removeAllViews();

        boolean first = true;

        for (ChannelWithTvGuide c: channels) {
            if (first) {
                setSelectedChannel(c);
                updateTvShows(c);
                first = false;
            }

            ChannelView view = new ChannelView(this, c);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ChannelView tmp = (ChannelView) v;

                    ChannelWithTvGuide ctv = (ChannelWithTvGuide)
                        tmp.getChannel();

                    setSelectedChannel(ctv);
                    updateTvShows(ctv);
                }
            });

            channelList.addView(view);
        }

        progress.dismiss();
    }


    public void setSelectedChannel(Channel selectedChannel) {
        this.selectedChannel = selectedChannel;
    }


    public Channel getSelectedChannel() {
        return selectedChannel;
    }


    protected void updateTvShows(final ChannelWithTvGuide channel) {
        Log.d("TvR [TvGuide]", "Update channel: " + channel);

        if (channel == null) {
            Log.d("TvR [TvGuide]", "Channel is NULL.");
            return;
        }

        Collection<TvShow> tmp = channel.getSortedListing();

        final Activity activity = this;
        final TvShow[] shows    = (TvShow[]) tmp.toArray(new TvShow[tmp.size()]);

        tvShows.post(new Runnable() {
            public void run() {
                ArrayAdapter adapter = new TvShowsAdapter(
                    activity,
                    R.layout.tvguide_list,
                    shows);

                tvShows.setAdapter(adapter);
            }
        });
    }


    public void onTvGuideUpdateFailed(Exception e) {
        Log.e("TvR [TvGuide]", e.getMessage());

        // TODO IMPLEMENT ME
    }


    protected void recordShows(List<TvShow> shows) {
        if (shows == null || shows.isEmpty()) {
            return;
        }

        Channel    channel = getSelectedChannel();
        JobBuilder builder = new JobBuilder();

        final int[] success = new int[] { 0 };

        for (TvShow show: shows) {
            if(builder.addJob(channel, show)) {
                success[0] = success[0] + 1;
            }
        }

        new JobRecorder(this).record(builder.get());
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
