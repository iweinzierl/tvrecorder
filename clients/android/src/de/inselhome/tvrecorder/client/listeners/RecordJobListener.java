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
package de.inselhome.tvrecorder.client.android.listeners;

import java.util.Calendar;

import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.utils.DateUtils;

import de.inselhome.tvrecorder.client.android.R;
import de.inselhome.tvrecorder.client.android.TvRecorder;
import de.inselhome.tvrecorder.client.android.util.JobRecorder;


/**
 * A very simple {@link OnClickListener} that interacts with the {@link TvRecorder}.
 * After a click event is fired, a new {@link Job} will be created.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class RecordJobListener
implements   View.OnClickListener
{
    /**
     * The {@link TvRecorder}.
     */
    protected TvRecorder recorder;


    /**
     * The default constructor to create a new {@link RecordJobListener} object.
     *
     * @param recorder The {@link TvRecorder}.
     */
    public RecordJobListener(TvRecorder recorder) {
        this.recorder = recorder;
    }


    /**
     * After a click event is catched, a new {@link Job} will be created. The
     * job information are retrieved by {@link recorder}.
     */
    public void onClick(View v) {
        Log.i("TvR [RecordJobListener]", "onClick() - add new record.");

        Resources res = recorder.getResources();

        try {
            Calendar start = recorder.getStart();
            Calendar end   = recorder.getEnd();
            Channel  chann = recorder.getChannel();
            String   name  = recorder.getName();

            if (!DateUtils.isEndGreaterThanStart(
                end.getTime(), start.getTime()))
            {
                throw new IllegalArgumentException(
                    res.getString(R.string.error_endtime_before_starttime));
            }

            Job job = new Job(start.getTime(), end.getTime(), chann, name);

            new JobRecorder(recorder).record(job);
        }
        catch (Exception e) {
            String msg =
                res.getString(R.string.addjob_recorded_error_title)
                + " " + e.getMessage();

            Toast popup = Toast.makeText(recorder, msg, Toast.LENGTH_SHORT);
            popup.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            popup.show();
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
