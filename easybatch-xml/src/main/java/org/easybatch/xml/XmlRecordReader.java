/**
 * The MIT License
 *
 *   Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package org.easybatch.xml;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Header;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A record reader that reads xml records from an xml stream.
 * <p/>
 * This reader produces {@link XmlRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class XmlRecordReader implements RecordReader {

    private static final Logger LOGGER = Logger.getLogger(XmlRecordReader.class.getName());

    private String rootElementName;
    private InputStream xmlInputStream;
    private String charset;
    private XMLEventReader xmlEventReader;
    private long currentRecordNumber;

    /**
     * Create a new {@link XmlRecordReader}.
     *
     * @param rootElementName to extract as record
     * @param xmlInputStream to read
     */
    public XmlRecordReader(final String rootElementName, final InputStream xmlInputStream) {
        this(rootElementName, xmlInputStream, Charset.defaultCharset().name());
    }

    /**
     * Create a new {@link XmlRecordReader}.
     *
     * @param rootElementName to extract as record
     * @param xmlInputStream to read
     * @param charset of the input stream
     */
    public XmlRecordReader(final String rootElementName, final InputStream xmlInputStream, final String charset) {
        this.rootElementName = rootElementName;
        this.xmlInputStream = xmlInputStream;
        this.charset = charset;
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(xmlInputStream, charset);
    }

    @Override
    public XmlRecord readRecord() throws Exception {
        if (hasNextRecord()) {
            StringBuilder stringBuilder = new StringBuilder("");
            while (!nextTagIsRootElementEnd()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    escapeStartElementAttributes(stringBuilder, xmlEvent);
                } else if (xmlEvent.isEndElement()) {
                    writeEndElement(stringBuilder, xmlEvent);
                } else {
                    stringBuilder.append(escape(xmlEvent.asCharacters().getData()));
                }
            }
            writeEndElement(stringBuilder, xmlEventReader.nextEvent());
            Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
            return new XmlRecord(header, stringBuilder.toString());
        } else {
            return null;
        }
    }

    protected String getDataSourceName() {
        return "XML stream";
    }

    @Override
    public void close() throws Exception {
        if (xmlEventReader != null) {
            xmlEventReader.close();
        }
    }

    private boolean hasNextRecord() {
        try {
            while (!nextTagIsRootElementStart()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent instanceof EndDocument) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Unable to peek next xml record", e); // JUL does not have DEBUG level ..
            return false;
        }
    }

    private boolean nextTagIsRootElementStart() throws XMLStreamException {
        return xmlEventReader.peek().isStartElement() &&
                xmlEventReader.peek().asStartElement().getName().getLocalPart().equalsIgnoreCase(rootElementName);
    }

    private boolean nextTagIsRootElementEnd() throws XMLStreamException {
        return xmlEventReader.peek().isEndElement() &&
                xmlEventReader.peek().asEndElement().getName().getLocalPart().equalsIgnoreCase(rootElementName);
    }

    private void writeEndElement(StringBuilder stringBuilder, XMLEvent xmlEvent) throws XMLStreamException {
        if (xmlEvent.isEndElement()) {
            EndElement endElement = xmlEvent.asEndElement();
            stringBuilder.append("</").append(endElement.getName().getLocalPart()).append(">");
        }
    }

    private void escapeStartElementAttributes(StringBuilder stringBuilder, XMLEvent xmlEvent) {
        StartElement startElement = xmlEvent.asStartElement();
        stringBuilder.append("<").append(startElement.getName().getLocalPart());
        Iterator iterator = startElement.getAttributes();
        while (iterator.hasNext()) {
            Attribute attribute = (Attribute) iterator.next();
            stringBuilder.append(" ")
                    .append(attribute.getName().getLocalPart())
                    .append("='")
                    .append(escape(attribute.getValue()))
                    .append("'");
        }
        stringBuilder.append(">");
    }

    private String escape(String xmlToEscape) {
        return xmlToEscape.replaceAll("&", "&amp;")
                .replaceAll("'", "&apos;")
                .replaceAll("\"", "&quot;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                ;
    }

}
