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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.inselhome.tvrecorder.common.objects.TvShow;
import de.inselhome.tvrecorder.common.utils.DateUtils;

import de.inselhome.tvrecorder.client.R;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TvShowsAdapter extends ArrayAdapter<TvShow>
{
    protected TvShow[] shows;


    public TvShowsAdapter(Context context, int resourceId) {
        super(context, resourceId);
    }


    public TvShowsAdapter(Context context, int resourceId, TvShow[] shows) {
        super(context, resourceId, shows);
        this.shows = shows;
    }


    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        View v = view;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = vi.inflate(R.layout.tvguide_list, null);
        }

        TvShow o = shows[pos];

        if (o != null) {
            TextView tt = (TextView) v.findViewById(R.id.timetext);
            TextView bt = (TextView) v.findViewById(R.id.titletext);

            if (tt != null) {
                tt.setText(
                    DateUtils.format(o.getStart(), DateUtils.DATETIME_FORMAT));
            }

            if (bt != null) {
                bt.setText(o.getTitle());
            }
        }

        return v;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
