/*
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
package org.jeasy.batch.flatfile;

import org.jeasy.batch.core.record.Header;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FixedLengthRecordMarshallerTest {

    @Mock
    private Header header;
    @Mock
    private Bean payload;
    @Mock
    private Record<Bean> record;

    private FixedLengthRecordMarshaller<Bean> marshaller;

    @Before
    public void setUp() {
        when(record.getHeader()).thenReturn(header);
        when(record.getPayload()).thenReturn(payload);
        when(payload.getField1()).thenReturn("aaa");
        when(payload.getField2()).thenReturn("bb");
        when(payload.getField3()).thenReturn("cc");
        when(payload.getField4()).thenReturn("dddd");
        marshaller = new FixedLengthRecordMarshaller<>(Bean.class, "%3s%-3s%3s%3s", "field1", "field2", "field3", "field4");
    }

    @Test
    public void marshal() throws Exception {
        String expectedPayload = "aaabb  ccdddd";
        // field1: exact length: "aaa" -> "aaa"
        // field2: right padded to 3 characters: "bb" -> "bb "
        // field3: left padded to 3 characters: "cc" -> " cc"
        // field4: longer than length => Not truncated as documented in Javadoc : "dddd" -> "dddd"
        StringRecord actual = marshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }
}
