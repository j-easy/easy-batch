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

package org.easybatch.core.processor;

import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobStatus;
import org.easybatch.core.reader.IterableBatchReader;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchProcessorTest {

    @Mock
    private Record record, processedRecord;
    @Mock
    private Batch batch;
    @Mock
    private Header header;
    @Mock
    private RecordProcessor<Record, Record> recordProcessor;
    @Mock
    private RecordProcessingException recordProcessingException;

    private BatchProcessor batchProcessor;

    @Before
    public void setUp() throws Exception {
        batchProcessor = new BatchProcessor(recordProcessor);
        List<Record> records = new ArrayList<>();
        records.add(record);
        when(batch.getHeader()).thenReturn(header);
        when(batch.getPayload()).thenReturn(records);
    }

    @Test
    public void processBatchWithoutException() throws Exception {
        when(recordProcessor.processRecord(record)).thenReturn(processedRecord);
        try {
            Batch actual = batchProcessor.processRecord(this.batch);
            assertThat(actual).isNotNull();
            assertThat(actual.getHeader()).isEqualTo(header);
            assertThat(actual.getPayload()).containsExactly(processedRecord);
        } catch (RecordProcessingException e) {
            fail("No exception should be thrown for the batch when all its records are processed without errors");
        }
    }

    @Test(expected = RecordProcessingException.class)
    public void processBatchWithException() throws Exception {
        when(recordProcessor.processRecord(record)).thenThrow(recordProcessingException);
        batchProcessor.processRecord(batch);
        verify(recordProcessor).processRecord(record);
    }

    @Test
    public void integrationTest() throws Exception {
        List<String> strings = Arrays.asList("foo", "bar", "baz");

        JobReport report = JobBuilder.aNewJob()
                .reader(new IterableBatchReader(strings, 2))
                .processor(new BatchProcessor(new RecordProcessor<GenericRecord<String>, GenericRecord<String>>() {
                    @Override
                    public GenericRecord<String> processRecord(GenericRecord<String> record) throws RecordProcessingException {
                        return record;
                    }
                })).call();

        assertThat(report.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(report.getMetrics().getTotalCount()).isEqualTo(2);// 2 batches: ["foo","bar"] and ["baz"]
        assertThat(report.getMetrics().getSuccessCount()).isEqualTo(2);
    }
}
