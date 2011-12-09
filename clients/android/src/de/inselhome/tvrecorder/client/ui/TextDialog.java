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
package de.inselhome.tvrecorder.client.android.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;


/**
 * This {@link Dialog} shows a text field and a button.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class TextDialog
extends      Dialog
{
    /**
     * This interface describes a method {@link onOk(String)} that is called by
     * {@link TextDialog} after the ok {@link Button} has been clicked so submit
     * the dialog.
     */
    public interface OnOkListener {
        public void onOk(String text);
    }

    protected OnOkListener listener;
    protected EditText     edit;


    /**
     * This constructor creates a new {@link TextDialog} with <i>title</i> as
     * title.
     *
     * @param content the context
     * @param title the title of this dialog.
     */
    public TextDialog(Context context, String title) {
        super(context);
        setTitle(title);
        setCancelable(true);
        edit = new EditText(context);

        LinearLayout vertical = new LinearLayout(context);
        vertical.setOrientation(LinearLayout.VERTICAL);

        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setOrientation(LinearLayout.HORIZONTAL);

        Button ok     = new Button(context);
        Button cancel = new Button(context);

        ok.setText("Ok");
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                listener.onOk(edit.getText().toString());

                dismiss();
            }
        });

        cancel.setText("Cancel");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextDialog.this.cancel();
            }
        });

        horizontal.addView(ok);
        horizontal.addView(cancel);

        vertical.addView(edit);
        vertical.addView(horizontal);

        setContentView(vertical);
    }


    /**
     * Creates a new {@link TextDialog} with title <i>title></i> and a preset
     * default value.
     *
     * @param context the context
     * @param title the title of this dialog.
     * @param defaultValue a preset value for the text field.
     */
    public TextDialog(Context context, String title, String defaultValue) {
        this(context, title);
        edit.setText(defaultValue);
    }


    /**
     * This method might be used to register a new OnOkListener listening on
     * submitting this dialog with the OK button.
     *
     * @param listener the new {@link OnOkListener}
     */
    public void setOnOkListener(OnOkListener listener) {
        this.listener = listener;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
