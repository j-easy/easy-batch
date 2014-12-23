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
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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
