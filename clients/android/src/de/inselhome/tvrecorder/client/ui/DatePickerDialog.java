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
import android.widget.DatePicker;
import android.widget.LinearLayout;

import de.inselhome.tvrecorder.client.R;


/**
 * The DatePickerDialog is a special {@link Dialog} that allows users to select
 * a specific date.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class DatePickerDialog
extends      Dialog
{
    public interface OnDateSelectListener {
        public void onDateSelect(int year, int month, int day);
    } // end of OnSubmitListener interface


    /**
     * The {@link DatePicker} widget the is used to select the date.
     */
    protected DatePicker datepicker;

    /**
     * The {@link Button} to cancel the dialog.
     */
    protected Button cancel;

    /**
     * The {@link Button} to submit the dialog and fire an onDateSelect event.
     */
    protected Button submit;

    /**
     * An attribute to store an {@link OnDateSelectListener}.
     */
    protected OnDateSelectListener listener;


    /**
     * The default constructor to create new {@link DatePickerDialog} objects.
     *
     * @param context The {@link Context} where this widget will live in.
     */
    public DatePickerDialog(Context context) {
        super(context);

        datepicker = new DatePicker(context);
        cancel     = new Button(context);
        submit     = new Button(context);

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
        cancel.setText(R.string.datepicker_cancel);
        submit.setText(R.string.datepicker_ok);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDateSelect(
                        datepicker.getYear(),
                        datepicker.getMonth(),
                        datepicker.getDayOfMonth());
                }

                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancel();
            }
        });

        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        layout.addView(submit);
        layout.addView(cancel);

        view.addView(datepicker);
        view.addView(layout);

        return view;
    }


    /**
     * This method adds an {@link OnSubmitListener} to this dialog.
     *
     * @param listener An {@link OnSubmitListener}.
     */
    public void setOnDateSelectListener(OnDateSelectListener listener) {
        this.listener = listener;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
