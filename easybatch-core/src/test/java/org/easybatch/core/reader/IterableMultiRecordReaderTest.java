/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

import org.easybatch.core.api.Record;
import org.easybatch.core.api.Report;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.MultiRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.EngineBuilder.aNewEngine;

@RunWith(MockitoJUnitRunner.class)
public class IterableMultiRecordReaderTest {

    public static final int CHUNK_SIZE = 2;

    @Mock
    private Object record1, record2, record3, record4, record5;

    @SuppressWarnings("unchecked")
    @Test
    public void testMultiRecordReading() throws Exception {

        List<Object> dataSource = asList(record1, record2, record3, record4, record5);

        Report report = aNewEngine()
                .reader(new IterableMultiRecordReader<Object>(dataSource, CHUNK_SIZE))
                .processor(new RecordCollector<Object>())
                .build().call();

        List<MultiRecord> multiRecords = (List<MultiRecord>) report.getJobResult();

        assertThat(multiRecords).isNotNull().isNotEmpty().hasSize(3);

        List<Record> chunk1 = multiRecords.get(0).getPayload();
        assertThat(chunk1).isNotNull().isNotEmpty().hasSize(2);
        assertThat(chunk1.get(0).getPayload()).isEqualTo(record1);
        assertThat(chunk1.get(1).getPayload()).isEqualTo(record2);

        List<Record> chunk2 = multiRecords.get(1).getPayload();
        assertThat(chunk2).isNotNull().isNotEmpty().hasSize(2);
        assertThat(chunk2.get(0).getPayload()).isEqualTo(record3);
        assertThat(chunk2.get(1).getPayload()).isEqualTo(record4);

        List<Record> chunk3 = multiRecords.get(2).getPayload();
        assertThat(chunk3).isNotNull().isNotEmpty().hasSize(1);
        assertThat(chunk3.get(0).getPayload()).isEqualTo(record5);

    }
}