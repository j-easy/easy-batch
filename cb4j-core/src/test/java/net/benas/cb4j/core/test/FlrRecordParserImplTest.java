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
import net.benas.cb4j.core.impl.FlrRecordParserImpl;
import net.benas.cb4j.core.model.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test class for {@link net.benas.cb4j.core.impl.FlrRecordParserImpl}
 * @author benas (md.benhassine@gmail.com)
 */
public class FlrRecordParserImplTest {

    private RecordParser recordParser;

    private String record;

    @Before
    public void setUp() throws Exception {
        int[] fieldsLength = {4, 2, 3, 5};
        recordParser = new FlrRecordParserImpl(fieldsLength);
        record = "aaaabbcccddddd";
    }

    @Test
    public void testRecordWellFormedness() throws Exception {
        assertNull(recordParser.analyseRecord(record));
    }

    @Test
    public void testRecordWellFormednessKO() throws Exception {

        record = "aaaabbcccdddddd";
        String error = recordParser.analyseRecord(record);
        assertNotNull(error);
        assertEquals("record length " + 15 + " not equal to expected length of " + 14, error);

        record = "aaaabbcccdddd";
        error = recordParser.analyseRecord(record);
        assertNotNull(error);
        assertEquals("record length " + 13 + " not equal to expected length of " + 14, error);

    }

    @Test
    public void testRecordParsing() throws Exception {
        Record parsedRecord = recordParser.parseRecord(record, 1);
        assertEquals(4, parsedRecord.getFields().size());
        assertEquals("aaaa",parsedRecord.getFieldContentByIndex(0));
        assertEquals("bb",parsedRecord.getFieldContentByIndex(1));
        assertEquals("ccc",parsedRecord.getFieldContentByIndex(2));
        assertEquals("ddddd",parsedRecord.getFieldContentByIndex(3));
        assertEquals(1,parsedRecord.getNumber());
    }

    @After
    public void tearDown() throws Exception {
        recordParser = null;
        record = null;
        System.gc();
    }
}
