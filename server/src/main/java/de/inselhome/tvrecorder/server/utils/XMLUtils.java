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

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

/**
 * This helper class supports some functions to work with XML files.
 *
 * @author <a href="mailto:ingo_weinzierl@web.de">Ingo Weinzierl</a>
 */
public final class XMLUtils {

    /** The logger that is used in this class. */
    private static Logger logger = Logger.getLogger(XMLUtils.class);

    /**
     * There is no need to create objects of this class.
     */
    private XMLUtils() {
    }


    /**
     * This function parses an XML file and retrieves the {@link Document}
     * object.
     *
     * @param file An XML file.
     *
     * @return the document.
     */
    public static final Document parse(File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        try {
            return factory.newDocumentBuilder().parse(file);
        }
        catch (IOException ioe) {
            logger.error(ioe.getLocalizedMessage());
        }
        catch (ParserConfigurationException pce) {
            logger.error(pce.getLocalizedMessage());
        }
        catch (SAXException se) {
            logger.error(se.getLocalizedMessage());
        }

        return null;
    }


    /**
     * This method retrieves an XPath value specified by <i>query</i> as string.
     *
     * @param root The root node. The query string needs to be relative to this
     * node.
     * @param query The XPath query.
     * @param ns The context namespace.
     *
     * @return the value as string.
     */
    public static final String getXPathAsString(
        Object           root,
        String           query,
        NamespaceContext ns)
    {
        return (String) getXPath(root, query, XPathConstants.STRING, ns);
    }

    /**
     * This method retrieves an XPath value specified by <i>path</i>.
     *
     * @param root The root node. The query string needs to be relative to this
     * node.
     * @param path The XPath.
     * @param type The return type.
     * @param ns The context namespace.
     *
     * @return the value as string.
     */
    public static final Object getXPath(
        Object           root,
        String           path,
        QName            type,
        NamespaceContext ns)
    {
        if (root == null) {
            logger.error("Cannot read xpath: root node is null.");
            return null;
        }

        try {
            XPathFactory fac = XPathFactory.newInstance();
            XPath      xpath = fac.newXPath();

            if (xpath != null) {
                if (ns != null) {
                    xpath.setNamespaceContext(ns);
                }

                return xpath.evaluate(path, root, type);
            }
        }
        catch (XPathExpressionException xee) {
            logger.error(xee.getLocalizedMessage());
        }

        return null;
    }
}
// vim:set ts=4 sw=4 si et sta sts=4 fenc=utf8 :
