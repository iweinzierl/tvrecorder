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

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.inselhome.tvrecorder.client.utils.DateUtils;


/**
 * This widget contains two {@link TextView} objects that display a date and a
 * time. The user can interact with this widget by touching it. After a touch
 * event is fired, a dialog opens to change the date and time.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class DateTimeWidget
extends      LinearLayout
{
    /**
     * The content of the widget.
     */
    protected Context  context;

    /**
     * The gui component that displays the selected date.
     */
    protected TextView date;

    /**
     * The gui component that displays the selected time.
     */
    protected TextView time;

    /**
     * The object to store the selected date/time.
     */
    protected Calendar datetime;


    /**
     * The default constructor to create a new DateTimeWidget with the current
     * time as the selected date/time.
     *
     * @param The widgets context.
     */
    public DateTimeWidget(Context context) {
        super(context);
        this.context = context;

        setOrientation(LinearLayout.HORIZONTAL);

        datetime = new GregorianCalendar();

        date = new TextView(context);
        time = new TextView(context);

        date.setPadding(10, 0, 20, 0);
        date.setTextSize(30);
        time.setTextSize(30);

        updateView();

        addView(date);
        addView(time);
    }


    /**
     * This method might be called to update the text displaying the date and
     * time.
     */
    public void updateView() {
        date.setText(DateUtils.formatDate(datetime));
        time.setText(DateUtils.formatTime(datetime));
    }


    /**
     * This method retrieves the selected timestamp.
     *
     * @return a {@link Calendar} object that represents the currently selected
     * timestamp.
     */
    public Calendar getDatetime() {
        return datetime;
    }


    /**
     * This method is called when this widget receives a MotionEvent. After this
     * method is called, a dialog to change the selected date and time is
     * opened.
     *
     * @param e the MotionEvent
     *
     * @return true, if the event was handled.
     */
    public boolean onTouchEvent(MotionEvent e) {
        int year  = datetime.getTime().getYear();
        int month = datetime.getTime().getMonth();
        int day   = datetime.getTime().getDay();

        Log.d("TvR [DateTimeWidget]", "onTouchEvent() - show DateTimePicker.");
        // TODO Open a dialog that contains a DatePicker to choose the date

        return true;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
