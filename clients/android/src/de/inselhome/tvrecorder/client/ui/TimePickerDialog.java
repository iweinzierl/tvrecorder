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

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.LinearLayout;

import de.inselhome.tvrecorder.client.R;


/**
 * The DatePickerDialog is a special {@link Dialog} that allows users to select
 * a specific date.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TimePickerDialog
extends      Dialog
{
    public interface OnTimeSelectListener {
        public void onTimeSelect(int hours, int minutes);
    } // end of OnTimeSelectListener interface
    /**
     * The {@link TimePicker} widget the is used to select a time.
     */
    protected TimePicker timepicker;

    /**
     * The {@link Button} to cancel the dialog.
     */
    protected Button cancel;

    /**
     * The {@link Button} to submit the dialog and fire an onSubmit event.
     */
    protected Button submit;

    /**
     * A boolean property to define, if the {@link TimePicker} is in 24 hours
     * mode.
     */
    protected boolean is24Hours;

    /**
     * An attribute to store an {@link OnTimeSelectListener}.
     */
    protected OnTimeSelectListener listener;


    /**
     * The default constructor to create new {@link TimePickerDialog} objects.
     *
     * @param context The {@link Context} where this widget will live in.
     */
    public TimePickerDialog(Context context, boolean is24Hours) {
        super(context);

        timepicker = new TimePicker(context);
        cancel     = new Button(context);
        submit     = new Button(context);
        is24Hours  = is24Hours;

        setContentView(createContentView(context));
    }


    /**
     * This method creates the content of the widget and initializes the
     * listeners for the cancel and submit button.
     *
     * @param context The context where this widget lives in.
     *
     * @return the content.
     */
    protected View createContentView(Context context) {
        cancel.setText(R.string.timepicker_cancel);
        submit.setText(R.string.timepicker_ok);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTimeSelect(
                        timepicker.getCurrentHour(),
                        timepicker.getCurrentMinute());
                }

                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancel();
            }
        });

        timepicker.setIs24HourView(is24Hours);

        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        layout.addView(submit);
        layout.addView(cancel);

        view.addView(timepicker);
        view.addView(layout);

        return view;
    }


    /**
     * This method adds an {@link OnSubmitListener} to this dialog.
     *
     * @param listener An {@link OnSubmitListener}.
     */
    public void setOnTimeSelectListener(OnTimeSelectListener listener) {
        this.listener = listener;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
