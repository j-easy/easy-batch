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
import org.easybatch.core.api.RecordMappingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link XmlRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class XmlRecordMapperTest {

    private XmlRecordMapper xmlRecordMapper;

    private XmlRecord xmlRecord;

    @Mock
    private Header header;

    @Before
    public void setUp() throws Exception {
        xmlRecordMapper = new XmlRecordMapper<Person>(Person.class);
    }

    @Test
    public void testValidXmlPersonMapping() throws Exception {
        xmlRecord = new XmlRecord(header, getXmlFromFile("/person.xml"));
        Person person = (Person) xmlRecordMapper.mapRecord(xmlRecord);
        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isEqualTo("bar");
        assertThat(person.getBirthDate()).isEqualTo(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"));
        assertThat(person.isMarried()).isTrue();
    }

    @Test
    public void testEmptyXmlPersonMapping() throws Exception {
        xmlRecord = new XmlRecord(header, "<person/>");
        Person person = (Person) xmlRecordMapper.mapRecord(xmlRecord);
        assertThat(person.getId()).isEqualTo(0);
        assertThat(person.getFirstName()).isNull();
        assertThat(person.getLastName()).isNull();
        assertThat(person.getBirthDate()).isNull();
        assertThat(person.isMarried()).isFalse();
    }

    @Test
    public void testPartialXmlPersonMapping() throws Exception {
        xmlRecord = new XmlRecord(header, getXmlFromFile("/person-partial.xml"));
        Person person = (Person) xmlRecordMapper.mapRecord(xmlRecord);
        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isNull();
        assertThat(person.getBirthDate()).isNull();
        assertThat(person.isMarried()).isFalse();
    }

    @Test
    public void testMappingWithEscapedXmlSpecialCharacter() throws Exception {
        XmlRecordMapper<Website> xmlRecordMapper = new XmlRecordMapper<Website>(Website.class);

        xmlRecord = new XmlRecord(header, "<website name='google' url='http://www.google.com?query=test&amp;sort=asc'/>");
        Website website = xmlRecordMapper.mapRecord(xmlRecord);
        assertThat(website).isNotNull();
        assertThat(website.getName()).isEqualTo("google");
        assertThat(website.getUrl()).isEqualTo("http://www.google.com?query=test&sort=asc");

        xmlRecord = new XmlRecord(header, "<website name='l&apos;équipe' url='http://www.lequipe.fr'/>");
        website = xmlRecordMapper.mapRecord(xmlRecord);
        assertThat(website).isNotNull();
        assertThat(website.getName()).isEqualTo("l'équipe");
        assertThat(website.getUrl()).isEqualTo("http://www.lequipe.fr");

    }

    @Test(expected = RecordMappingException.class)
    public void testMappingWithUnescapedXmlSpecialCharacter() throws Exception {
        xmlRecord = new XmlRecord(header, "<website name='google' url='http://www.google.com?query=test&sort=asc'/>");
        XmlRecordMapper<Website> xmlRecordMapper = new XmlRecordMapper<Website>(Website.class);
        xmlRecordMapper.mapRecord(xmlRecord);
    }

    @Test(expected = RecordMappingException.class)
    public void testInvalidXmlPersonMapping() throws Exception {
        xmlRecord = new XmlRecord(header, getXmlFromFile("/person-invalid.xml"));
        xmlRecordMapper.mapRecord(xmlRecord);
    }

    @Test(expected = RecordMappingException.class)
    public void testMappingOfInvalidXmlAccordingToXsd() throws Exception {
        xmlRecordMapper = new XmlRecordMapper<Person>(Person.class, getFile("/person.xsd"));
        xmlRecord = new XmlRecord(header, getXmlFromFile("/person-invalid-xsd.xml"));
        xmlRecordMapper.mapRecord(xmlRecord);
    }

    private File getFile(String fileName) {
        return new File(this.getClass().getResource(fileName).getFile());
    }

    private String getXmlFromFile(String file) {
        return new Scanner(this.getClass().getResourceAsStream(file)).useDelimiter("\\A").next();
    }

}
