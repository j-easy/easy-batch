/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.extensions.univocity;

import com.univocity.parsers.csv.CsvWriterSettings;
import com.univocity.parsers.fixed.FixedWidthFields;
import com.univocity.parsers.fixed.FixedWidthWriterSettings;
import com.univocity.parsers.tsv.TsvWriterSettings;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnivocityRecordMarshallerTest {

    @Mock
    private Header header;
    @Mock
    private Record<TestBean> record;

    private UnivocityCsvRecordMarshaller<TestBean> csvRecordMarshaller;
    private UnivocityTsvRecordMarshaller<TestBean> tsvRecordMarshaller;
    private UnivocityFixedWidthRecordMarshaller<TestBean> fixedWidthRecordMarshaller;

    @Before
    public void setUp() throws Exception {
        setUpCsvMarshaller();
        setUpTsvMarshaller();
        setUpFixedWidthMarshaller();
        TestBean foo = new TestBean();
        foo.setFirstName("foo");
        foo.setLastName("bar");
        foo.setAge(30);
        foo.setMarried(true);
        when(record.getHeader()).thenReturn(header);
        when(record.getPayload()).thenReturn(foo);
    }

    private void setUpCsvMarshaller() throws Exception {
        CsvWriterSettings settings = new CsvWriterSettings();
        settings.setQuoteAllFields(true);
        csvRecordMarshaller = new UnivocityCsvRecordMarshaller<>(TestBean.class, settings,  "firstName", "lastName", "age", "married");
    }

    private void setUpTsvMarshaller() throws Exception {
        TsvWriterSettings settings = new TsvWriterSettings();
        tsvRecordMarshaller = new UnivocityTsvRecordMarshaller<>(TestBean.class, settings,  "firstName", "lastName", "age", "married");
    }

    private void setUpFixedWidthMarshaller() throws Exception {
        FixedWidthWriterSettings settings = new FixedWidthWriterSettings(new FixedWidthFields(4,4,3,4));
        fixedWidthRecordMarshaller = new UnivocityFixedWidthRecordMarshaller<>(TestBean.class, settings,  "firstName", "lastName", "age", "married");
    }


    @Test
    public void processRecordIntoCsv() throws Exception {
        String expectedPayload = "\"foo\",\"bar\",\"30\",\"true\"" + LINE_SEPARATOR;
        StringRecord actual = csvRecordMarshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

    @Test
    public void processRecordIntoTsv() throws Exception {
        String expectedPayload = "foo\tbar\t30\ttrue" + LINE_SEPARATOR;
        StringRecord actual = tsvRecordMarshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

    @Test
    public void processRecordIntoFixedWidth() throws Exception {
        String expectedPayload = "foo bar 30 true" + LINE_SEPARATOR;
        StringRecord actual = fixedWidthRecordMarshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

}
