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

package org.easybatch.integration.opencsv.test;

import org.easybatch.core.record.StringRecord;
import org.easybatch.integration.opencsv.OpenCsvRecordMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link OpenCsvRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class OpenCsvRecordMapperTest {

    private OpenCsvRecordMapper<Foo> openCsvRecordMapper;

    @Before
    public void setUp() throws Exception {
        openCsvRecordMapper = new OpenCsvRecordMapper<Foo>(Foo.class, new String[]{"firstName", "lastName", "age", "married"});
    }

    @Test
    public void testOpenCsvMapping() throws Exception {
        StringRecord fooRecord = new StringRecord(1, "foo,bar,15,true");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        Assert.assertNotNull(foo);
        Assert.assertEquals("foo", foo.getFirstName());
        Assert.assertEquals("bar", foo.getLastName());
        Assert.assertEquals(15, foo.getAge());
        Assert.assertEquals(true, foo.isMarried());
    }

    @Test
    public void testOpenCsvDelimiter() throws Exception {
        openCsvRecordMapper.setDelimiter(';');
        StringRecord fooRecord = new StringRecord(1, "foo;bar");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        Assert.assertNotNull(foo);
        Assert.assertEquals("foo", foo.getFirstName());
        Assert.assertEquals("bar", foo.getLastName());
    }

    @Test
    public void testOpenCsvQualifier() throws Exception {
        openCsvRecordMapper.setQualifier('\'');
        StringRecord fooRecord = new StringRecord(1, "'foo,s','bar'");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        Assert.assertNotNull(foo);
        Assert.assertEquals("foo,s", foo.getFirstName());
        Assert.assertEquals("bar", foo.getLastName());
    }

    @Test
    public void testOpenCsvCarriageReturn() throws Exception {
        openCsvRecordMapper.setQualifier('\'');
        StringRecord fooRecord = new StringRecord(1, "'foo\n','bar\n'");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        Assert.assertNotNull(foo);
        Assert.assertEquals("foo\n", foo.getFirstName());
        Assert.assertEquals("bar\n", foo.getLastName());
    }

}
