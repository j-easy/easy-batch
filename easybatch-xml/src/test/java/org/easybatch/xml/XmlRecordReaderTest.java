/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlRecordReaderTest {

    private XmlRecordReader xmlRecordReader;

    @Before
    public void setUp() throws Exception {
        xmlRecordReader = new XmlRecordReader("person", getDataSource("/persons.xml"));
        xmlRecordReader.open();
    }

    @Test
    public void testGetTotalRecords() throws Exception {
        assertThat(xmlRecordReader.getTotalRecords()).isNull();
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

        assertThat(xmlRecord.getHeader().getNumber()).isEqualTo(1);
        assertThat(diff.similar()).isTrue();
    }

    @Test
    public void testReadingEscapedXml() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("website", getDataSource("/websites.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.hasNextRecord()).isTrue();
        XmlRecord record = xmlRecordReader.readNextRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<website name=\"google\" url=\"http://www.google.com?query=test&amp;sort=asc\"/>");

        assertThat(xmlRecordReader.hasNextRecord()).isTrue();
        record = xmlRecordReader.readNextRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<website name=\"l&apos;equipe\" url=\"http://www.lequipe.fr\"/>");

        assertThat(xmlRecordReader.hasNextRecord()).isTrue();
        record = xmlRecordReader.readNextRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<website name=\"l&quot;internaute.com\" url=\"http://www.linternaute.com\"/>");

        assertThat(xmlRecordReader.hasNextRecord()).isFalse();

    }

    @Test
    public void testReadingXmlWithCustomNamespace() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("bean", getDataSource("/beans.xml"));
        xmlRecordReader.open();

        assertThat(xmlRecordReader.hasNextRecord()).isTrue();
        XmlRecord record = xmlRecordReader.readNextRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<bean id=\"foo\" class=\"java.lang.String\"><description>foo bean</description></bean>");

        assertThat(xmlRecordReader.hasNextRecord()).isTrue();
        record = xmlRecordReader.readNextRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<bean id=\"bar\" class=\"java.lang.String\"/>");

        assertThat(xmlRecordReader.hasNextRecord()).isFalse();

    }

    /*
     * Test empty files
     */

    @Test
    public void testGetTotalRecordsForEmptyPersonsFile() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", getDataSource("/persons-empty.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.getTotalRecords()).isNull();
    }

    @Test
    public void testHasNextRecordForEmptyPersonsFile() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", getDataSource("/persons-empty.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void testGetTotalRecordsForEmptyFile() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", getDataSource("/empty-file.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.getTotalRecords()).isNull();
    }

    @Test
    public void testHasNextRecordForEmptyFile() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", getDataSource("/empty-file.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.hasNextRecord()).isFalse();
    }

     /*
     * Test "non standard" files
     */

    @Test
    public void testReadNextNestedRecord() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", getDataSource("/persons-nested.xml"));
        xmlRecordReader.open();
        xmlRecordReader.hasNextRecord(); //should call this method to move the cursor forward to the first tag
        XmlRecord xmlRecord = xmlRecordReader.readNextRecord();
        String expectedPayload = getXmlFromFile("/person.xml");
        String actualPayload = xmlRecord.getPayload();

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectedPayload, actualPayload);

        assertThat(xmlRecord.getHeader().getNumber()).isEqualTo(1);
        assertThat(diff.similar()).isTrue();
    }


    @After
    public void tearDown() throws Exception {
        xmlRecordReader.close();
    }

    private String getXmlFromFile(String file) {
        return new Scanner(this.getClass().getResourceAsStream(file)).useDelimiter("\\A").next();
    }

    private InputStream getDataSource(String name) {
        return this.getClass().getResourceAsStream(name);
    }
}
