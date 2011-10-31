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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import de.inselhome.tvrecorder.client.gwt.shared.DateUtils;
import de.inselhome.tvrecorder.client.gwt.shared.model.TvShow;


public class TvShowCell extends AbstractCell<TvShow> {

    public static final String STYLE                = "tvshow-cell";
    public static final String STYLE_TITLE          = "tvshow-cell-title";
    public static final String STYLE_DETAILS        = "tvshow-cell-details";
    public static final String STYLE_DESC_BOX       = "tvshow-cell-desc-box";


    @Override
    public void render(Context context, TvShow value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }

        String startDate = DateUtils.format(
            value.getStart(),
            DateUtils.DATE_FORMAT);

        String startTime = DateUtils.format(
            value.getStart(),
            DateUtils.TIME_FORMAT);

        String endTime = DateUtils.format(
            value.getEnd(),
            DateUtils.TIME_FORMAT);

        // title row
        sb.appendHtmlConstant("<table width='100%' class='" + STYLE + "'>");
        sb.appendHtmlConstant("<tr>");
        sb.appendHtmlConstant("<td colspan='2'>");
        sb.appendHtmlConstant("<p class='" + STYLE_TITLE + "'>");
        sb.append(SafeHtmlUtils.fromString(value.getTitle()));
        sb.appendHtmlConstant("</p>");
        sb.appendHtmlConstant("</td>");
        sb.appendHtmlConstant("</tr>");

        // category and length row
        sb.appendHtmlConstant("<tr>");
        sb.appendHtmlConstant("<td>");
        sb.appendHtmlConstant("<p class='" + STYLE_DETAILS + "'>");
        sb.append(SafeHtmlUtils.fromString(value.getCategory()));
        sb.appendHtmlConstant("</p>");
        sb.appendHtmlConstant("</td>");
        sb.appendHtmlConstant("<td>");
        sb.appendHtmlConstant("<p class='" + STYLE_DETAILS + "'>");
        sb.append(SafeHtmlUtils.fromString(value.getLength() + "min"));
        sb.appendHtmlConstant("</p>");
        sb.appendHtmlConstant("</td>");
        sb.appendHtmlConstant("</tr>");

        // start and end time row
        sb.appendHtmlConstant("<tr>");
        sb.appendHtmlConstant("<td>");
        sb.appendHtmlConstant("<p class='" + STYLE_DETAILS + "'>");
        sb.append(SafeHtmlUtils.fromString(startDate));
        sb.appendHtmlConstant("</p>");
        sb.appendHtmlConstant("</td>");
        sb.appendHtmlConstant("<td>");
        sb.appendHtmlConstant("<p class='" + STYLE_DETAILS + "'>");
        sb.append(SafeHtmlUtils.fromString(startTime + " - " + endTime));
        sb.appendHtmlConstant("</p>");
        sb.appendHtmlConstant("</td>");
        sb.appendHtmlConstant("</tr>");

        // description row
        sb.appendHtmlConstant("<tr>");
        sb.appendHtmlConstant("<td colspan='2'>");
        sb.appendHtmlConstant("<p class='" + STYLE_DESC_BOX + "'>");
        sb.append(SafeHtmlUtils.fromString(value.getDescription()));
        sb.appendHtmlConstant("</p>");
        sb.appendHtmlConstant("</td>");
        sb.appendHtmlConstant("</tr>");
        sb.appendHtmlConstant("</table>");
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
