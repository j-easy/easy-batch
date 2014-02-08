/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.flatfile.test;

import io.github.benas.easybatch.flatfile.FlatFileRecord;
import io.github.benas.easybatch.core.util.StringRecord;
import io.github.benas.easybatch.flatfile.dsv.DelimitedRecordMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Unit test class for {@link io.github.benas.easybatch.flatfile.dsv.DelimitedRecordMapper}.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class DelimitedRecordMapperTest {

    private DelimitedRecordMapper delimitedRecordMapper;

    private StringRecord stringRecord;

    @Before
    public void setUp() throws Exception {
        delimitedRecordMapper = new DelimitedRecordMapper<Person>(Person.class,
                new String[]{"firstName", "lastName", "age", "birthDate", "isMarried"});
        stringRecord = new StringRecord(1, "foo,bar,30,1990-12-12,true");
    }

    @Test(expected = Exception.class)
    public void testRecordWellFormednessKO() throws Exception {
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

    private void validateRecord(final StringRecord stringRecord) throws Exception {
        assertNotNull(delimitedRecordMapper.parseRecord(stringRecord));
        FlatFileRecord flatFileRecord = delimitedRecordMapper.parseRecord(stringRecord);
        assertEquals(5, flatFileRecord.getFlatFileFields().size());
        assertEquals("foo",flatFileRecord.getFlatFileFields().get(0).getRawContent());
        assertEquals("bar",flatFileRecord.getFlatFileFields().get(1).getRawContent());
        assertEquals("30",flatFileRecord.getFlatFileFields().get(2).getRawContent());
        assertEquals("1990-12-12",flatFileRecord.getFlatFileFields().get(3).getRawContent());
        assertEquals("true",flatFileRecord.getFlatFileFields().get(4).getRawContent());
    }

    @After
    public void tearDown() throws Exception {
        delimitedRecordMapper = null;
        stringRecord = null;
        System.gc();
    }
}
