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

import java.io.IOException;

import org.apache.log4j.Logger;

import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;


public class HttpClient {

    public interface ResponseHandler {
        Object handle(Response response) throws IOException;
    }


    private static final Logger logger = Logger.getLogger(HttpClient.class);

    protected String url;
    protected String user;
    protected String pass;


    public HttpClient(String url) {
        this.url = url;
    }


    public HttpClient(String url, String user, String pass) {
        this(url);
        this.user = user;
        this.pass = pass;
    }


    public String getUrl() {
        return url;
    }


    protected Client getClient() {
        return new Client(Protocol.HTTP);
    }


    protected Request getRequest(Method method) {
        Request request = new Request(method, getUrl());

        if (user != null && pass != null) {
            request.setChallengeResponse(
                new ChallengeResponse(ChallengeScheme.HTTP_BASIC, user, pass));
        }

        return request;
    }


    protected Response doGet()
    throws    IOException
    {
        Client  client  = getClient();
        Request request = getRequest(Method.GET);

        long start = System.currentTimeMillis();

        Response response = client.handle(request);
        Status   status   = response.getStatus();

        if (status.getCode() != 200) {
            logger.error("Response status: " + status.getCode());
            throw new IOException(status.getDescription());
        }

        long end = System.currentTimeMillis();

        logger.info("Http request took " + (end-start) + "ms.");

        return response;
    }


    public Object get(ResponseHandler handler)
    throws IOException
    {
        return handler.handle(doGet());
    }
}
