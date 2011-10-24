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
package de.inselhome.tvrecorder.client.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

import de.inselhome.tvrecorder.client.gwt.client.modules.TvRecorderModuleList;
import de.inselhome.tvrecorder.client.gwt.client.services.TvGuideService;
import de.inselhome.tvrecorder.client.gwt.client.services.TvGuideServiceAsync;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TvRecorder implements EntryPoint {

    public static final String STYLE_NAME_CONTENT = "content";
    public static final String STYLE_NAME_HEADER  = "header";
    public static final String STYLE_NAME_ROOT    = "root";

    public static final TvRecorderConstants MSG =
        GWT.create(TvRecorderConstants.class);

    public static final TvRecorderImages IMG =
        GWT.create(TvRecorderImages.class);

    public static final TvGuideServiceAsync TVGUIDE =
        GWT.create(TvGuideService.class);


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        Layout body = new Layout();
        body.setWidth100();
        body.setHeight100();
        body.setAlign(Alignment.CENTER);
        body.setAlign(VerticalAlignment.CENTER);

        Canvas spacer = new Canvas();
        spacer.setHeight(50);

        Canvas content = new Canvas();
        content.setStyleName(STYLE_NAME_CONTENT);
        content.setHeight("*");

        VLayout rootLayout = new VLayout();
        rootLayout.setStyleName(STYLE_NAME_ROOT);
        rootLayout.setHeight("90%");
        rootLayout.setWidth(600);
        rootLayout.setMembersMargin(5);

        Label title = new Label("TvRecorder GWT Client");
        title.setHeight(50);

        rootLayout.addMember(spacer);
        rootLayout.addMember(title);
        rootLayout.addMember(new TvRecorderModuleList(content));
        rootLayout.addMember(content);

        body.addMember(rootLayout);
        body.draw();
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
