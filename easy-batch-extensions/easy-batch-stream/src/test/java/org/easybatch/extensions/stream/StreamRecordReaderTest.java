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
package org.easybatch.extensions.stream;

import org.easybatch.core.record.Record;
import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Charles Fleury
 * @since 5.0
 */
public class StreamRecordReaderTest {

    @Test
    public void whenStreamIsNotEmpty_thenThereShouldBeANextRecordToRead() throws Exception {
        Stream<String> stream = Stream.of("a", "b");
        StreamRecordReader<String> streamRecordReader = new StreamRecordReader<>(stream);
        streamRecordReader.open();

        Record<String> record1 = streamRecordReader.readRecord();
        assertThat(record1).isNotNull();
        assertThat(record1.getHeader().getNumber().equals(1L));
        assertThat(record1.getHeader().getSource().equals("In-Memory Stream"));
        assertThat(record1.getPayload().equals("a"));

        Record<String> record2 = streamRecordReader.readRecord();
        assertThat(record2).isNotNull();
        assertThat(record1.getHeader().getNumber().equals(2L));
        assertThat(record2.getHeader().getSource().equals("In-Memory Stream"));
        assertThat(record2.getPayload().equals("b"));

        Record<String> record3 = streamRecordReader.readRecord();
        assertThat(record3).isNull();
    }

    @Test
    public void whenStreamIsEmpty_thenThereShouldBeNoNextRecordToRead() throws Exception {
        Stream<String> stream = Stream.empty();
        StreamRecordReader<String> streamRecordReader = new StreamRecordReader<>(stream);
        streamRecordReader.open();

        Record<String> record = streamRecordReader.readRecord();
        assertThat(record).isNull();
    }

}
