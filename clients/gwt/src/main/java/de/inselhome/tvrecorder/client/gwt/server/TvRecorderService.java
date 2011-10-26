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
package de.inselhome.tvrecorder.client.gwt.server;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public abstract class TvRecorderService extends RemoteServiceServlet {

    private static Logger logger = Logger.getLogger(TvRecorderService.class);


    protected HttpClient getClient(String uri) {
        ServletContext sc = getServletContext();

        String host = sc.getInitParameter("tvrecorder-server-url");
        String user = sc.getInitParameter("user");
        String pass = sc.getInitParameter("password");

        logger.debug("Found host: " + host);
        logger.debug("Found user: " + user);
        logger.debug("Found pass: " + pass);

        String url = host.endsWith("/") ? host + uri : host + "/" + uri;

        return user != null && pass != null
            ? new HttpClient(url, user, pass)
            : new HttpClient(url);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
