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
package de.inselhome.tvrecorder.client.activities.tvjoblist;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.inselhome.tvrecorder.common.objects.Channel;
import de.inselhome.tvrecorder.common.objects.Job;
import de.inselhome.tvrecorder.common.utils.DateUtils;

import de.inselhome.tvrecorder.client.R;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvJoblistAdapter extends ArrayAdapter<Job> {

    private static final String TAG = "TvR [TvJoblistAdapter]";

    protected List<Job> jobs;


    public TvJoblistAdapter(Context context, int resourceId) {
        super(context, resourceId);
    }


    public TvJoblistAdapter(Context context, int resourceId, List<Job> jobs) {
        super(context, resourceId, jobs);
        //super(context, resourceId, jobs != null
        //    ? (Job[]) jobs.toArray(new Job[jobs.size()])
        //    : new Job[0]);
        this.jobs = jobs;
    }


    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = vi.inflate(R.layout.tvjob_list, null);
        }

        final Job job = jobs.get(pos);

        if (job != null) {
            TextView start   = (TextView) view.findViewById(R.id.start);
            TextView end     = (TextView) view.findViewById(R.id.end);
            TextView name    = (TextView) view.findViewById(R.id.name);
            TextView channel = (TextView) view.findViewById(R.id.channel);

            start.setText(
                DateUtils.format(job.getStart(), DateUtils.DATETIME_FORMAT));

            end.setText(
                DateUtils.format(job.getEnd(), DateUtils.DATETIME_FORMAT));

            name.setText(job.getName());

            Resources res   = view.getResources();
            Channel   chann = job.getChannel();

            int resId = res.getIdentifier(
                chann.getKey().toLowerCase(),
                "drawable",
                "de.inselhome.tvrecorder.client");

            if (resId == 0) {
                channel.setText(job.getChannel().getDescription());
                channel.setBackgroundDrawable(null);
            }
            else {
                channel.setText(" ");
                channel.setBackgroundDrawable(res.getDrawable(resId));
            }
        }

        return view;
    }


    public List<Job> getJobs() {
        return jobs;
    }


    public void removeJobs(List<Job> toRemove) {
        OUTER: for (int i = jobs.size()-1; i >= 0; i--) {
            Job orig = jobs.get(i);

            String chann = orig.getChannel().getKey();
            long   start = orig.getStart().getTime();

            for (Job job: toRemove) {
                if (chann.equals(job.getChannel().getKey())
                    && start == job.getStart().getTime())
                {
                    jobs.remove(orig);
                    remove(orig);

                    continue OUTER;
                }
            }
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
