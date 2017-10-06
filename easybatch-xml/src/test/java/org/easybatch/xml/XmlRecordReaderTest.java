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
    public void testReadRecord() throws Exception {
        XmlRecord xmlRecord = xmlRecordReader.readRecord();
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
        XmlRecord record = xmlRecordReader.readRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<website name=\"google\" url=\"http://www.google.com?query=test&amp;sort=asc\"/>");

        record = xmlRecordReader.readRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<website name=\"l&apos;equipe\" url=\"http://www.lequipe.fr\"/>");

        record = xmlRecordReader.readRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<website name=\"l&quot;internaute.com\" url=\"http://www.linternaute.com\"/>");

        record = xmlRecordReader.readRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<website name=\"google\">http://www.google.com?query=test&amp;sort=asc</website>");

        record = xmlRecordReader.readRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<website name=\"test\">&lt;test&gt;foo&lt;/test&gt;</website>");

        record = xmlRecordReader.readRecord();
        assertThat(record).isNull();

    }

    @Test
    public void testReadingXmlWithCustomNamespace() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("bean", getDataSource("/beans.xml"));
        xmlRecordReader.open();

        XmlRecord record = xmlRecordReader.readRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<bean id=\"foo\" class=\"java.lang.String\"><description>foo bean</description></bean>");

        record = xmlRecordReader.readRecord();
        assertThat(record.getPayload()).isXmlEqualTo("<bean id=\"bar\" class=\"java.lang.String\"/>");

        record = xmlRecordReader.readRecord();
        assertThat(record).isNull();

    }

    /*
     * Test empty files
     */

    @Test
    public void testHasNextRecordForEmptyPersonsFile() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", getDataSource("/persons-empty.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.readRecord()).isNull();
    }

    @Test
    public void testHasNextRecordForEmptyFile() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", getDataSource("/empty-file.xml"));
        xmlRecordReader.open();
        assertThat(xmlRecordReader.readRecord()).isNull();
    }

     /*
     * Test "non standard" files
     */

    @Test
    public void testReadNextNestedRecord() throws Exception {
        xmlRecordReader.close();
        xmlRecordReader = new XmlRecordReader("person", getDataSource("/persons-nested.xml"));
        xmlRecordReader.open();
        XmlRecord xmlRecord = xmlRecordReader.readRecord();
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
