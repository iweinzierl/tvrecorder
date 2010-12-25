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
package de.inselhome.tvrecorder.client.listeners;

import java.util.Calendar;
import java.util.Date;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import org.restlet.resource.ClientResource;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.rest.RecordResource;

import de.inselhome.tvrecorder.client.Config;
import de.inselhome.tvrecorder.client.TvRecorder;
import de.inselhome.tvrecorder.client.ui.AddJobForm;
import de.inselhome.tvrecorder.client.utils.DateUtils;


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

        try {
            AddJobForm form = recorder.getAddJobForm();

            Calendar start = form.getStart();
            Calendar end   = form.getEnd();
            Channel  chann = form.getChannel();
            String   name  = form.getName();

            if (!isEndGreaterThanStart(end.getTime(), start.getTime())) {
                throw new IllegalArgumentException(
                    "End time needs to be greater than start time.");
            }

            String msg = "Neuen Job erstellt: \n";
            msg += "Start: ";
            msg += DateUtils.formatDate(start) + "  ";
            msg += DateUtils.formatTime(start);
            msg += "\nEnde: ";
            msg += DateUtils.formatDate(end) + "  ";
            msg += DateUtils.formatTime(end);
            msg += "\nKanal: ";
            msg += chann.getDescription();
            msg += "\nName: ";
            msg += name;

            ClientResource c = new ClientResource(Config.getServerResource(
                recorder, RecordResource.PATH));
            RecordResource resource = c.wrap(RecordResource.class);

            Job job = new Job(start.getTime(), end.getTime(), chann, name);

            Log.i("TvR [RecordJobListener]", "createJob() - add new job.");
            resource.add(job);

            Toast popup = Toast.makeText(recorder, msg, Toast.LENGTH_SHORT);
            popup.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            popup.show();
        }
        catch (Exception e) {
            String msg = "Error while adding new Job: " + e.getMessage();

            Toast popup = Toast.makeText(recorder, msg, Toast.LENGTH_SHORT);
            popup.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            popup.show();
        }
    }


    /**
     * This method compares two dates with a precision of minutes.
     *
     * @param end An end date.
     * @param start A start date.
     *
     * @return true, if <i>end</i> is greater that that <i>start</i>, otherwise
     * false.
     */
    public static boolean isEndGreaterThanStart(Date end, Date start) {
        long endTime   = end.getTime() / (1000 * 60);
        long startTime = start.getTime() / (1000 * 60);

        return endTime <= startTime ? false : true;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
