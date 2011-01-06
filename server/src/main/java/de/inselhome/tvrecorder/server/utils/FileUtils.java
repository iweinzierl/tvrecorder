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
package de.inselhome.tvrecorder.server.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * @author <a href="mailto: ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public class FileUtils {

    private static Logger logger = Logger.getLogger(FileUtils.class);

    public static File getFile(String filename) {
        File file = new File(filename);

        try {
            if (!file.exists() && file.canWrite()) {
                file.createNewFile();
            }

            return file;
        }
        catch (IOException ioe) {
            logger.error(ioe.getLocalizedMessage());
        }

        return null;
    }


    public static void deleteFile(File file) {
        if (file == null) {
            return;
        }

        file.delete();
    }


    public static boolean writeStringToFile(File file, String text) {

        PrintWriter    writer = null;
        BufferedWriter buff   = null;

        try {
            buff   = new BufferedWriter(new FileWriter(file));
            writer = new PrintWriter(buff);

            writer.print(text);

            if (buff != null) {
                try {
                    buff.close();
                }
                catch (IOException e) { /* nothing to do here */ }
            }

            if (writer != null) {
                writer.close();
            }

            return true;
        }
        catch (IOException ioe) {
            logger.error(ioe.getLocalizedMessage());
        }

        return false;
    }


    public static String[] readLines(String filename) {
        BufferedReader reader = null;
        List<String>   lines  = new ArrayList<String>();

        try {
            reader = new BufferedReader(new FileReader(filename));

            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            return (String[]) lines.toArray(new String[lines.size()]);
        }
        catch (IOException ioe) {
            logger.error(
                "Error while reader file '" + filename +"':" +
                ioe.getLocalizedMessage());
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) { /* do nothing */ }
            }
        }

        return null;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
