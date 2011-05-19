package de.inselhome.tvrecorder.common.utils;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;


public final class XMLUtils {

    public static final Document parseDocument(InputStream inputStream) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        try {
            return factory.newDocumentBuilder().parse(inputStream);
        }
        catch (ParserConfigurationException pce) {
            System.err.println(pce.getLocalizedMessage());
        }
        catch (SAXException se) {
            System.err.println(se.getLocalizedMessage());
        }
        catch (IOException ioe) {
            System.err.println(ioe.getLocalizedMessage());
        }
        return null;
    }


    public static boolean toStream(Document document, OutputStream out) {
        try {
            Transformer transformer =
                TransformerFactory.newInstance().newTransformer();
            DOMSource    source = new DOMSource(document);
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);
            return true;
        }
        catch (TransformerConfigurationException tce) {
            System.err.println(tce.getLocalizedMessage());
        }
        catch (TransformerFactoryConfigurationError tfce) {
            System.err.println(tfce.getLocalizedMessage());
        }
        catch (TransformerException te) {
            System.err.println(te.getLocalizedMessage());
        }

        return false;
    }


    public static final XPath newXPath() {
        return newXPath(null);
    }


    public static final XPath newXPath(NamespaceContext namespaceContext) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath        xpath   = factory.newXPath();
        if (namespaceContext != null) {
            xpath.setNamespaceContext(namespaceContext);
        }
        return xpath;
    }


    public static final Object xpath(
        Object root,
        String query,
        QName  returnTyp
    ) {
        return xpath(root, query, returnTyp, null);
    }


    public static final String xpathString(
        Object root, String query, NamespaceContext namespaceContext
    ) {
        return (String)xpath(
            root, query, XPathConstants.STRING, namespaceContext);
    }


    public static final Object xpath(
        Object           root,
        String           query,
        QName            returnType,
        NamespaceContext namespaceContext
    ) {
        if (root == null) {
            return null;
        }

        try {
            XPath xpath = newXPath(namespaceContext);
            if (xpath != null) {
                return xpath.evaluate(query, root, returnType);
            }
        }
        catch (XPathExpressionException xpee) {
            System.err.println(xpee.getLocalizedMessage());
        }

        return null;
    }
}
