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

package org.easybatch.core.validator;

import org.assertj.core.api.Assertions;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobStatus;
import org.easybatch.core.mapper.BatchMapper;
import org.easybatch.core.mapper.GenericRecordMapper;
import org.easybatch.core.reader.IterableBatchReader;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.GenericRecord;
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
@SuppressWarnings("unchecked")
public class BatchValidatorTest {

    @Mock
    private Record record;
    @Mock
    private Batch batch;
    @Mock
    private RecordValidator recordValidator;
    @Mock
    private RecordValidationException recordValidationException;

    private BatchValidator batchValidator;

    @Before
    public void setUp() throws Exception {
        batchValidator = new BatchValidator(recordValidator);
        List<Record> records = new ArrayList<>();
        records.add(record);
        when(batch.getPayload()).thenReturn(records);
    }

    @Test
    public void processBatchWithoutException() throws Exception {
        when(recordValidator.processRecord(record)).thenReturn(record);
        try {
            batchValidator.processRecord(batch);
            verify(recordValidator).processRecord(record);
        } catch (RecordValidationException e) {
            fail("If all records are valid, the batch should be valid");
        }
    }

    @Test(expected = RecordValidationException.class)
    public void processBatchWithException() throws Exception {
        when(recordValidator.processRecord(record)).thenThrow(recordValidationException);
        batchValidator.processRecord(batch);
        verify(recordValidator).processRecord(record);
    }

    @Test
         public void integrationTestWithoutBatchMapping() throws Exception {
        List<String> strings = Arrays.asList("foo", "bar", "baz");

        JobReport report = JobBuilder.aNewJob()
                .reader(new IterableBatchReader<>(strings, 2))
                .validator(new BatchValidator(new RecordValidator<GenericRecord>() {
                    @Override
                    public GenericRecord processRecord(GenericRecord record) throws RecordValidationException {
                        String payload = (String) record.getPayload();
                        if (payload.startsWith("b")) {
                            throw new RecordValidationException("Record " + record + " is invalid");
                        }
                        return record;
                    }
                })).call();

        assertThat(report.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(report.getMetrics().getTotalCount()).isEqualTo(2);// 2 batches: ["foo","bar"] and ["baz"]
        assertThat(report.getMetrics().getErrorCount()).isEqualTo(2);// both batches are invalid
    }

    @Test
    public void integrationTestWithBatchMapping() throws Exception {
        List<String> strings = Arrays.asList("foo", "bar", "baz");

        JobReport report = JobBuilder.aNewJob()
                .reader(new IterableBatchReader<>(strings, 2))
                .mapper(new BatchMapper(new GenericRecordMapper()))
                .validator(new BatchValidator(new RecordValidator<String>() {
                    @Override
                    public String processRecord(String record) throws RecordValidationException {
                        if (record.startsWith("b")) {
                            throw new RecordValidationException("Record " + record + " is invalid");
                        }
                        return record;
                    }
                })).call();

        assertThat(report.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(report.getMetrics().getTotalCount()).isEqualTo(2);// 2 batches: ["foo","bar"] and ["baz"]
        assertThat(report.getMetrics().getErrorCount()).isEqualTo(2);// both batches are invalid
    }
}
