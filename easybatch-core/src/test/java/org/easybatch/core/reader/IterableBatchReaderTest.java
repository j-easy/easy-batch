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

import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;

@RunWith(MockitoJUnitRunner.class)
public class IterableBatchReaderTest {

    public static final int BATCH_SIZE = 2;

    @Mock
    private Object record1, record2, record3, record4, record5;

    @SuppressWarnings("unchecked")
    @Test
    public void testBatchReading() throws Exception {

        List<Object> dataSource = asList(record1, record2, record3, record4, record5);

        JobReport jobReport = aNewJob()
                .reader(new IterableBatchReader(dataSource, BATCH_SIZE))
                .processor(new RecordCollector())
                .call();

        List<Batch> batches = (List<Batch>) jobReport.getResult();

        assertThat(batches).hasSize(3);

        List<Record> batch1 = batches.get(0).getPayload();
        assertThat(batch1).extracting("payload").containsExactly(record1, record2);

        List<Record> batch2 = batches.get(1).getPayload();
        assertThat(batch2).extracting("payload").containsExactly(record3, record4);

        List<Record> batch3 = batches.get(2).getPayload();
        assertThat(batch3).extracting("payload").containsExactly(record5);
    }
}
