/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.xml;

import org.easybatch.core.api.Header;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A record reader that reads xml records from an xml stream.
 *
 * This reader produces {@link XmlRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class XmlRecordReader implements RecordReader {

    private static final Logger LOGGER = Logger.getLogger(XmlRecordReader.class.getSimpleName());

    /**
     * The root element name.
     */
    private String rootElementName;

    /**
     * The xml input stream.
     */
    private InputStream xmlInputStream;

    /**
     * The xml reader.
     */
    private XMLEventReader xmlEventReader;

    /**
     * The current record number.
     */
    private long currentRecordNumber;

    public XmlRecordReader(final String rootElementName, final InputStream xmlInputStream) {
        this.rootElementName = rootElementName;
        this.xmlInputStream = xmlInputStream;
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(xmlInputStream);
    }

    @Override
    public boolean hasNextRecord() {
        try {
            while (!nextTagIsRootElementStart()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent instanceof EndDocument) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception occurred during checking the existence of next xml record", e);
            return false;
        }
    }

    @Override
    public Record readNextRecord() throws Exception {
        StringBuilder stringBuilder = new StringBuilder("");
        while (!nextTagIsRootElementEnd()) {
            stringBuilder.append(xmlEventReader.nextEvent().toString());
        }
        //append root element end tag
        stringBuilder.append(xmlEventReader.nextEvent().toString());
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new XmlRecord(header, stringBuilder.toString());
    }

    @Override
    public Long getTotalRecords() {
        return null;
    }

    @Override
    public String getDataSourceName() {
        return "XML stream: " + xmlInputStream;
    }

    @Override
    public void close() throws Exception {
        if (xmlEventReader != null) {
            xmlEventReader.close();
        }
    }

    /**
     * Utility method to check if the next tag matches a start tag of the root element.
     *
     * @return true if the next tag matches a start element of the root element, false else
     * @throws Exception thrown if no able to peek the next xml element
     */
    private boolean nextTagIsRootElementStart() throws Exception {
        return xmlEventReader.peek().isStartElement() &&
                xmlEventReader.peek().asStartElement().getName().toString().equalsIgnoreCase(rootElementName);
    }

    /**
     * Utility method to check if the next tag matches an end tag of the root element.
     *
     * @return true if the next tag matches an end tag of the root element, false else
     * @throws Exception thrown if no able to peek the next xml element
     */
    private boolean nextTagIsRootElementEnd() throws Exception {
        return xmlEventReader.peek().isEndElement() &&
                xmlEventReader.peek().asEndElement().getName().toString().equalsIgnoreCase(rootElementName);
    }

}
