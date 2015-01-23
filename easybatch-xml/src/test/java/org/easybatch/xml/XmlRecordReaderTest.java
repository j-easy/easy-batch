package org.easybatch.xml;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test class for {@link XmlRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class XmlRecordReaderTest {

    private XmlRecordReader xmlRecordReader;

    @Before
    public void setUp() throws Exception {
        xmlRecordReader = new XmlRecordReader("person", this.getClass().getResourceAsStream("/persons.xml"));
        xmlRecordReader.open();
    }

    @Test
    public void testGetTotalRecords() throws Exception {
        assertThat(xmlRecordReader.getTotalRecords()).isEqualTo(2);
    }

    @Test
    public void testHasNextRecord() throws Exception {
        assertThat(xmlRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void testReadNextRecord() throws Exception {
        xmlRecordReader.hasNextRecord(); //should call this method to move the cursor forward to the first tag
        XmlRecord xmlRecord = xmlRecordReader.readNextRecord();
        String expectedPayload = getXmlFromFile("/person.xml");
        String actualPayload = xmlRecord.getPayload();

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectedPayload, actualPayload);

        assertThat(xmlRecord.getNumber()).isEqualTo(1);
        assertThat(diff.similar()).isTrue();
    }

    /*
     * Test empty files
     */

    @Test
    public void testGetTotalRecordsForEmptyPersonsFile() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", this.getClass().getResourceAsStream("/persons-empty.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.getTotalRecords()).isEqualTo(0);
    }

    @Test
    public void testHasNextRecordForEmptyPersonsFile() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", this.getClass().getResourceAsStream("/persons-empty.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void testGetTotalRecordsForEmptyFile() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", this.getClass().getResourceAsStream("/empty-file.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.getTotalRecords()).isNull();
    }

    @Test
    public void testHasNextRecordForEmptyFile() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", this.getClass().getResourceAsStream("/empty-file.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.hasNextRecord()).isFalse();
    }

    @After
    public void tearDown() throws Exception {
        xmlRecordReader.close();
    }

    private String getXmlFromFile(String file) {
        return new Scanner(this.getClass().getResourceAsStream(file) ).useDelimiter("\\A").next();
    }
}
