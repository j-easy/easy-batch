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

import net.benas.cb4j.core.impl.RecordParserImpl;
import net.benas.cb4j.core.model.Field;
import net.benas.cb4j.core.model.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Unit test class for {@link net.benas.cb4j.core.model.Record}
 * @author benas (md.benhassine@gmail.com)
 */
public class RecordTest {

    private Record record;

    @Before
    public void setUp() throws Exception {
        RecordParserImpl recordParser = new RecordParserImpl(2, ",",false,"");
        record = recordParser.parseRecord("Hello,World", 1);
    }

    @Test
    public void testGetRecordContentAsString() throws Exception{
        assertEquals("Hello,World", record.getContentAsString());
    }

    @Test
    public void testGetFieldByIndex() throws Exception {
        Field field = record.getFieldByIndex(0);
        assertEquals(0,field.getIndex());
        assertEquals("Hello",field.getContent());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldByIndexKo() throws Exception {
        record.getFieldByIndex(2);
    }

    @Test
    public void testGetFieldContentByIndex() throws Exception {
        assertEquals("World", record.getFieldContentByIndex(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldContentByIndexKo() throws Exception {
        assertEquals("World", record.getFieldContentByIndex(2));
    }

    @Test
    public void testGetRecordContentAsStringWithDataEnclosingCharacter() throws Exception{
        RecordParserImpl recordParser = new RecordParserImpl(2, ",",false,"'");
        String originalRecord = "'Hello','World'";
        record = recordParser.parseRecord(originalRecord, 1);
        assertEquals(originalRecord, record.getContentAsString());
    }

    @After
    public void tearDown() throws Exception {
        System.gc();
    }

}
