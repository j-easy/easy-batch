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

package org.easybatch.core.mapper;

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchMapperTest {

    @Mock
    private Record record1, record2, mappedRecord1, mappedRecord2;
    @Mock
    private Batch batch;
    @Mock
    private Header header;
    @Mock
    private RecordMapper<Record, Record> recordMapper;

    private BatchMapper batchMapper;

    @Before
    public void setUp() throws Exception {
        batchMapper = new BatchMapper(recordMapper);
        when(recordMapper.processRecord(record1)).thenReturn(mappedRecord1);
        when(recordMapper.processRecord(record2)).thenReturn(mappedRecord2);
        when(batch.getHeader()).thenReturn(header);
        when(batch.getPayload()).thenReturn(Arrays.asList(record1, record2));
    }

    @Test
    public void processRecord() throws RecordMappingException {
        Batch actual = batchMapper.processRecord(batch);

        assertThat(actual).isNotNull();
        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).containsExactly(mappedRecord1, mappedRecord2);
    }

}
