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

import java.util.HashMap;
import java.util.Map;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;


public class TvRecorderModuleList extends HLayout {

    public static final String STYLE_NAME = "module-list";


    protected Canvas content;

    protected Map<String, AbstractModule> modules;


    public TvRecorderModuleList(Canvas content) {
        super();

        this.content = content;
        this.modules = new HashMap<String, AbstractModule>();

        setStyleName(STYLE_NAME);

        initLayout();
        addModules();
    }


    protected void initLayout() {
        setHeight(50);
        setMembersMargin(2);
        setPadding(2);
    }


    protected void addModules() {
        final AbstractModule manual = new ManualModule();
        manual.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                openModule(manual.getName());
            }
        });
        modules.put(manual.getName(), manual);
        addMember(manual);

        final AbstractModule tvGuide = new TvGuideModule();
        tvGuide.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                openModule(tvGuide.getName());
            }
        });
        modules.put(tvGuide.getName(), tvGuide);
        addMember(tvGuide);

        final AbstractModule tvSearch = new TvSearchModule();
        tvSearch.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                openModule(tvSearch.getName());
            }
        });
        modules.put(tvSearch.getName(), tvSearch);
        addMember(tvSearch);
    }


    public void openModule(String name) {
        AbstractModule module = modules.get(name);
        if (module == null) {
            SC.warn("Cannot open module: " + name);
            return;
        }

        removeCurrentModule();

        displayModule(module);
    }


    protected void removeCurrentModule() {
        Canvas[] children = content.getChildren();
        for (Canvas child: children) {
            content.removeChild(child);
        }
    }


    protected void displayModule(AbstractModule module) {
        content.addChild(module.doRender());
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
