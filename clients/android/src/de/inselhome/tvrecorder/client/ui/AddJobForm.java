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
package de.inselhome.tvrecorder.client.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import de.inselhome.tvrecorder.common.objects.Channel;

import de.inselhome.tvrecorder.client.R;
import de.inselhome.tvrecorder.client.TvRecorder;
import de.inselhome.tvrecorder.client.handlers.RetrieveChannelsHandler;
import de.inselhome.tvrecorder.client.listeners.RecordJobListener;


/**
 * This view contains widgets to adjust the start and end time, the channel and
 * and the name of a new {@link Job} to create.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class AddJobForm
extends      ScrollView
{
    /**
     * The raw channels supported by the server.
     */
    protected List<Channel> rawChannels;

    /**
     * The {@link DateTimeWidget} to select the start point.
     */
    protected DateTimeWidget start;

    /**
     * The {@link DateTimeWidget} to select the end point.
     */
    protected DateTimeWidget end;

    /**
     * The {@link Spinner} to select the {@link Channel}.
     */
    protected Spinner channels;

    /**
     * The {@Link EditText} field to adjust the name of the {@link Job}.
     */
    protected EditText name;

    /**
     * The {@link Button} to add the {@link Job}.
     */
    protected Button add;


    /**
     * The default constructor to create a new {@link AddJobForm}. The ui
     * creation takes place in {@link createContent(Context)} and is triggered
     * after all components have been initialized.
     *
     * @param context The context where this widget will live in.
     */
    public AddJobForm(Context context) {
        super(context);

        rawChannels = new ArrayList<Channel>();

        start    = new DateTimeWidget(context);
        end      = new DateTimeWidget(context);
        channels = new Spinner(context);
        name     = new EditText(context);
        add      = new Button(context);

        addView(createContent(context));
    }


    /**
     * This method creates the ui.
     *
     * @param context The context where this {@link View} will live in.
     *
     * @return the created view.
     */
    protected View createContent(Context context) {
        LinearLayout layout       = new LinearLayout(context);
        TextView     startTitle   = new TextView(context);
        TextView     endTitle     = new TextView(context);
        TextView     channelTitle = new TextView(context);
        TextView     nameTitle    = new TextView(context);

        layout.setOrientation(LinearLayout.VERTICAL);

        startTitle.setText(R.string.addjob_start);
        endTitle.setText(R.string.addjob_end);
        channelTitle.setText(R.string.addjob_channel);
        nameTitle.setText(R.string.addjob_name);

        name.setPadding(10,0,0,0);
        name.setWidth(0);

        add.setOnClickListener(new RecordJobListener((TvRecorder) context));
        add.setText(R.string.addjob_start_record);

        layout.addView(startTitle);
        layout.addView(start);
        layout.addView(endTitle);
        layout.addView(end);
        layout.addView(channelTitle);
        layout.addView(channels);
        layout.addView(nameTitle);
        layout.addView(name);
        layout.addView(add);

        return layout;
    }

    /**
     * This method is used to update the channel list with new channels.
     *
     * @param c new Channels.
     */
    public void updateChannels(Channel[] c) {
        Log.i(
            "TvR [TvRecorderActivity]",
            "updateChannels() - " + c.length + " channels.");

        ArrayAdapter adapter = new ArrayAdapter(
            getContext(), android.R.layout.simple_spinner_dropdown_item);

        for (Channel channel: c) {
            rawChannels.add(channel);
            adapter.add(channel.getDescription());
        }

        channels.setAdapter(adapter);
    }


    /**
     * This method makes use of {@link RetrieveChannelsHandler}. A {@link
     * ProgressDialog} is displayed to inform the user about the loading
     * process. After the channels have been received, this dialog is closed.
     */
    public void refresh() {
        TvRecorder recorder  = (TvRecorder) getContext();
        Resources  resources = recorder.getResources();

        ProgressDialog d = ProgressDialog.show(
            recorder,
            resources.getString(R.string.addjob_load_progress_title),
            resources.getString(R.string.addjob_load_progress_text));

        RetrieveChannelsHandler handler = new RetrieveChannelsHandler(recorder);

        handler.run();

        d.hide();
    }


    /**
     * This method retrieves the start date and time as {@link Calendar} object.
     *
     * @return the start date and time.
     */
    public Calendar getStart() {
        return start.getDatetime();
    }


    /**
     * This method retrieves the end date and time as {@link Calendar} object.
     *
     * @return the end date and time.
     */
    public Calendar getEnd() {
        return end.getDatetime();
    }


    /**
     * This method retrieves the selected channel as string.
     *
     * @return the selected channel as string.
     */
    public Channel getChannel() {
        int idx = channels.getSelectedItemPosition();
        return rawChannels.get(idx);
    }


    /**
     * This method retrieves the user inserted name of <i>unknown</i> if no name
     * is given.
     *
     * @return the name or <i>unknown</i> if no name is given.
     */
    public String getName() {
        CharSequence n = name.getText();
        return n != null ? n.toString() : "unknown";
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
