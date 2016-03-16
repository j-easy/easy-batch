/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.extensions.apache.common.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

@RunWith(MockitoJUnitRunner.class)
public class ApacheCommonCsvRecordMapperTest {

    private ApacheCommonCsvRecordMapper mapper;

    @Mock
    private Header header;

    @Before
    public void setUp() throws Exception {
        mapper = new ApacheCommonCsvRecordMapper(Foo.class);
    }

    @Test
    public void testApacheCommonCsvMapping() throws Exception {
        StringReader stringReader = new StringReader("foo,bar,15,true");
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("firstName", "lastName", "age", "married");
        ApacheCommonCsvRecord record = getApacheCommonCsvRecord(stringReader, csvFormat);

        GenericRecord<Foo> actual = mapper.processRecord(record);
        Foo foo = actual.getPayload();

        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo");
        assertThat(foo.getLastName()).isEqualTo("bar");
        assertThat(foo.getAge()).isEqualTo(15);
        assertThat(foo.isMarried()).isTrue();
    }

    @Test
    public void testApacheCommonCsvDelimiter() throws Exception {
        StringReader stringReader = new StringReader("foo;bar;15;true");
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withHeader("firstName", "lastName", "age", "married");
        ApacheCommonCsvRecord record = getApacheCommonCsvRecord(stringReader, csvFormat);

        GenericRecord<Foo> actual = mapper.processRecord(record);
        Foo foo = actual.getPayload();

        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo");
        assertThat(foo.getLastName()).isEqualTo("bar");
        assertThat(foo.getAge()).isEqualTo(15);
        assertThat(foo.isMarried()).isTrue();
    }

    @Test
    public void testApacheCommonCsvQualifier() throws Exception {
        StringReader stringReader = new StringReader("'foo,s','bar,n'");
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withQuote('\'')
                .withHeader("firstName", "lastName", "age", "married");
        ApacheCommonCsvRecord record = getApacheCommonCsvRecord(stringReader, csvFormat);

        GenericRecord<Foo> actual = mapper.processRecord(record);
        Foo foo = actual.getPayload();

        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo,s");
        assertThat(foo.getLastName()).isEqualTo("bar,n");
        assertThat(foo.getAge()).isEqualTo(0);
        assertThat(foo.isMarried()).isFalse();
    }

    @Test
    public void testApacheCommonCsvLineFeed() throws Exception {
        StringReader stringReader = new StringReader("'foo" + LINE_SEPARATOR + "','bar" + LINE_SEPARATOR + "'");
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withQuote('\'')
                .withHeader("firstName", "lastName", "age", "married");
        ApacheCommonCsvRecord record = getApacheCommonCsvRecord(stringReader, csvFormat);

        GenericRecord<Foo> actual = mapper.processRecord(record);
        Foo foo = actual.getPayload();

        assertThat(foo).isNotNull();
        assertThat(foo.getFirstName()).isEqualTo("foo" + LINE_SEPARATOR);
        assertThat(foo.getLastName()).isEqualTo("bar" + LINE_SEPARATOR);
        assertThat(foo.getAge()).isEqualTo(0);
        assertThat(foo.isMarried()).isFalse();
    }

    private ApacheCommonCsvRecord getApacheCommonCsvRecord(StringReader stringReader, CSVFormat csvFormat) throws IOException {
        CSVParser parser = new CSVParser(stringReader, csvFormat);
        CSVRecord csvRecord = parser.iterator().next();
        return new ApacheCommonCsvRecord(header, csvRecord);
    }

}
