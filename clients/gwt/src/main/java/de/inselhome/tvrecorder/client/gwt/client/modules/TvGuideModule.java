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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.MultiSelectionModel;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import de.inselhome.tvrecorder.client.gwt.client.TvRecorder;
import de.inselhome.tvrecorder.client.gwt.client.widgets.TvShowCell;
import de.inselhome.tvrecorder.client.gwt.shared.model.Channel;
import de.inselhome.tvrecorder.client.gwt.shared.model.TvShow;


public class TvGuideModule extends AbstractModule {

    public static final String NAME = "module.tvguide";

    public static final String STYLE_CHANNEL_TITLE = "channel-column-title";


    protected List<Channel> channels;

    protected HLayout layout;


    public TvGuideModule() {
        super(
            new Img(TvRecorder.IMG.moduleTvGuide().getURL()),
            TvRecorder.MSG.moduleTvGuide());
    }


    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }


    @Override
    public String getName() {
        return NAME;
    }


    @Override
    public Canvas doRender() {
        if (layout == null) {
            updateTvGuide();

            layout = new HLayout();
            layout.setWidth100();
            layout.setHeight100();
            layout.setOverflow(Overflow.AUTO);
            layout.setMembersMargin(5);
        }

        return layout;
    }


    protected void updateUI() {
        TvShowCell empty = new TvShowCell();
        for (Channel channel: channels) {
            VLayout vertical = new VLayout();
            vertical.setWidth100();

            CellList<TvShow> list = new CellList<TvShow>(empty);
            list.setRowData(0, channel.getTvShows());
            list.setSelectionModel(new MultiSelectionModel());
            list.setWidth("175px");

            Label channelName = new Label(channel.getName());
            channelName.setWidth("175px");
            channelName.setHeight("35px");
            channelName.setStyleName(STYLE_CHANNEL_TITLE);

            vertical.addMember(channelName);
            vertical.addMember(list);

            layout.addMember(vertical);
        }
    }


    protected void updateTvGuide() {
        TvRecorder.TVGUIDE.getTvGuide(new AsyncCallback<List<Channel>>() {
            @Override
            public void onFailure(Throwable caught) {
                SC.warn("Error while fetching TvGuide: " + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Channel> channels) {
                setChannels(channels);
                updateUI();
            }
        });
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
