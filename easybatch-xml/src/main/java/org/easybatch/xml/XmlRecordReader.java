/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;

/**
 * A record reader that reads xml records from an xml input stream.
 *
 * This reader produces {@link XmlRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class XmlRecordReader implements RecordReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlRecordReader.class.getName());

    private String rootElementName;
    private InputStream xmlInputStream;
    private Charset charset;
    private XMLEventReader xmlEventReader;
    private long currentRecordNumber;

    /**
     * Create a new {@link XmlRecordReader}.
     *
     * @param rootElementName to extract as record
     * @param xmlInputStream to read
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link XmlRecordReader#XmlRecordReader(java.io.InputStream, java.lang.String)} instead
     */
    @Deprecated
    public XmlRecordReader(final String rootElementName, final InputStream xmlInputStream) {
        this(rootElementName, xmlInputStream, Charset.defaultCharset().name());
    }

    /**
     * Create a new {@link XmlRecordReader}.
     *
     * @param xmlInputStream to read
     * @param rootElementName to extract as record
     */
    public XmlRecordReader(final InputStream xmlInputStream, final String rootElementName) {
        this(xmlInputStream, rootElementName, Charset.defaultCharset());
    }

    /**
     * Create a new {@link XmlRecordReader}.
     *
     * @param rootElementName to extract as record
     * @param xmlInputStream to read
     * @param charset of the input stream
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link XmlRecordReader#XmlRecordReader(java.io.InputStream, java.lang.String, java.nio.charset.Charset)} instead
     */
    @Deprecated
    public XmlRecordReader(final String rootElementName, final InputStream xmlInputStream, final String charset) {
        this.rootElementName = rootElementName;
        this.xmlInputStream = xmlInputStream;
        this.charset = Charset.forName(charset);
    }

    /**
     * Create a new {@link XmlRecordReader}.
     *
     * @param rootElementName to extract as record
     * @param xmlInputStream to read
     * @param charset of the input stream
     */
    public XmlRecordReader(final InputStream xmlInputStream, final String rootElementName, final Charset charset) {
        this.rootElementName = rootElementName;
        this.xmlInputStream = xmlInputStream;
        this.charset = charset;
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(xmlInputStream, charset.name());
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
                    if (xmlEvent.isCharacters()) {
                        stringBuilder.append(escape(xmlEvent.asCharacters().getData()));
                    }
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
            xmlEventReader.close(); // TODO should close underlying input stream (See Javadoc)
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
            LOGGER.debug("Unable to peek next xml record", e);
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

    private void writeEndElement(StringBuilder stringBuilder, XMLEvent xmlEvent) {
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
