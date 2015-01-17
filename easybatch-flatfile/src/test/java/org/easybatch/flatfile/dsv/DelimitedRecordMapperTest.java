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

package org.easybatch.flatfile.dsv;

import org.easybatch.flatfile.FlatFileField;
import org.easybatch.flatfile.FlatFileRecord;
import org.easybatch.core.util.StringRecord;
import org.easybatch.flatfile.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Unit test class for {@link org.easybatch.flatfile.dsv.DelimitedRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class DelimitedRecordMapperTest {

    private DelimitedRecordMapper delimitedRecordMapper;

    private StringRecord stringRecord;

    @Before
    public void setUp() throws Exception {
        delimitedRecordMapper = new DelimitedRecordMapper<Person>(Person.class,
                new String[]{"firstName", "lastName", "age", "birthDate", "married"});
        stringRecord = new StringRecord(1, "foo,bar,30,1990-12-12,true");
    }

    /*
     * Record parsing tests
     */

    @Test(expected = Exception.class)
    public void testIllFormedRecord() throws Exception {
        stringRecord = new StringRecord(1, "foo,bar,30,1990-12-12");// incorrect record size
        delimitedRecordMapper.parseRecord(stringRecord);
    }


    @Test
    public void testRecordSizeWithEmptyField() throws Exception {
        stringRecord = new StringRecord(1, "foo,bar,30,1990-12-12,");
        FlatFileRecord flatFileRecord = delimitedRecordMapper.parseRecord(stringRecord);
        assertEquals("", flatFileRecord.getFlatFileFields().get(4).getRawContent());
    }

    @Test
    public void testRecordParsing() throws Exception {
        validateRecord(stringRecord);
    }

    @Test
    public void testRecordParsingWithTrimmedWhitespaces() throws Exception {
        delimitedRecordMapper.setTrimWhitespaces(true);
        stringRecord = new StringRecord(1, "  foo ,    bar  ,  30  ,     1990-12-12  ,  true         ");
        validateRecord(stringRecord);
    }

    @Test
    public void testRecordParsingWithPipeDelimiter() throws Exception {
        delimitedRecordMapper.setDelimiter("|");
        stringRecord = new StringRecord(1, "foo|bar|30|1990-12-12|true");
        validateRecord(stringRecord);
    }

    @Test
    public void testRecordParsingWithSpaceDelimiter() throws Exception {
        delimitedRecordMapper.setDelimiter(" ");
        stringRecord = new StringRecord(1, "foo bar 30 1990-12-12 true");
        validateRecord(stringRecord);
    }

    @Test
    public void testRecordParsingWithTabDelimiter() throws Exception {
        delimitedRecordMapper.setDelimiter("\t");
        stringRecord = new StringRecord(1, "foo\tbar\t30\t1990-12-12\ttrue");
        validateRecord(stringRecord);
    }

    @Test
    public void testRecordParsingWithMultipleCharacterDelimiter() throws Exception {
        delimitedRecordMapper.setDelimiter("###");
        stringRecord = new StringRecord(1, "foo###bar###30###1990-12-12###true");
        validateRecord(stringRecord);
    }

    @Test
    public void testRecordParsingWithDataQualifierCharacter() throws Exception {
        delimitedRecordMapper.setQualifier("'");
        stringRecord = new StringRecord(1, "'foo','bar','30','1990-12-12','true'");
        validateRecord(stringRecord);
    }

    @Test
    public void testFieldSubsetMapping() throws Exception {
        delimitedRecordMapper = new DelimitedRecordMapper<Person>(Person.class,
                new Integer[]{1, 5},
                new String[]{"firstName", "married"}
        );
        FlatFileRecord flatFileRecord = delimitedRecordMapper.parseRecord(stringRecord);
        assertNotNull(flatFileRecord);
        assertEquals(2, flatFileRecord.getFlatFileFields().size());
        assertEquals("foo",flatFileRecord.getFlatFileFields().get(0).getRawContent());
        assertEquals("true",flatFileRecord.getFlatFileFields().get(1).getRawContent());
    }

    private void validateRecord(final StringRecord stringRecord) throws Exception {
        FlatFileRecord flatFileRecord = delimitedRecordMapper.parseRecord(stringRecord);
        assertNotNull(flatFileRecord);
        List<FlatFileField> flatFileFields = flatFileRecord.getFlatFileFields();
        assertEquals(5, flatFileFields.size());
        assertEquals("foo", flatFileFields.get(0).getRawContent());
        assertEquals("bar", flatFileFields.get(1).getRawContent());
        assertEquals("30", flatFileFields.get(2).getRawContent());
        assertEquals("1990-12-12", flatFileFields.get(3).getRawContent());
        assertEquals("true", flatFileFields.get(4).getRawContent());
    }

     /*
     * Record mapping tests
     */

    @Test
    public void testFiledNamesConventionOverConfiguration() throws Exception {
        delimitedRecordMapper = new DelimitedRecordMapper<Person>(Person.class);

        delimitedRecordMapper.parseRecord(new StringRecord(1, "firstName,lastName,age,birthDate,married"));
        Person person = (Person) delimitedRecordMapper.mapRecord(stringRecord);
        assertNotNull(person);
        assertEquals("foo", person.getFirstName());
        assertEquals("bar", person.getLastName());
        assertEquals(30, person.getAge());
        assertEquals(java.sql.Date.valueOf("1990-12-12"), person.getBirthDate());
        assertEquals(true, person.isMarried());
    }

    @After
    public void tearDown() throws Exception {
        delimitedRecordMapper = null;
        stringRecord = null;
        System.gc();
    }
}
