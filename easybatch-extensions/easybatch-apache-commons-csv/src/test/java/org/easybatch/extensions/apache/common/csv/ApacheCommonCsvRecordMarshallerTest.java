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

import org.easybatch.core.marshaller.RecordMarshallingException;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ApacheCommonCsvRecordMarshallerTest {

    @Mock
    private Header header;

    private ApacheCommonCsvRecordMarshaller marshaller;

    @Before
    public void setUp() throws Exception {
        marshaller = new ApacheCommonCsvRecordMarshaller(Foo.class, new String[]{"firstName", "lastName", "married"}, ';', '\'');
    }

    @Test
    public void marshal() throws RecordMarshallingException {
        Foo foo = new Foo();
        foo.setFirstName("foo");
        foo.setLastName("bar");
        GenericRecord<Foo> record = new GenericRecord<>(header, foo);

        String expected = "'foo';'bar';'false'";
        String actual = marshaller.processRecord(record).getPayload();

        assertThat(actual).isEqualTo(expected);
    }
}
