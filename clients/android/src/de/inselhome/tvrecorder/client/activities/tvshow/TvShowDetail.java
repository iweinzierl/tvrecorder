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
package de.inselhome.tvrecorder.client.activities.tvshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import de.inselhome.tvrecorder.client.R;


public class TvShowDetail extends Activity {

    public static final String SHOW_TITLE       = "activities.tvshow.title";
    public static final String SHOW_DESCRIPTION = "activities.tvshow.desc";
    public static final String SHOW_STARTDATE   = "activities.tvshow.start";


    protected String title;
    protected String description;
    protected String imgurl;


    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        Log.d("TvR [TvShowDetail]", "onCreate()");

        setContentView(R.layout.tvshowdetail);

        Intent i    = getIntent();
        title       = i.getStringExtra(SHOW_TITLE);
        description = i.getStringExtra(SHOW_DESCRIPTION);

        initLayout();
    }


    protected void initLayout() {
        TextView title       = (TextView) findViewById(R.id.title);
        TextView description = (TextView) findViewById(R.id.description);
        TextView date        = (TextView) findViewById(R.id.date);

        title.setText(this.title);
        description.setText(Html.fromHtml(this.description));
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
