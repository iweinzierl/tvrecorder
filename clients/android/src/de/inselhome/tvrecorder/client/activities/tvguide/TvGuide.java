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

import java.io.IOException;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.objects.TvShow;
import de.inselhome.tvrecorder.common.rest.RecordResource;
import de.inselhome.tvrecorder.common.utils.DateUtils;
import de.inselhome.tvrecorder.common.utils.JSONUtils;

import de.inselhome.tvrecorder.client.Config;
import de.inselhome.tvrecorder.client.R;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvGuide
extends      Activity
implements   TvGuideUpdateListener {

    protected List<TvGuideUpdateListener> listeners;

    protected LinearLayout rootLayout;
    protected Spinner      channelList;
    protected ListView     tvShows;

    protected ProgressDialog progress;


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

        tvShows     = (ListView) findViewById(R.id.tvguide);
        channelList = (Spinner)  findViewById(R.id.channel_spinner);

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
            updateTvGuide(true);
            return true;
        }
        else if (title.equals(record)) {
            TvShowsAdapter adapter = (TvShowsAdapter) tvShows.getAdapter();
            recordShows(adapter.getSelectedTvShows());
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


    /**
     * This method triggers the update process of the TvShows.
     */
    protected void updateTvGuide(final boolean forceHttp) {
        Resources resources = getResources();

        progress = ProgressDialog.show(
            this,
            resources.getString(R.string.tvguide_load_progress_title),
            resources.getString(R.string.tvguide_load_progress_text),
            true);

        new AsyncTask<Void, Void, ChannelWithTvGuide[]>() {
            protected ChannelWithTvGuide[] doInBackground(Void... v) {
                return TvGuideDataStore.get(TvGuide.this, forceHttp);
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

        Log.d("TvR [TvGuide]", "Create ArrayAdapter now...");

        ArrayAdapter adapter = new ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_dropdown_item,
            channels);

        channelList.setAdapter(adapter);

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

                    Log.d("TvR [TvGuide]", "Update list with new channels.");

                    updateTvShows(c);
                }


                public void onNothingSelected(AdapterView view) {
                    // do nothing
                }
            });

        progress.dismiss();
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

        Toast popup = Toast.makeText(
            this,
            "Record " + shows.size() + " TvShows.\n" +
            "Currently not implemented.",
            Toast.LENGTH_SHORT);

        popup.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        popup.show();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
