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
package de.inselhome.tvrecorder.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.restlet.resource.ClientResource;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.rest.ChannelsResource;
import de.inselhome.tvrecorder.common.utils.DateUtils;

import de.inselhome.tvrecorder.client.listeners.RecordJobListener;
import de.inselhome.tvrecorder.client.activities.addjob.OnChannelsUpdatedListener;
import de.inselhome.tvrecorder.client.activities.setup.TvRecorderSettings;
import de.inselhome.tvrecorder.client.activities.tvguide.TvGuide;
import de.inselhome.tvrecorder.client.services.TvGuideUpdateService;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvRecorder extends Activity implements OnChannelsUpdatedListener {

    protected List<OnChannelsUpdatedListener> listeners;

    protected List<Channel> rawChannels;

    protected ProgressDialog dialog;

    protected TextView start_date;
    protected TextView start_time;
    protected TextView end_date;
    protected TextView end_time;
    protected EditText name;
    protected Spinner  channels;
    protected Button   record;

    protected GregorianCalendar start_datetime;
    protected GregorianCalendar end_datetime;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TvR [TvRecorderActivity]", "onCreate() - create view");

        listeners      = new ArrayList<OnChannelsUpdatedListener>();
        rawChannels    = new ArrayList<Channel>();
        start_datetime = new GregorianCalendar();
        end_datetime   = new GregorianCalendar();

        addOnChannelsUpdatedListener(this);

        setContentView(R.layout.tvrecorder);
        initLayout();
        updateDateTime();
        updateChannels();

        boolean autoUpdate = Config.getPreferenceAsBool(
            this, Config.SETTINGS_TVGUIDE_AUTO_UPDATE, false);

        if (autoUpdate) {
            Log.d("TvR [TvRecorderActivity]", "Start TvGuideUpdateService");
            Intent service = new Intent(this, TvGuideUpdateService.class);
            startService(service);
        }
    }


    /**
     * Initializes the UI components of this activity and its listeners.
     */
    protected void initLayout() {
        Log.d("TvR [TvRecorderActivity]", "initLayout()");

        start_date = (TextView) findViewById(R.id.start_date);
        start_time = (TextView) findViewById(R.id.start_time);
        end_date   = (TextView) findViewById(R.id.end_date);
        end_time   = (TextView) findViewById(R.id.end_time);
        name       = (EditText) findViewById(R.id.addjob_name);
        channels   = (Spinner)  findViewById(R.id.addjob_channel_spinner);
        record     = (Button)   findViewById(R.id.record);

        start_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(
                    TvRecorder.this,
                    new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(
                            DatePicker picker,
                            int        year,
                            int        month,
                            int        day)
                        {
                            Log.d("TvR [TvRecorderActivity]", "Selected start date");
                            start_datetime.set(Calendar.YEAR, year);
                            start_datetime.set(Calendar.MONTH, month);
                            start_datetime.set(Calendar.DAY_OF_MONTH, day);
                            updateDateTime();
                        }
                    },
                    start_datetime.get(Calendar.YEAR),
                    start_datetime.get(Calendar.MONTH),
                    start_datetime.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        start_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new TimePickerDialog(
                    TvRecorder.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(
                            TimePicker picker,
                            int        hours,
                            int        minutes)
                        {
                            Log.d("TvR [TvRecorderActivity]", "Selected start time");
                            start_datetime.set(Calendar.HOUR_OF_DAY, hours);
                            start_datetime.set(Calendar.MINUTE, minutes);
                            updateDateTime();
                        }
                    },
                    start_datetime.get(Calendar.HOUR),
                    start_datetime.get(Calendar.MINUTE),
                    true
                ).show();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(
                    TvRecorder.this,
                    new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(
                            DatePicker picker,
                            int        year,
                            int        month,
                            int        day)
                        {
                            Log.d("TvR [TvRecorderActivity]", "Selected end date");
                            end_datetime.set(Calendar.YEAR, year);
                            end_datetime.set(Calendar.MONTH, month);
                            end_datetime.set(Calendar.DAY_OF_MONTH, day);
                            updateDateTime();
                        }
                    },
                    end_datetime.get(Calendar.YEAR),
                    end_datetime.get(Calendar.MONTH),
                    end_datetime.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new TimePickerDialog(
                    TvRecorder.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(
                            TimePicker picker,
                            int        hours,
                            int        minutes)
                        {
                            Log.d("TvR [TvRecorderActivity]", "Selected end time");
                            end_datetime.set(Calendar.HOUR_OF_DAY, hours);
                            end_datetime.set(Calendar.MINUTE, minutes);
                            updateDateTime();
                        }
                    },
                    end_datetime.get(Calendar.HOUR),
                    end_datetime.get(Calendar.MINUTE),
                    true
                ).show();
            }
        });

        record.setOnClickListener(new RecordJobListener(this));
    }


    /**
     * Update the date/time views with the data stored in start_datetime and
     * end_datetime.
     */
    protected void updateDateTime() {
        Log.d(
            "TvR [TvRecorderActivity]", "updateDateTime() - " +
            "start: " + start_datetime.getTime());

        Log.d(
            "TvR [TvRecorderActivity]", "updateDateTime() - " +
            "end: " + end_datetime.getTime());

        start_date.setText(DateUtils.format(
            start_datetime.getTime(), DateUtils.DATE_FORMAT));

        start_time.setText(DateUtils.format(
            start_datetime.getTime(), DateUtils.TIME_FORMAT));

        end_date.setText(DateUtils.format(
            end_datetime.getTime(), DateUtils.DATE_FORMAT));

        end_time.setText(DateUtils.format(
            end_datetime.getTime(), DateUtils.TIME_FORMAT));
    }


    public Calendar getStart() {
        return start_datetime;
    }


    public Calendar getEnd() {
        return end_datetime;
    }


    public Channel getChannel() {
        int idx = channels.getSelectedItemPosition();
        return rawChannels.get(idx);
    }


    public String getName() {
        CharSequence n = name.getText();
        return n != null ? n.toString() : "unknown";
    }


    public void addOnChannelsUpdatedListener(OnChannelsUpdatedListener l) {
        if (l != null) {
            listeners.add(l);
        }
    }


    protected void fireOnChannelsUpdated(Channel[] channels) {
        for (OnChannelsUpdatedListener l : listeners) {
            Log.d("TvR [TvRecorder]", "Listener: " + l);
            l.onChannelsUpdated(channels);
        }
    }


    /**
     * Triggers the download of supported channels from server.
     */
    protected void updateChannels() {
        Resources resources = getResources();

        dialog = ProgressDialog.show(
            this,
            resources.getString(R.string.addjob_load_progress_title),
            resources.getString(R.string.addjob_load_progress_text),
            true);

        Log.d("TvR [TvRecorder]", "updateChannels()");

        final TvRecorder tvrecorder = this;

        new AsyncTask<Void, Void, Channel[]>() {
            protected Channel[] doInBackground(Void... v1) {
                ClientResource cr = Config.getClientResource(
                    tvrecorder, ChannelsResource.PATH);

                ChannelsResource resource = cr.wrap(ChannelsResource.class);
                try {
                    return resource.retrieve();
                }
                catch (Exception e) {
                    Log.e("TvR [TvRecorder]", e.getMessage());
                }

                return null;
            }

            protected void onPostExecute(Channel[] channels) {
                Log.d("TvR [TvRecorder]", "HTTP request finished.");
                fireOnChannelsUpdated(channels);
            }
        }.execute();
    }


    /**
     * Replaces the - if existing - old channels of spinner with new channels.
     *
     * @param channels A list of channels.
     */
    public void onChannelsUpdated(Channel[] channels) {
        dialog.dismiss();

        if (channels == null) {
            Log.w("TvR [TvRecorder]", "No channels found!");
            AlertDialog.Builder adb = new AlertDialog.Builder(TvRecorder.this);
            adb.setTitle(R.string.error);
            adb.setMessage(R.string.error_no_channels);
            adb.show();
            return;
        }

        Log.i("TvR [TvRecorder]", "Found " + channels.length + " channels");

        ArrayAdapter adapter = new ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item);

        for (Channel channel: channels) {
            rawChannels.add(channel);
            adapter.add(channel.getDescription());
        }

        this.channels.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("TvR [TvRecorder]", " - onCreateOptionsMenu()");
        menu.add(R.string.tvrecorder_contextmenu_quit);
        menu.add(R.string.tvrecorder_contextmenu_settings);
        menu.add(R.string.tvrecorder_contextmenu_tvguide);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();

        Log.d(
            "TvR [TvRecorder]",
            "onOptionsItemSelected(): selected '" + title + "'");

        Resources res = getResources();

        String quit  = res.getString(R.string.tvrecorder_contextmenu_quit);
        String sett  = res.getString(R.string.tvrecorder_contextmenu_settings);
        String guide = res.getString(R.string.tvrecorder_contextmenu_tvguide);
        Log.d("TvR [TvRecorder]", "onOptionsItemSelected(): settings = " + sett);

        if (title.equals(quit)) {
            finish();
            return true;
        }
        else if (title.equals(sett)) {
            Log.d("TvR [TvRecorder]", "onOptionsItemSelected(): goto settings");
            startActivity(new Intent(this, TvRecorderSettings.class));
            return true;
        }
        else if (title.equals(guide)) {
            Log.d("TvR [TvRecorder]", "onOptionsItemSelected(): goto tv guide");
            startActivity(new Intent(this, TvGuide.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
