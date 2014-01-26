package io.github.benas.easybatch.xml;

import io.github.benas.easybatch.core.api.Record;
import io.github.benas.easybatch.core.api.RecordReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A record reader that reads xml records from an xml file.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class XmlRecordReader implements RecordReader {

    private static final Logger logger = Logger.getLogger(XmlRecordReader.class.getSimpleName());

    /**
     * The root element name.
     */
    private String rootElementName;

    /**
     * The xml input file.
     */
    private String xmlFile;

    /**
     * The xml reader.
     */
    private XMLEventReader xmlEventReader;

    /**
     * The current record number.
     */
    private long currentRecordNumber;

    public XmlRecordReader(final String rootElementName, final String xmlFile) {
        this.rootElementName = rootElementName;
        this.xmlFile = xmlFile;
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(new FileInputStream(new File(xmlFile)));
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
            logger.log(Level.SEVERE, "An exception occurred during checking the existence of next xml record", e);
            return false;
        }
    }

    @Override
    public Record readNextRecord() {
        StringBuilder stringBuilder = new StringBuilder("");
        try {
            while (!nextTagIsRootElementEnd()) {
                stringBuilder.append(xmlEventReader.nextEvent().toString());
            }
            //append root element end tag
            stringBuilder.append(xmlEventReader.nextEvent().toString());

            return new XmlRecord(++currentRecordNumber, stringBuilder.toString());
        } catch (Exception e) {
            throw new RuntimeException("An exception occurred during reading next xml record", e);
        }
    }

    @Override
    public long getTotalRecords() {
        long totalRecords = 0;
        try {
            XMLEventReader totalRecordsXmlEventReader =
                    XMLInputFactory.newInstance().createXMLEventReader(new FileInputStream(new File(xmlFile)));
            XMLEvent event;
            while (totalRecordsXmlEventReader.hasNext()) {
                event = totalRecordsXmlEventReader.nextEvent();
                if (event.isStartElement() && event.asStartElement().getName().toString().equals(rootElementName)) {
                    totalRecords++;
                }
            }
            totalRecordsXmlEventReader.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException("Unable to read data from xml file " + xmlFile, e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found " + xmlFile, e);
        }
        return totalRecords;
    }

    @Override
    public String getDataSourceName() {
        return xmlFile;
    }

    @Override
    public void close() {
        try {
            xmlEventReader.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException("An exception occurred during closing xml reader", e);
        }
    }

    /**
     * Utility method to check if the next tag matches a start tag of the root element.
     * @return true if the next tag matches a start element of the root element, false else
     * @throws Exception thrown if no able to peek the next xml element
     */
    private boolean nextTagIsRootElementStart() throws Exception {
        return xmlEventReader.peek().isStartElement() &&
               xmlEventReader.peek().asStartElement().getName().toString().equalsIgnoreCase(rootElementName);
    }

    /**
     * Utility method to check if the next tag matches an end tag of the root element.
     * @return true if the next tag matches an end tag of the root element, false else
     * @throws Exception thrown if no able to peek the next xml element
     */
    private boolean nextTagIsRootElementEnd() throws Exception {
        return xmlEventReader.peek().isEndElement() &&
                xmlEventReader.peek().asEndElement().getName().toString().equalsIgnoreCase(rootElementName);
    }

}
