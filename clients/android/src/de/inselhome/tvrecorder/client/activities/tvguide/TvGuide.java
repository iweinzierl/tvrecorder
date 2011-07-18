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
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import org.restlet.resource.ClientResource;

import de.inselhome.tvrecorder.client.R;
import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.objects.TvShow;
import de.inselhome.tvrecorder.common.rest.TvGuideResource;

import de.inselhome.tvrecorder.client.Config;
import de.inselhome.tvrecorder.client.activities.tvshow.TvShowDetail;


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


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        TvShow show = (TvShow) tvShows.getAdapter().getItem(info.position);

        if (show == null) {
            Log.d("TvR [TvGuide]", "Context menu selected no item.");
            return super.onContextItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.details: {
                showDetails(show);
                break;
            }
            case R.id.record: {
                recordShow(show);
                break;
            }
        }

        return super.onContextItemSelected(item);
    }


    /**
     * This method triggers the update process of the TvShows.
     */
    protected void updateTvGuide() {
        Resources resources = getResources();

        progress = ProgressDialog.show(
            this,
            resources.getString(R.string.tvguide_load_progress_title),
            resources.getString(R.string.tvguide_load_progress_text),
            true);

        new AsyncTask<Void, Void, ChannelWithTvGuide[]>() {
            protected ChannelWithTvGuide[] doInBackground(Void... v) {
                ClientResource cr = Config.getClientResource(
                    TvGuide.this, TvGuideResource.PATH);

                try {
                    TvGuideResource resource = cr.wrap(TvGuideResource.class);
                    ChannelWithTvGuide[] channels = resource.retrieve();

                    return channels;
                }
                catch (Exception e) {
                    Log.e("TvR [TvGuide]", "No channels found.");
                }

                return null;
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


    protected void showDetails(TvShow show) {
        Intent intent = new Intent(this, TvShowDetail.class);
        intent.putExtra(TvShowDetail.SHOW_TITLE, show.getTitle());
        intent.putExtra(TvShowDetail.SHOW_DESCRIPTION, show.getDescription());
        intent.putExtra(TvShowDetail.SHOW_STARTDATE, show.getStart());
        startActivity(intent);
    }


    protected void recordShow(TvShow show) {
        // TODO IMPLEMENT ME
        Log.e("TvR [TvGuide]", "recordShow() currently not implemented.");
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
