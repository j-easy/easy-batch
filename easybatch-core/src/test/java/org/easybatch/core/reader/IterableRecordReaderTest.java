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

package org.easybatch.core.reader;

import org.easybatch.core.record.GenericRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class IterableRecordReaderTest {

    private static final String EXPECTED_DATA_SOURCE_NAME = "In-Memory Iterable";

    private static final String RECORD = "Foo";

    @Mock
    private Iterable<String> dataSource;

    @Mock
    private Iterator<String> iterator;

    private IterableRecordReader iterableRecordReader;

    @Before
    public void setUp() throws Exception {
        when(dataSource.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(RECORD);
        iterableRecordReader = new IterableRecordReader(dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDataSourceIsNull_thenShouldThrowAnIllegalArgumentException() throws Exception {
        iterableRecordReader = new IterableRecordReader(null);
    }

    @Test
    public void testHasNextRecord() throws Exception {
        assertThat(iterableRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void testReadNextRecord() throws Exception {
        GenericRecord genericRecord = iterableRecordReader.readNextRecord();
        assertThat(genericRecord).isNotNull();
        assertThat(genericRecord.getPayload()).isEqualTo(RECORD);
    }

    @Test
    public void testGetTotalRecords() throws Exception {
        assertThat(iterableRecordReader.getTotalRecords()).isNull();
    }

    @Test
    public void testGetDataSourceName() throws Exception {
        assertThat(iterableRecordReader.getDataSourceName()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);

    }
}
