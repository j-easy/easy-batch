package org.easybatch.xml.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import org.easybatch.xml.XmlRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Scanner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test class for {@link XstreamRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class XstreamRecordMapperTest {

    private XStream xStream;

    private XstreamRecordMapper xmlRecordMapper;

    private XmlRecord xmlRecord;

    @Before
    public void setUp() throws Exception {
        xStream = new XStream();
        xStream.alias("person", Person.class);
        xmlRecordMapper = new XstreamRecordMapper<Person>(xStream);
        String xml = getXmlFromFile("/person.xml");
        xmlRecord = new XmlRecord(1, xml);
    }

    @Test
    public void testValidXmlPersonMapping() throws Exception {
        Person person = (Person) xmlRecordMapper.mapRecord(xmlRecord);
        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isEqualTo("bar");
        assertThat(person.isMarried()).isTrue();
    }

    @Test
    public void testEmptyXmlPersonMapping() throws Exception {
        xmlRecord = new XmlRecord(1, "<person/>");
        Person person = (Person) xmlRecordMapper.mapRecord(xmlRecord);
        assertThat(person.getId()).isEqualTo(0);
        assertThat(person.getFirstName()).isNull();
        assertThat(person.getLastName()).isNull();
        assertThat(person.isMarried()).isFalse();
    }

    @Test
    public void testPartialXmlPersonMapping() throws Exception {
        xmlRecord = new XmlRecord(1, getXmlFromFile("/person-partial.xml"));
        Person person = (Person) xmlRecordMapper.mapRecord(xmlRecord);
        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isNull();
        assertThat(person.isMarried()).isFalse();
    }

    @Test(expected = ConversionException.class)
    public void testInvalidXmlPersonMapping() throws Exception {
        xmlRecord = new XmlRecord(1, getXmlFromFile("/person-invalid.xml"));
        xmlRecordMapper.mapRecord(xmlRecord);
    }

    private File getFile(String fileName) {
        return new File(this.getClass().getResource(fileName).getFile());
    }

    private String getXmlFromFile(String file) {
        return new Scanner(this.getClass().getResourceAsStream(file) ).useDelimiter("\\A").next();
    }

}
