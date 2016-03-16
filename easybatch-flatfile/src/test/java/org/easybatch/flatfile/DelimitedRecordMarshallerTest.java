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

package org.easybatch.flatfile;

import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DelimitedRecordMarshallerTest {

    @Mock
    private Header header;
    @Mock
    private Person payload;
    @Mock
    private GenericRecord<Person> record;

    private DelimitedRecordMarshaller marshaller;

    @Before
    public void setUp() throws Exception {
        when(record.getHeader()).thenReturn(header);
        when(record.getPayload()).thenReturn(payload);
        when(payload.getFirstName()).thenReturn("foo");
        when(payload.getLastName()).thenReturn("bar");
        marshaller = new DelimitedRecordMarshaller(Person.class, "firstName", "lastName", "married");
    }

    @Test
    public void marshal() throws Exception {
        String expectedPayload = "\"foo\",\"bar\",\"false\"";
        StringRecord actual = marshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }
}
