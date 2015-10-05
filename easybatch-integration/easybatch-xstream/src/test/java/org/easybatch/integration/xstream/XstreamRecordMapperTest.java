/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.integration.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import org.easybatch.xml.XmlRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link XstreamRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class XstreamRecordMapperTest {

    private XstreamRecordMapper xmlRecordMapper;

    @Mock
    private XmlRecord xmlRecord;

    @Before
    public void setUp() throws Exception {
        XStream xStream = new XStream();
        xStream.alias("person", Person.class);
        xmlRecordMapper = new XstreamRecordMapper<Person>(xStream);
    }

    @Test
    public void testValidXmlPersonMapping() throws Exception {
        when(xmlRecord.getPayload()).thenReturn(getXmlFromFile("/person.xml"));

        Person person = (Person) xmlRecordMapper.processRecord(xmlRecord);

        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isEqualTo("bar");
        assertThat(person.isMarried()).isTrue();
    }

    @Test
    public void testEmptyXmlPersonMapping() throws Exception {
        when(xmlRecord.getPayload()).thenReturn("<person/>");

        Person person = (Person) xmlRecordMapper.processRecord(xmlRecord);

        assertThat(person.getId()).isEqualTo(0);
        assertThat(person.getFirstName()).isNull();
        assertThat(person.getLastName()).isNull();
        assertThat(person.isMarried()).isFalse();
    }

    @Test
    public void testPartialXmlPersonMapping() throws Exception {
        when(xmlRecord.getPayload()).thenReturn(getXmlFromFile("/person-partial.xml"));

        Person person = (Person) xmlRecordMapper.processRecord(xmlRecord);

        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isNull();
        assertThat(person.isMarried()).isFalse();
    }

    @Test(expected = ConversionException.class)
    public void testInvalidXmlPersonMapping() throws Exception {
        when(xmlRecord.getPayload()).thenReturn(getXmlFromFile("/person-invalid.xml"));

        xmlRecordMapper.processRecord(xmlRecord);
    }

    private String getXmlFromFile(String file) {
        return new Scanner(this.getClass().getResourceAsStream(file)).useDelimiter("\\A").next();
    }

}
