package org.easybatch.xml.test;

import org.easybatch.xml.XmlRecord;
import org.easybatch.xml.XmlRecordMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link XmlRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class XmlRecordMapperTest {

    private XmlRecordMapper xmlRecordMapper;

    private XmlRecord xmlRecord;

    @Before
    public void setUp() throws Exception {
        xmlRecordMapper = new XmlRecordMapper<Person>(Person.class);
        String xml = new Scanner(this.getClass().getResourceAsStream("/person.xml") ).useDelimiter("\\A").next();
        xmlRecord = new XmlRecord(1, xml);
    }

    @Test
    public void testXmlPersonMapping() throws Exception {
        Person person = (Person) xmlRecordMapper.mapRecord(xmlRecord);
        assertEquals(1, person.getId());
        assertEquals("foo", person.getFirstName());
        assertEquals("bar", person.getLastName());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), person.getBirthDate());
        assertEquals(true, person.isMarried());
    }

    @After
    public void tearDown() throws Exception {
        xmlRecord = null;
        xmlRecordMapper = null;
        System.gc();
    }
}
