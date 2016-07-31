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

package org.easybatch.extensions.xstream;

import com.thoughtworks.xstream.XStream;
import org.easybatch.core.mapper.RecordMappingException;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.xml.XmlRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class XstreamRecordMapperTest {

    private XstreamRecordMapper<Person> xmlRecordMapper;

    @Mock
    private XmlRecord xmlRecord;
    @Mock
    private Header header;

    @Before
    public void setUp() throws Exception {
        XStream xStream = new XStream();
        xStream.alias("person", Person.class);
        when(xmlRecord.getHeader()).thenReturn(header);
        xmlRecordMapper = new XstreamRecordMapper<>(xStream);
    }

    @Test
    public void testValidXmlPersonMapping() throws Exception {
        when(xmlRecord.getPayload()).thenReturn(getXmlFromFile("/person.xml"));

        GenericRecord<Person> actual = xmlRecordMapper.processRecord(xmlRecord);
        Person person = actual.getPayload();

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isEqualTo("bar");
        assertThat(person.isMarried()).isTrue();
    }

    @Test
    public void testEmptyXmlPersonMapping() throws Exception {
        when(xmlRecord.getPayload()).thenReturn("<person/>");

        GenericRecord<Person> actual = xmlRecordMapper.processRecord(xmlRecord);
        Person person = actual.getPayload();

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(person.getId()).isEqualTo(0);
        assertThat(person.getFirstName()).isNull();
        assertThat(person.getLastName()).isNull();
        assertThat(person.isMarried()).isFalse();
    }

    @Test
    public void testPartialXmlPersonMapping() throws Exception {
        when(xmlRecord.getPayload()).thenReturn(getXmlFromFile("/person-partial.xml"));

        GenericRecord<Person> actual = xmlRecordMapper.processRecord(xmlRecord);
        Person person = actual.getPayload();

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isNull();
        assertThat(person.isMarried()).isFalse();
    }

    @Test(expected = RecordMappingException.class)
    public void testInvalidXmlPersonMapping() throws Exception {
        when(xmlRecord.getPayload()).thenReturn(getXmlFromFile("/person-invalid.xml"));

        xmlRecordMapper.processRecord(xmlRecord);
    }

    private String getXmlFromFile(String file) {
        return new Scanner(this.getClass().getResourceAsStream(file)).useDelimiter("\\A").next();
    }

}
