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

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

import de.inselhome.tvrecorder.client.gwt.shared.model.Channel;
import de.inselhome.tvrecorder.client.gwt.shared.model.TvShow;


public class ChannelColumn extends VLayout {

    public static final String STYLE_TITLE = "channel-column-title";


    protected Channel channel;


    public ChannelColumn(Channel channel) {
        this.channel = channel;
        setPadding(3);
        setWidth(175);
        setAlign(Alignment.CENTER);

        Label title = new Label(channel.getName());
        title.setStyleName(STYLE_TITLE);
        title.setWidth("96%");
        title.setHeight(35);
        title.setLayoutAlign(Alignment.CENTER);

        addMember(title);

        for (TvShow tvshow: channel.getTvShows()) {
            addMember(new TvShowCell(tvshow));
        }
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
