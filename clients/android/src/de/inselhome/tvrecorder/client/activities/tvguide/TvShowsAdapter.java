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
package de.inselhome.tvrecorder.client.android.activities.tvguide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.inselhome.tvrecorder.common.objects.TvShow;
import de.inselhome.tvrecorder.common.utils.DateUtils;

import de.inselhome.tvrecorder.client.android.R;
import de.inselhome.tvrecorder.client.android.activities.tvshow.TvShowDetail;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvShowsAdapter extends ArrayAdapter<TvShow>
{
    protected TvShow[]  shows;
    protected boolean[] selection;


    public TvShowsAdapter(Context context, int resourceId) {
        super(context, resourceId);
    }


    public TvShowsAdapter(Context context, int resourceId, TvShow[] shows) {
        super(context, resourceId, shows);
        this.shows     = shows;
        this.selection = new boolean[shows.length];

        if (selection.length >= 1) {
            Arrays.fill(selection, 0, selection.length-1, false);
        }
    }


    public boolean[] getSelection() {
        return selection;
    }

    public void setSelection(int pos, boolean selected) {
        if (pos >= 0 && pos < selection.length) {
            selection[pos] = selected;
        }
    }


    public List<TvShow> getSelectedTvShows() {
        List<TvShow> selected = new ArrayList<TvShow>();

        for (int i = 0, len = selection.length; i < len; i++) {
            if (selection[i]) {
                selected.add(shows[i]);
            }
        }

        return selected;
    }


    public List<TvShow> getAllTvShows() {
        return Arrays.asList(shows);
    }


    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        View v = view;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = vi.inflate(R.layout.tvguide_list, null);
        }

        final TvShow o     = shows[pos];
        final int position = pos;

        if (o != null) {
            CheckBox     cb = (CheckBox) v.findViewById(R.id.checkbox);
            TextView     tt = (TextView) v.findViewById(R.id.timetext);
            TextView     bt = (TextView) v.findViewById(R.id.titletext);
            LinearLayout ll = (LinearLayout) v.findViewById(R.id.show);

            if (cb != null) {
                cb.setChecked(selection[pos]);
                cb.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        CheckBox tmp = (CheckBox) v;
                        setSelection(position, tmp.isChecked());
                    }
                });
            }

            if (tt != null) {
                tt.setText(
                    DateUtils.format(o.getStart(), DateUtils.DATETIME_FORMAT));
            }

            if (bt != null) {
                bt.setText(o.getTitle());
            }

            if (ll != null) {
                ll.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        showDetails(v.getContext(), o);
                    }
                });
            }
        }

        return v;
    }

    protected void showDetails(Context context, TvShow show) {
        Intent intent = new Intent(context, TvShowDetail.class);
        intent.putExtra(TvShowDetail.SHOW_TITLE, show.getTitle());
        intent.putExtra(TvShowDetail.SHOW_DESCRIPTION, show.getDescription());
        intent.putExtra(TvShowDetail.SHOW_CATEGORY, show.getCategory());
        intent.putExtra(TvShowDetail.SHOW_STARTDATE, show.getStart());
        intent.putExtra(TvShowDetail.SHOW_ENDDATE, show.getEnd());
        intent.putExtra(
            TvShowDetail.SHOW_LENGTH,
            String.valueOf(show.getLength()));

        context.startActivity(intent);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
