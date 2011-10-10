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

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.ImageView;

import de.inselhome.tvrecorder.common.utils.DateUtils;

import de.inselhome.tvrecorder.client.R;


public class TvShowDetail extends Activity {

    public static final String SHOW_TITLE       = "activities.tvshow.title";
    public static final String SHOW_DESCRIPTION = "activities.tvshow.desc";
    public static final String SHOW_STARTDATE   = "activities.tvshow.start";
    public static final String SHOW_ENDDATE     = "activities.tvshow.end";
    public static final String SHOW_CATEGORY    = "activities.tvshow.category";
    public static final String SHOW_LENGTH      = "activities.tvshow.length";


    protected String title;
    protected String description;
    protected String imgurl;
    protected String category;
    protected String length;
    protected String image;
    protected Date   start;
    protected Date   end;


    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        Log.d("TvR [TvShowDetail]", "onCreate()");

        setContentView(R.layout.tvshowdetail);

        Intent i    = getIntent();
        title       = i.getStringExtra(SHOW_TITLE);
        description = i.getStringExtra(SHOW_DESCRIPTION);
        category    = i.getStringExtra(SHOW_CATEGORY);
        length      = i.getStringExtra(SHOW_LENGTH);
        start       = (Date) i.getSerializableExtra(SHOW_STARTDATE);
        end         = (Date) i.getSerializableExtra(SHOW_ENDDATE);

        initLayout();
    }


    protected void initLayout() {
        TextView title       = (TextView) findViewById(R.id.title);
        TextView description = (TextView) findViewById(R.id.description);
        TextView date        = (TextView) findViewById(R.id.date);
        TextView category    = (TextView) findViewById(R.id.category);
        TextView length      = (TextView) findViewById(R.id.length);
        ImageView image      = (ImageView) findViewById(R.id.image);

        title.setText(this.title);
        description.setText(Html.fromHtml(this.description));
        category.setText(this.category);
        length.setText(this.length + " min");
        date.setText(getDateString());

        if (this.image == null || this.image.length() == 0) {
            image.setImageResource(R.drawable.tvdetail_no_image);
        }
    }


    protected String getDateString() {
        String day      = DateUtils.format(start, DateUtils.DAY_DATE_FORMAT);
        String startStr = DateUtils.format(start, DateUtils.TIME_FORMAT);
        String endStr   = DateUtils.format(end, DateUtils.TIME_FORMAT);

        Resources resources = getResources();

        return
            day + " | " +
            startStr + " - " + endStr + " " +
            resources.getString(R.string.tvshowdetail_datesuffix);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
