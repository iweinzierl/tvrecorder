package de.inselhome.tvrecorder.client.gwt.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Log4JInitServlet extends HttpServlet {

    static final Logger logger = Logger.getLogger(Log4JInitServlet.class);


    public void init() throws ServletException {
        System.out.println("Log4JInitServlet init() starting.");

        String log4jfile = getInitParameter("log4j-properties");

        if (log4jfile != null && log4jfile.length() > 0) {
            String properties = getServletContext().getRealPath(log4jfile);
            PropertyConfigurator.configure(properties);
            logger.info("logger configured.");
        }
        else {
            System.out.println("Error setting up logger.");
        }

        System.out.println("Log4JInitServlet init() done.");
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
