/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.extensions.opencsv.test;

import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.StringRecord;
import org.easybatch.extensions.opencsv.OpenCsvRecordMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

@RunWith(MockitoJUnitRunner.class)
public class OpenCsvRecordMapperTest {

    @Mock
    private StringRecord record;
    @Mock
    private Header header;

    private OpenCsvRecordMapper<Foo> openCsvRecordMapper;

    @Before
    public void setUp() throws Exception {
        openCsvRecordMapper = new OpenCsvRecordMapper<>(Foo.class, "firstName", "lastName", "age", "married");
    }

    @Test
    public void testOpenCsvMapping() throws Exception {
        StringRecord fooRecord = new StringRecord(header, "foo,bar,15,true");
        GenericRecord<Foo> actual = openCsvRecordMapper.processRecord(fooRecord);
        assertThat(actual.getHeader()).isEqualTo(header);

        Foo foo = actual.getPayload();
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
        GenericRecord<Foo> actual = openCsvRecordMapper.processRecord(fooRecord);
        assertThat(actual.getHeader()).isEqualTo(header);

        Foo foo = actual.getPayload();
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo");
        assertThat(foo.getLastName()).isEqualTo("bar");
    }

    @Test
    public void testOpenCsvQualifier() throws Exception {
        openCsvRecordMapper.setQualifier('\'');
        StringRecord fooRecord = new StringRecord(header, "'foo,s','bar'");
        GenericRecord<Foo> actual = openCsvRecordMapper.processRecord(fooRecord);
        assertThat(actual.getHeader()).isEqualTo(header);

        Foo foo = actual.getPayload();
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo,s");
        assertThat(foo.getLastName()).isEqualTo("bar");
    }

    @Ignore("Todo: bug in open CSV? 'foo\r\n' is mapped to 'foo\n' on MS windows..")
    @Test
    public void testOpenCsvCarriageReturn() throws Exception {
        openCsvRecordMapper.setQualifier('\'');
        StringRecord fooRecord = new StringRecord(header, "'foo" + LINE_SEPARATOR + "','bar" + LINE_SEPARATOR + "'");
        GenericRecord<Foo> actual = openCsvRecordMapper.processRecord(fooRecord);
        assertThat(actual.getHeader()).isEqualTo(header);

        Foo foo = actual.getPayload();
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo" + LINE_SEPARATOR);
        assertThat(foo.getLastName()).isEqualTo("bar" + LINE_SEPARATOR);
    }

}
