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
package de.inselhome.tvrecorder.client.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

import de.inselhome.tvrecorder.common.objects.ChannelWithTvGuide;
import de.inselhome.tvrecorder.common.objects.TvShow;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class ChannelSQLiteHelper {

    protected Context context;

    protected SQLiteProvider sqlite;
    protected SQLiteDatabase db;


    public ChannelSQLiteHelper(Context context) {
        this.context = context;
    }


    protected SQLiteProvider getDatabase() {
        if (sqlite == null) {
            sqlite = new SQLiteProvider(context);
        }

        return sqlite;
    }


    public void clean() {
        if (db == null) {
            db = getDatabase().getWritableDatabase();
        }

        db.delete(SQLiteProvider.CHANNEL_TBL_NAME, null, null);
        db.delete(SQLiteProvider.TVSHOW_TBL_NAME, null, null);

        db.close();
        db = null;
    }


    public void insert(ChannelWithTvGuide c) {
        if (db == null) {
            db = getDatabase().getWritableDatabase();
            db.beginTransaction();
        }

        ContentValues values = new ContentValues();
        values.putNull(SQLiteProvider.CHANNEL_TBL_COLUMN_ID);
        values.put(SQLiteProvider.CHANNEL_TBL_COLUMN_KEY, c.getKey());
        values.put(SQLiteProvider.CHANNEL_TBL_COLUMN_TITLE, c.getDescription());

        try {
            long id = db.insert(SQLiteProvider.CHANNEL_TBL_NAME, null, values);

            Collection<TvShow> shows = c.getSortedListing();
            for (TvShow show: shows) {
                insert(id, show);
            }
        }
        catch (SQLException se) {
            Log.d("TvR [ChannelSQLiteHelper]", se.getMessage());
        }
    }


    protected void insert(long id, TvShow show) {
        ContentValues values = new ContentValues();
        values.putNull(SQLiteProvider.TVSHOW_TBL_COLUMN_ID);
        values.put(
            SQLiteProvider.TVSHOW_TBL_COLUMN_CHANNEL_ID,
            id);
        values.put(
            SQLiteProvider.TVSHOW_TBL_COLUMN_TITLE,
            show.getTitle());
        values.put(
            SQLiteProvider.TVSHOW_TBL_COLUMN_DESCRIPTION,
            show.getDescription());
        values.put(
            SQLiteProvider.TVSHOW_TBL_COLUMN_START,
            show.getStart().getTime());
        values.put(
            SQLiteProvider.TVSHOW_TBL_COLUMN_END,
            show.getEnd().getTime());

        db.insert(SQLiteProvider.TVSHOW_TBL_NAME, null, values);
    }


    public List<ChannelWithTvGuide> getAll() {
        if (db == null) {
            db = getDatabase().getReadableDatabase();
        }

        Cursor cursor = db.query(
            SQLiteProvider.CHANNEL_TBL_NAME,
            new String[] {
                SQLiteProvider.CHANNEL_TBL_COLUMN_KEY,
                SQLiteProvider.CHANNEL_TBL_COLUMN_TITLE,
                SQLiteProvider.CHANNEL_TBL_COLUMN_ID },
            null,
            null,
            null,
            null,
            null);

        List<ChannelWithTvGuide> channels =
            new ArrayList<ChannelWithTvGuide>(cursor.getCount());

        cursor.moveToPosition(-1);

        while (cursor.moveToNext()) {
            ChannelWithTvGuide c = new ChannelWithTvGuide(
                cursor.getString(0),
                cursor.getString(1));
            getAllTvShows(cursor.getLong(2), c);

            channels.add(c);
        }

        cursor.close();
        db.close();
        db = null;

        return channels;
    }


    protected void getAllTvShows(long id, ChannelWithTvGuide c) {
        if (db == null) {
            db = getDatabase().getReadableDatabase();
        }

        Cursor cursor = db.query(
            SQLiteProvider.TVSHOW_TBL_NAME,
            new String[] {
                SQLiteProvider.TVSHOW_TBL_COLUMN_TITLE,
                SQLiteProvider.TVSHOW_TBL_COLUMN_DESCRIPTION,
                SQLiteProvider.TVSHOW_TBL_COLUMN_START,
                SQLiteProvider.TVSHOW_TBL_COLUMN_END,
                SQLiteProvider.TVSHOW_TBL_COLUMN_IMG_URL },
            SQLiteProvider.TVSHOW_TBL_COLUMN_CHANNEL_ID + " = ?"
                + " AND " + SQLiteProvider.TVSHOW_TBL_COLUMN_END
                + " > " + System.currentTimeMillis(),
            new String[] { String.valueOf(id) },
            null,
            null,
            null);

        cursor.moveToPosition(-1);

        while (cursor.moveToNext()) {
            c.addTvShow(new TvShow(
                cursor.getString(0),
                cursor.getString(1),
                new Date(cursor.getLong(2)),
                new Date(cursor.getLong(3))));
        }

        cursor.close();
    }


    public void setLastUpdate(long now) {
        // TODO Implement me
    }


    public boolean needsUpdate() {
        return false;
    }


    public void doIt() {
        if (db != null) {
            Log.d("TvR [ChannelSQLiteHelper]", "End transaction.");

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            db = null;
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
