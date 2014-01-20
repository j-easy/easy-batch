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
import io.github.benas.easybatch.flatfile.flr.FixedLengthRecordMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test class for {@link io.github.benas.easybatch.flatfile.flr.FixedLengthRecordMapper}.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class FixedLengthRecordMapperTest {

    private FixedLengthRecordMapper fixedLengthRecordMapper;

    private StringRecord stringRecord;

    @Before
    public void setUp() throws Exception {
        int[] fieldsLength = {4, 2, 3, 5};
        fixedLengthRecordMapper = new FixedLengthRecordMapper<Person>(Person.class, fieldsLength,
                new String[]{"field1", "field2", "field3", "field4"});
        stringRecord = new StringRecord(1, "aaaabbcccddddd");
    }

    @Test (expected = Exception.class)
    public void testRecordWellFormednessKO() throws Exception {
        stringRecord = new StringRecord(1, "aaaabbcccdddddd"); // unexpected record size
        fixedLengthRecordMapper.parseRecord(stringRecord);
    }

    @Test
    public void testRecordParsing() throws Exception {
        FlatFileRecord flatFileRecord = fixedLengthRecordMapper.parseRecord(stringRecord);
        assertEquals(4, flatFileRecord.getFlatFileFields().size());
        assertEquals("aaaa",flatFileRecord.getFlatFileFields().get(0).getRawContent());
        assertEquals("bb",flatFileRecord.getFlatFileFields().get(1).getRawContent());
        assertEquals("ccc",flatFileRecord.getFlatFileFields().get(2).getRawContent());
        assertEquals("ddddd",flatFileRecord.getFlatFileFields().get(3).getRawContent());
    }

    @After
    public void tearDown() throws Exception {
        fixedLengthRecordMapper = null;
        stringRecord = null;
        System.gc();
    }
}
