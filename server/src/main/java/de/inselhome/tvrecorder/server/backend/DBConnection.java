/**
 * Copyright (C) 2010 Ingo Weinzierl (ingo_weinzierl@web.de)
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
package de.inselhome.tvrecorder.server.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * @author <a href="mailto:ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class DBConnection {

    private DBConnection() {
    }


    /**
     * This method returns the database connection string with the url of the
     * database file.
     *
     * @return the database connection string.
     */
    public static String getUrl() {

        // TODO the path to the database file should be read from configuration
        // file.
        return "jdbc:sqlite:tvrecorder.db";
    }


    /**
     * This method returns a {@link Connection} to the database.
     *
     * @return the database {@link Connection} object.
     */
    public static Connection getConnection()
    throws SQLException, ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        String url = getUrl();

        return DriverManager.getConnection(url);
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
