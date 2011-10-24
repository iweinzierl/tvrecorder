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
package de.inselhome.tvrecorder.client.gwt.client.widgets;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseMoveEvent;
import com.smartgwt.client.widgets.events.MouseMoveHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import de.inselhome.tvrecorder.client.gwt.shared.model.TvShow;


public class TvShowCell extends VLayout {

    public static final String STYLE                = "tvshow-cell";
    public static final String STYLE_SELECTED       = "tvshow-cell-selected";
    public static final String STYLE_HOVER          = "tvshow-cell-hover";
    public static final String STYLE_HOVER_SELECTED = "tvshow-cell-hover-sel";
    public static final String STYLE_TITLE_ROW      = "tvshow-cell-title-row";
    public static final String STYLE_TITLE          = "tvshow-cell-title";
    public static final String STYLE_DETAILS        = "tvshow-cell-details";
    public static final String STYLE_DESC_BOX       = "tvshow-cell-desc-box";


    protected boolean selected;


    public TvShowCell(TvShow tvshow) {
        this.selected = false;

        setStyleName(STYLE);
        setWidth100();
        setPadding(3);
        setMargin(3);

        addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {
                if (isSelected()) {
                    TvShowCell.this.setStyleName(STYLE_HOVER_SELECTED);
                }
                else {
                    TvShowCell.this.setStyleName(STYLE_HOVER);
                }
            }
        });

        addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                if (isSelected()) {
                    TvShowCell.this.setStyleName(STYLE_SELECTED);
                }
                else {
                    TvShowCell.this.setStyleName(STYLE);
                }
            }
        });

        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent e) {
                setSelected(!isSelected());
            }
        });

        addMember(buildTitleCell(tvshow));
        addMember(buildDescriptionRow(tvshow));
    }


    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

        onSelectionChanged();
    }


    @Override
    public boolean isSelected() {
        return selected;
    }


    protected void onSelectionChanged() {
        if (isSelected()) {
            setStyleName(STYLE_SELECTED);
        }
        else {
            setStyleName(STYLE);
        }
    }


    protected Canvas buildTitleCell(TvShow tvshow) {
        VLayout cell = new VLayout();
        cell.setStyleName(STYLE_TITLE_ROW);
        cell.setWidth100();

        cell.addMember(buildTitleRow(tvshow));
        cell.addMember(buildDetailsRow(tvshow));
        cell.addMember(buildTimeRow(tvshow));

        return cell;
    }


    protected Canvas buildTitleRow(TvShow tvshow) {
        Label label = new Label(tvshow.getTitle());
        label.setHeight(25);
        label.setWidth100();
        label.setStyleName(STYLE_TITLE);

        return label;
    }


    protected Canvas buildDetailsRow(TvShow tvshow) {
        HLayout detail = new HLayout();
        detail.setHeight(15);
        detail.setWidth100();

        Label category = new Label(tvshow.getCategory());
        category.setHeight(15);
        category.setWidth("50%");
        category.setStyleName(STYLE_DETAILS);

        Label length = new Label(String.valueOf(tvshow.getLength()) + " min");
        length.setHeight(15);
        length.setWidth("50%");
        length.setStyleName(STYLE_DETAILS);

        detail.addMember(category);
        detail.addMember(length);

        return detail;
    }


    protected Canvas buildTimeRow(TvShow tvshow) {
        HLayout time = new HLayout();
        time.setHeight(15);
        time.setWidth100();

        Label start = new Label("-TODO start-");
        start.setStyleName(STYLE_DETAILS);
        start.setWidth("50%");
        start.setHeight(15);

        Label end = new Label("-TODO end-");
        end.setStyleName(STYLE_DETAILS);
        end.setWidth("50%");
        end.setHeight(15);

        time.addMember(start);
        time.addMember(end);

        return time;
    }


    protected Canvas buildDescriptionRow(TvShow tvshow) {
        HLayout layout = new HLayout();
        layout.setWidth100();
        layout.setStyleName(STYLE_DESC_BOX);

        Label label = new Label(tvshow.getDescription());
        label.setValign(VerticalAlignment.TOP);

        layout.addMember(label);

        return layout;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
