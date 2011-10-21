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

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;

import de.inselhome.tvrecorder.client.gwt.client.TvRecorder;


public class TvSearchModule extends AbstractModule {

    public static final String NAME = "module.tvsearch";


    public TvSearchModule() {
        super(
            new Img(TvRecorder.IMG.moduleTvSearch().getURL()),
            TvRecorder.MSG.moduleTvSearch());
    }


    @Override
    public String getName() {
        return NAME;
    }


    @Override
    public Canvas doRender() {
        return new Label("HELLO TvSearch MODULE.");
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
