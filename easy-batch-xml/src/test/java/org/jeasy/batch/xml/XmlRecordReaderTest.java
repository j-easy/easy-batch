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
package org.jeasy.batch.xml;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

public class XmlRecordReaderTest {

    private XmlRecordReader xmlRecordReader;

    @Test
    public void testReadRecord() throws Exception {
        // given
        InputStream dataSource = Mockito.spy(getDataSource("/persons.xml"));
        xmlRecordReader = new XmlRecordReader(dataSource, "person");
        xmlRecordReader.open();
        String expectedPayload = getXmlFromFile("/person.xml");

        // when
        XmlRecord xmlRecord = xmlRecordReader.readRecord();

        // then
        String actualPayload = xmlRecord.getPayload();
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectedPayload, actualPayload);
        assertThat(xmlRecord.getHeader().getNumber()).isEqualTo(1);
        assertThat(diff.similar()).isTrue();

        xmlRecordReader.close();
        verify(dataSource).close();
    }

    @Test
    public void testReadingEscapedXml() throws Exception {
        // given
        InputStream dataSource = Mockito.spy(getDataSource("/websites.xml"));
        xmlRecordReader = new XmlRecordReader(dataSource, "website");
        xmlRecordReader.open();

        // when
        XmlRecord record1 = xmlRecordReader.readRecord();
        XmlRecord record2 = xmlRecordReader.readRecord();
        XmlRecord record3 = xmlRecordReader.readRecord();
        XmlRecord record4 = xmlRecordReader.readRecord();
        XmlRecord record5 = xmlRecordReader.readRecord();
        XmlRecord record6 = xmlRecordReader.readRecord();

        // then
        assertThat(record1.getPayload()).isXmlEqualTo("<website name=\"google\" url=\"http://www.google.com?query=test&amp;sort=asc\"/>");
        assertThat(record2.getPayload()).isXmlEqualTo("<website name=\"l&apos;equipe\" url=\"http://www.lequipe.fr\"/>");
        assertThat(record3.getPayload()).isXmlEqualTo("<website name=\"l&quot;internaute.com\" url=\"http://www.linternaute.com\"/>");
        assertThat(record4.getPayload()).isXmlEqualTo("<website name=\"google\">http://www.google.com?query=test&amp;sort=asc</website>");
        assertThat(record5.getPayload()).isXmlEqualTo("<website name=\"test\">&lt;test&gt;foo&lt;/test&gt;</website>");
        assertThat(record6).isNull();

        xmlRecordReader.close();
        verify(dataSource, atLeast(1)).close();
    }

    @Test
    public void testReadingXmlWithCustomNamespace() throws Exception {
        // given
        InputStream dataSource = Mockito.spy(getDataSource("/beans.xml"));
        xmlRecordReader = new XmlRecordReader(dataSource, "bean");
        xmlRecordReader.open();

        // when
        XmlRecord record1 = xmlRecordReader.readRecord();
        XmlRecord record2 = xmlRecordReader.readRecord();
        XmlRecord record3 = xmlRecordReader.readRecord();

        // then
        assertThat(record1.getPayload()).isXmlEqualTo("<bean id=\"foo\" class=\"java.lang.String\"><description>foo bean</description></bean>");
        assertThat(record2.getPayload()).isXmlEqualTo("<bean id=\"bar\" class=\"java.lang.String\"/>");
        assertThat(record3).isNull();

        xmlRecordReader.close();
        verify(dataSource, atLeast(1)).close();
    }

    /*
     * Test empty files
     */

    @Test
    public void testHasNextRecordForEmptyPersonsFile() throws Exception {
        // given
        InputStream dataSource = Mockito.spy(getDataSource("/persons-empty.xml"));
        xmlRecordReader = new XmlRecordReader(dataSource, "person");
        xmlRecordReader.open();

        // when
        XmlRecord actual = xmlRecordReader.readRecord();

        // then
        assertThat(actual).isNull();
        xmlRecordReader.close();
        verify(dataSource, atLeast(1)).close();
    }

    @Test
    public void testHasNextRecordForEmptyFile() throws Exception {
        // given
        InputStream dataSource = Mockito.spy(getDataSource("/empty-file.xml"));
        xmlRecordReader = new XmlRecordReader(dataSource, "person");
        xmlRecordReader.open();

        // when
        XmlRecord actual = xmlRecordReader.readRecord();

        // then
        assertThat(actual).isNull();
        xmlRecordReader.close();
        verify(dataSource, atLeast(1)).close();
    }

     /*
     * Test "non standard" files
     */

    @Test
    public void testReadNextNestedRecord() throws Exception {
        // given
        InputStream dataSource = Mockito.spy(getDataSource("/persons-nested.xml"));
        xmlRecordReader = new XmlRecordReader(dataSource, "person");
        xmlRecordReader.open();
        String expectedPayload = getXmlFromFile("/person.xml");

        // when
        XmlRecord xmlRecord = xmlRecordReader.readRecord();

        // then
        String actualPayload = xmlRecord.getPayload();
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectedPayload, actualPayload);
        assertThat(xmlRecord.getHeader().getNumber()).isEqualTo(1);
        assertThat(diff.similar()).isTrue();
        xmlRecordReader.close();
        verify(dataSource).close();
    }

    private String getXmlFromFile(String file) {
        return new Scanner(this.getClass().getResourceAsStream(file)).useDelimiter("\\A").next();
    }

    private InputStream getDataSource(String name) {
        return this.getClass().getResourceAsStream(name);
    }
}
