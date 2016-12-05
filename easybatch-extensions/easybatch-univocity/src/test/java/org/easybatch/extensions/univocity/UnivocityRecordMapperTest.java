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

package org.easybatch.extensions.univocity;

import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

@RunWith(MockitoJUnitRunner.class)
public class UnivocityRecordMapperTest {

    @Mock
    private Header header;

    private UnivocityRecordMapper<TestBean> univocityRecordMapper;

    @Before
    public void setUp() throws Exception {
        univocityRecordMapper = new UnivocityRecordMapper<>(TestBean.class);
    }

    @Test
    public void testUnivocityMapping() throws Exception {
        StringRecord fooRecord = new StringRecord(header, "foo,bar,15,true");
        Record<TestBean> actual = univocityRecordMapper.processRecord(fooRecord);
        assertThat(actual.getHeader()).isEqualTo(header);

        TestBean foo = actual.getPayload();
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo");
        assertThat(foo.getLastName()).isEqualTo("bar");
        assertThat(foo.getAge()).isEqualTo(15);
        assertThat(foo.isMarried()).isTrue();
    }

    @Test
    public void testUnivocityDelimiter() throws Exception {
        univocityRecordMapper.setDelimiter(';');
        StringRecord fooRecord = new StringRecord(header, "foo;bar");
        Record<TestBean> actual = univocityRecordMapper.processRecord(fooRecord);
        assertThat(actual.getHeader()).isEqualTo(header);

        TestBean foo = actual.getPayload();
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo");
        assertThat(foo.getLastName()).isEqualTo("bar");
    }

    @Test
    public void testUnivocityQualifier() throws Exception {
        univocityRecordMapper.setQualifier('\"');
        StringRecord fooRecord = new StringRecord(header, "\"foo,s\",\"bar\"");
        Record<TestBean> actual = univocityRecordMapper.processRecord(fooRecord);
        assertThat(actual.getHeader()).isEqualTo(header);

        TestBean foo = actual.getPayload();
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo,s");
        assertThat(foo.getLastName()).isEqualTo("bar");
    }

    @Test
    public void testUnivocityStrictQualifier() throws Exception {
        univocityRecordMapper.setStrictQualifiers(true);
        StringRecord fooRecord = new StringRecord(header, "\'foo\'x,\'bar\'"); // characters outside quotes (x) should be ignored
        Record<TestBean> actual = univocityRecordMapper.processRecord(fooRecord);
        assertThat(actual.getHeader()).isEqualTo(header);

        TestBean foo = actual.getPayload();
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo");
        assertThat(foo.getLastName()).isEqualTo("bar");
    }

    @Test
    public void testUnivocityCarriageReturn() throws Exception {
        univocityRecordMapper.setQualifier('\'');
        StringRecord fooRecord = new StringRecord(header, "'foo" + LINE_SEPARATOR + "','bar" + LINE_SEPARATOR + "'");
        Record<TestBean> actual = univocityRecordMapper.processRecord(fooRecord);
        assertThat(actual.getHeader()).isEqualTo(header);

        TestBean foo = actual.getPayload();
        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo" + LINE_SEPARATOR);
        assertThat(foo.getLastName()).isEqualTo("bar" + LINE_SEPARATOR);
    }

}
