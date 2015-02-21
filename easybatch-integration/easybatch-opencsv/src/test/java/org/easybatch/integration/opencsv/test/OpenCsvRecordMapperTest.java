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

import org.easybatch.core.api.Header;
import org.easybatch.core.record.StringRecord;
import org.easybatch.integration.opencsv.OpenCsvRecordMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link OpenCsvRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class OpenCsvRecordMapperTest {

    private OpenCsvRecordMapper<Foo> openCsvRecordMapper;

    private Header header;

    @Before
    public void setUp() throws Exception {
        openCsvRecordMapper = new OpenCsvRecordMapper<Foo>(Foo.class, new String[]{"firstName", "lastName", "age", "married"});
        header = new Header(1l, "DataSource", new Date());
    }

    @Test
    public void testOpenCsvMapping() throws Exception {
        StringRecord fooRecord = new StringRecord(header, "foo,bar,15,true");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo");
        assertThat(foo.getLastName()).isEqualTo("bar");
        assertThat(foo.getAge()).isEqualTo(15);
        assertThat(foo.isMarried()).isTrue();
    }

    @Test
    public void testOpenCsvDelimiter() throws Exception {
        openCsvRecordMapper.setDelimiter(';');
        StringRecord fooRecord = new StringRecord(header, "foo;bar");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo");
        assertThat(foo.getLastName()).isEqualTo("bar");
    }

    @Test
    public void testOpenCsvQualifier() throws Exception {
        openCsvRecordMapper.setQualifier('\'');
        StringRecord fooRecord = new StringRecord(header, "'foo,s','bar'");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo,s");
        assertThat(foo.getLastName()).isEqualTo("bar");
    }

    @Test
    public void testOpenCsvCarriageReturn() throws Exception {
        openCsvRecordMapper.setQualifier('\'');
        StringRecord fooRecord = new StringRecord(header, "'foo\n','bar\n'");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo\n");
        assertThat(foo.getLastName()).isEqualTo("bar\n");
    }

}
