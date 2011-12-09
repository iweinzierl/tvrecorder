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

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.inselhome.tvrecorder.client.R;


/**
 * This {@link LinearLayout} displays a key value pair with a {@link Button} on
 * the right.
 *
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class KeyValueItem
extends      LinearLayout
{
    protected TextView label;
    protected TextView value;
    protected Button   change;


    /**
     * The default constructor.
     *
     * @param content The context
     * @param label The label describing the displayed value
     * @param value The value
     */
    public KeyValueItem(Context context, String label, String value) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);

        this.label  = new TextView(context);
        this.value  = new TextView(context);
        this.change = new Button(context);

        this.label.setText(label);
        this.value.setText(value);

        this.change.setText(R.string.keyvalueitem_change);
        this.label.setTextSize(20);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(this.label);
        layout.addView(this.value);

        addView(layout);
        addView(this.change);
    }


    public void setOnClickListener(View.OnClickListener listener) {
        this.change.setOnClickListener(listener);
    }


    /**
     * Call this method to set the text of the value field to <i>value</i>.
     *
     * @param value a new value for the text field.
     */
    public void setValue(String value) {
        this.value.setText(value);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
