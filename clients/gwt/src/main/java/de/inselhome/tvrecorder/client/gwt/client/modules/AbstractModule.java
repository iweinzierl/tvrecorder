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
package de.inselhome.tvrecorder.client.gwt.client.modules;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.layout.VLayout;

import de.inselhome.tvrecorder.client.gwt.client.widgets.Stateful;


public abstract class AbstractModule extends VLayout implements Stateful {

    public static final String STYLE            = "abstract-module";
    public static final String STYLE_SELECTED   = "abstract-module-selected";
    public static final String STYLE_NAME_LABEL = "module-label";
    public static final String STYLE_NAME_IMAGE = "module-image";


    protected Img image;

    protected String label;

    protected boolean selected;


    public AbstractModule(Img image, String label) {
        super();

        this.image = image;
        this.label = label;

        initLayout();
    }


    protected void initLayout() {
        setAlign(Alignment.CENTER);
        setAlign(VerticalAlignment.CENTER);
        setHeight(90);
        setWidth(90);

        addMember(getLabel());
        addMember(getImage());

        setStyleName(STYLE);
    }


    @Override
    public boolean isSelected() {
        return selected;
    }


    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        onSelectionChanged();
    }


    protected void onSelectionChanged() {
        if (selected) {
            setStyleName(STYLE_SELECTED);
        }
        else {
            setStyleName(STYLE);
        }
    }


    protected Img getImage() {
        image.setImageType(ImageStyle.CENTER);
        image.addStyleName(getImageStyleName());
        image.setHeight(60);
        return image;
    }


    protected Canvas getLabel() {
        Label label = new Label(this.label);
        label.addStyleName(getLabelStyleName());
        label.setAlign(Alignment.CENTER);
        label.setHeight(25);

        return label;
    }


    protected String getLabelStyleName() {
        return STYLE_NAME_LABEL;
    }


    protected String getImageStyleName() {
        return STYLE_NAME_IMAGE;
    }


    public abstract Canvas doRender();

    public abstract String getName();
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
