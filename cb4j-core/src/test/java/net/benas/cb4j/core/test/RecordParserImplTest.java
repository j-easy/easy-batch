/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.core.test;

import net.benas.cb4j.core.api.RecordParser;
import net.benas.cb4j.core.impl.RecordParserImpl;
import net.benas.cb4j.core.model.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test class for {@link RecordParserImpl}
 * @author benas (md.benhassine@gmail.com)
 */
public class RecordParserImplTest {

    private RecordParser recordParser;

    private String record;

    @Before
    public void setUp() throws Exception {
        recordParser = new RecordParserImpl(3,",");
        record = "hello,cb4j,world";
    }

    @Test
    public void testRecordWellFormedness() throws Exception {
        assertTrue(recordParser.isWellFormed(record));
    }

    @Test
    public void testRecordWellFormednessKO() throws Exception {
        record = "hello,world";
        assertFalse(recordParser.isWellFormed(record));
    }

    @Test
    public void testRecordSize() throws Exception {
        assertEquals(3, recordParser.getRecordSize(record));
    }

    @Test
    public void testRecordSizeWithEmptyField() throws Exception {
        record = "hello,cb4j,world,";
        assertEquals(4, recordParser.getRecordSize(record));
        Record parsedRecord = recordParser.parseRecord(record, 1);
        assertEquals("",parsedRecord.getFieldContentByIndex(3));
    }

    @Test
    public void testRecordParsing() throws Exception {
        Record parsedRecord = recordParser.parseRecord(record, 1);
        assertEquals(3, parsedRecord.getFields().size());
        assertEquals("hello",parsedRecord.getFieldContentByIndex(0));
        assertEquals("cb4j",parsedRecord.getFieldContentByIndex(1));
        assertEquals("world",parsedRecord.getFieldContentByIndex(2));
        assertEquals(1,parsedRecord.getNumber());

    }

    @Test
    public void testRecordParsingWithPipeSeparator() throws Exception {
        recordParser = new RecordParserImpl(3,"|");
        record = "hello|cb4j|world";
        assertTrue(recordParser.isWellFormed(record));
        assertEquals(3, recordParser.getRecordSize(record));
        Record parsedRecord = recordParser.parseRecord(record, 1);
        assertEquals("hello",parsedRecord.getFieldContentByIndex(0));
        assertEquals("cb4j",parsedRecord.getFieldContentByIndex(1));
        assertEquals("world",parsedRecord.getFieldContentByIndex(2));
    }

    @After
    public void tearDown() throws Exception {
        recordParser = null;
        record = null;
        System.gc();
    }
}
