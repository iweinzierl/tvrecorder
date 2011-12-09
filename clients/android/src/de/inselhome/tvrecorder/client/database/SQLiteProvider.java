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

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class SQLiteProvider extends SQLiteOpenHelper {

    public static final String TAG = "TvR [SQLiteProvider]";

    public static final String DATABASE_NAME    = "tvrecorder.db";
    public static final int    DATABASE_VERSION = 2;

    public static final String CHANNEL_TBL_NAME = "channels";
    public static final String CHANNEL_TBL_COLUMN_ID = "id";
    public static final String CHANNEL_TBL_COLUMN_KEY = "key";
    public static final String CHANNEL_TBL_COLUMN_TITLE = "title";

    public static final String UPDATE_TBL_NAME = "update_times";
    public static final String UPDATE_TBL_COLUMN_ID = "id";
    public static final String UPDATE_TBL_COLUMN_TBL = "tbl_name";
    public static final String UPDATE_TBL_COLUMN_TIMESTAMP = "timestamp";

    public static final String TVSHOW_TBL_NAME = "tvshows";
    public static final String TVSHOW_TBL_COLUMN_ID = "id";
    public static final String TVSHOW_TBL_COLUMN_CHANNEL_ID = "channel_id";
    public static final String TVSHOW_TBL_COLUMN_TITLE = "title";
    public static final String TVSHOW_TBL_COLUMN_DESCRIPTION = "desc";
    public static final String TVSHOW_TBL_COLUMN_CATEGORY = "category";
    public static final String TVSHOW_TBL_COLUMN_LENGTH = "length";
    public static final String TVSHOW_TBL_COLUMN_IMG_URL = "img_url";
    public static final String TVSHOW_TBL_COLUMN_START = "start";
    public static final String TVSHOW_TBL_COLUMN_END = "end";


    public SQLiteProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createUpdates(db);
        createChannels(db);
        createTvShows(db);
    }


    protected void createUpdates(SQLiteDatabase db) {
        Log.d(TAG, "Create table " + UPDATE_TBL_NAME);
        db.execSQL("CREATE TABLE " + UPDATE_TBL_NAME + " ("
            + UPDATE_TBL_COLUMN_ID        + " INTEGER PRIMARY KEY,"
            + UPDATE_TBL_COLUMN_TBL       + " TEXT UNIQUE NOT NULL,"
            + UPDATE_TBL_COLUMN_TIMESTAMP + " INTEGER NOT NULL"
            + ");");
    }


    protected void createChannels(SQLiteDatabase db) {
        Log.d(TAG, "Create table " + CHANNEL_TBL_NAME);
        db.execSQL("CREATE TABLE " + CHANNEL_TBL_NAME + " ("
            + CHANNEL_TBL_COLUMN_ID     + " INTEGER PRIMARY KEY,"
            + CHANNEL_TBL_COLUMN_KEY    + " TEXT UNIQUE NOT NULL,"
            + CHANNEL_TBL_COLUMN_TITLE  + " TEXT UNIQUE NOT NULL"
            + ");");

        db.execSQL("INSERT INTO " + UPDATE_TBL_NAME + " ("
            + UPDATE_TBL_COLUMN_TBL + "," + UPDATE_TBL_COLUMN_TIMESTAMP + ") "
            + "VALUES ('" + CHANNEL_TBL_NAME + "', strftime('%s','now'));");

        createUpdateTrigger(db, "update_channels_trigger", CHANNEL_TBL_NAME);
    }


    protected void createTvShows(SQLiteDatabase db) {
        Log.d(TAG, "Create table " + TVSHOW_TBL_NAME);
        db.execSQL("CREATE TABLE " + TVSHOW_TBL_NAME + " ("
            + TVSHOW_TBL_COLUMN_ID          + " INTEGER PRIMARY KEY,"
            + TVSHOW_TBL_COLUMN_CHANNEL_ID  + " INTEGER,"
            + TVSHOW_TBL_COLUMN_TITLE       + " TEXT,"
            + TVSHOW_TBL_COLUMN_DESCRIPTION + " TEXT,"
            + TVSHOW_TBL_COLUMN_CATEGORY    + " TEXT,"
            + TVSHOW_TBL_COLUMN_LENGTH      + " INTEGER,"
            + TVSHOW_TBL_COLUMN_IMG_URL     + " TEXT,"
            + TVSHOW_TBL_COLUMN_START       + " INTEGER,"
            + TVSHOW_TBL_COLUMN_END         + " INTEGER,"
            + "FOREIGN KEY(" + TVSHOW_TBL_COLUMN_CHANNEL_ID + ")"
            + "REFERENCES " + CHANNEL_TBL_NAME + "("+ CHANNEL_TBL_COLUMN_ID +")"
            + ");");

        db.execSQL("INSERT INTO " + UPDATE_TBL_NAME + " ("
            + UPDATE_TBL_COLUMN_TBL + "," + UPDATE_TBL_COLUMN_TIMESTAMP + ") "
            + "VALUES ('" + TVSHOW_TBL_NAME + "', strftime('%s','now'));");

        createUpdateTrigger(db, "update_tvshow_trigger", TVSHOW_TBL_NAME);
    }


    protected void createUpdateTrigger(
        SQLiteDatabase db,
        String         name,
        String         srcTable
    ) {
        db.execSQL("CREATE TRIGGER " + name + " INSERT "
            + " ON " + srcTable + " "
            + "BEGIN "
            + "UPDATE " + UPDATE_TBL_NAME + " SET "
            + UPDATE_TBL_COLUMN_TIMESTAMP + " = strftime('%s','now') "
            + "WHERE " + UPDATE_TBL_COLUMN_TBL + " = '" + srcTable +"';"
            + "END;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(
            TAG,
            "Upgrading database from version " + oldVersion + " to " +
            newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + CHANNEL_TBL_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TVSHOW_TBL_NAME);

        onCreate(db);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
