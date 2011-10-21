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

import java.util.List;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import de.inselhome.tvrecorder.client.gwt.client.TvRecorder;
import de.inselhome.tvrecorder.client.gwt.client.widgets.TButton;
import de.inselhome.tvrecorder.client.gwt.shared.TvGuideFaker;
import de.inselhome.tvrecorder.client.gwt.shared.model.Channel;


public class ManualModule extends AbstractModule {

    public static final String NAME = "module.manual";


    protected TextBox startDate;
    protected TextBox startTime;

    protected TextBox endDate;
    protected TextBox endTime;

    protected TextBox outputName;

    protected ListBox channelList;


    protected List<Channel> channels;



    public ManualModule() {
        super(
            new Img(TvRecorder.IMG.moduleManual().getURL()),
            TvRecorder.MSG.moduleManual());

        // XXX Remove this line when REST services are ready.
        this.channels = TvGuideFaker.getChannels(false);
    }


    @Override
    public String getName() {
        return NAME;
    }


    @Override
    public Canvas doRender() {
        VLayout layout = new VLayout();
        layout.setMembersMargin(5);
        layout.setPadding(10);

        layout.addMember(buildStartDate());
        layout.addMember(buildEndDate());
        layout.addMember(buildName());
        layout.addMember(buildChannel());
        layout.addMember(buildButtons());

        return layout;
    }


    public Canvas buildStartDate() {
        HLayout layout = buildRowLayout(0);

        startDate = new TextBox();
        startTime = new TextBox();

        startDate.setWidth("100px");
        startTime.setWidth("100px");

        layout.addMember(buildLabel("Start"));
        layout.addMember(startDate);
        layout.addMember(startTime);

        return layout;
    }


    public Canvas buildEndDate() {
        HLayout layout = buildRowLayout(1);

        endDate = new TextBox();
        endTime = new TextBox();

        endDate.setWidth("100px");
        endTime.setWidth("100px");

        layout.addMember(buildLabel("Ende"));
        layout.addMember(endDate);
        layout.addMember(endTime);

        return layout;
    }


    public Canvas buildName() {
        HLayout layout = buildRowLayout(2);

        outputName = new TextBox();
        outputName.setWidth("220px");

        layout.addMember(buildLabel("Ausgabename"));
        layout.addMember(outputName);

        return layout;
    }


    protected Canvas buildChannel() {
        HLayout layout = buildRowLayout(3);

        channelList = new ListBox(false);

        for (Channel channel: channels) {
            channelList.addItem(channel.getName(), channel.getName());
        }

        layout.addMember(buildLabel("Sender"));
        layout.addMember(channelList);

        return layout;
    }


    protected HLayout buildButtons() {
        HLayout layout = buildRowLayout(4);
        layout.setAlign(Alignment.LEFT);

        TButton doRecord = new TButton("Aufnehmen");
        TButton doReset  = new TButton("Zurücksetzen");

        layout.addMember(doRecord);
        layout.addMember(doReset);

        return layout;
    }


    protected HLayout buildRowLayout(int row) {
        HLayout layout = new HLayout();
        layout.setMembersMargin(10);
        layout.setHeight(getRowHeight(row));
        layout.setAlign(Alignment.CENTER);
        layout.setAlign(VerticalAlignment.CENTER);

        return layout;
    }


    protected Label buildLabel(String text) {
        Label label = new Label(text);
        label.setWidth(getColWidth(0));
        label.setHeight100();

        return label;
    }


    protected static int getColWidth(int col) {
        switch (col) {
            case 0: return 100;
            case 1: return 350;
            default: return 0;
        }
    }


    protected static int getRowHeight(int row) {
        switch (row) {
            case 0: return 35;
            case 1: return 35;
            case 2: return 35;
            case 3: return 35;
            case 4: return 35;
            default: return 35;
        }
    }

}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
