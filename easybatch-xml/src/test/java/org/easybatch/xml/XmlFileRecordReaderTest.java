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

import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlFileRecordReaderTest {

    private File dataSource;
    private XmlFileRecordReader xmlFileRecordReader;

    @Test
    public void testXmlRecordReading() throws Exception {
        // given
        dataSource = new File("src/test/resources/data.xml");
        xmlFileRecordReader = new XmlFileRecordReader(dataSource, "data");
        xmlFileRecordReader.open();
        String expectedDataSourceName = dataSource.getAbsolutePath();

        // when
        XmlRecord xmlRecord1 = xmlFileRecordReader.readRecord();
        XmlRecord xmlRecord2 = xmlFileRecordReader.readRecord();
        XmlRecord xmlRecord3 = xmlFileRecordReader.readRecord();
        XmlRecord xmlRecord4 = xmlFileRecordReader.readRecord();

        // then
        assertThat(xmlRecord1.getHeader().getNumber()).isEqualTo(1L);
        assertThat(xmlRecord1.getHeader().getSource()).isEqualTo(expectedDataSourceName);
        assertThat(xmlRecord1.getPayload()).isEqualTo("<data>1</data>");

        assertThat(xmlRecord2.getHeader().getNumber()).isEqualTo(2L);
        assertThat(xmlRecord2.getHeader().getSource()).isEqualTo(expectedDataSourceName);
        assertThat(xmlRecord2.getPayload()).isEqualTo("<data>2</data>");

        assertThat(xmlRecord3.getHeader().getNumber()).isEqualTo(3L);
        assertThat(xmlRecord3.getHeader().getSource()).isEqualTo(expectedDataSourceName);
        assertThat(xmlRecord3.getPayload()).isEqualTo("<data>3</data>");

        assertThat(xmlRecord4).isNull();
    }

    @Test
    public void testReadingEmptyElement() throws Exception {
        // given
        dataSource = new File("src/test/resources/data-empty.xml");
        xmlFileRecordReader = new XmlFileRecordReader(dataSource, "data");
        xmlFileRecordReader.open();

        // when
        XmlRecord xmlRecord1 = xmlFileRecordReader.readRecord();

        // then
        assertThat(xmlRecord1).isNull();
    }

    @After
    public void tearDown() throws Exception {
        xmlFileRecordReader.close();
    }
}