/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.easybatch.core.impl;

import org.easybatch.core.api.*;
import org.easybatch.core.api.event.job.JobEventListener;
import org.easybatch.core.api.event.step.*;
import org.easybatch.core.filter.StartWithStringRecordFilter;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link LocalEventManager}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class LocalEventManagerTest {

    @Mock
    private JobEventListener jobEventListener1, jobEventListener2;
    @Mock
    private RecordReaderEventListener recordReaderEventListener1, recordReaderEventListener2;
    @Mock
    private RecordFilterEventListener recordFilterEventListener1, recordFilterEventListener2;
    @Mock
    private RecordMapperEventListener recordMapperEventListener1, recordMapperEventListener2;
    @Mock
    private RecordValidatorEventListener recordValidatorEventListener1, recordValidatorEventListener2;
    @Mock
    private RecordProcessorEventListener recordProcessorEventListener1, recordProcessorEventListener2;
    @Mock
    private Throwable throwable;
    @Mock
    private Record record, record1, record2;
    @Mock
    private Object mappedRecord;
    @Mock
    private Object processingResult;
    @Mock
    private HashSet<ValidationError> validationErrors;

    private LocalEventManager localEventManager;

    @Before
    public void setUp() {
        localEventManager = new LocalEventManager();

        localEventManager.addJobEventListener(jobEventListener1);
        localEventManager.addJobEventListener(jobEventListener2);

        localEventManager.addRecordFilterEventListener(recordFilterEventListener1);
        localEventManager.addRecordFilterEventListener(recordFilterEventListener2);

        localEventManager.addRecordReaderEventListener(recordReaderEventListener1);
        localEventManager.addRecordReaderEventListener(recordReaderEventListener2);

        localEventManager.addRecordMapperEventListener(recordMapperEventListener1);
        localEventManager.addRecordMapperEventListener(recordMapperEventListener2);

        localEventManager.addRecordValidatorEventListener(recordValidatorEventListener1);
        localEventManager.addRecordValidatorEventListener(recordValidatorEventListener2);

        localEventManager.addRecordProcessorEventListener(recordProcessorEventListener1);
        localEventManager.addRecordProcessorEventListener(recordProcessorEventListener2);
    }

    @Test
    public void fireBeforeBatchStart() {
        localEventManager.fireBeforeJobStart();

        InOrder inOrder = inOrder(jobEventListener1, jobEventListener2);

        inOrder.verify(jobEventListener1).beforeJobStart();
        inOrder.verify(jobEventListener2).beforeJobStart();
    }

    @Test
    public void fireAfterBatchEnd() {
        localEventManager.fireAfterJobEnd();

        InOrder inOrder = inOrder(jobEventListener1, jobEventListener2);

        inOrder.verify(jobEventListener1).afterJobEnd();
        inOrder.verify(jobEventListener2).afterJobEnd();
    }

    @Test
    public void fireOnBatchException() {
        localEventManager.fireOnJobException(throwable);

        InOrder inOrder = inOrder(jobEventListener1, jobEventListener2);

        inOrder.verify(jobEventListener1).onJobException(throwable);
        inOrder.verify(jobEventListener2).onJobException(throwable);
    }

    @Test
    public void fireBeforeReaderOpen() {
        localEventManager.fireBeforeReaderOpening();

        InOrder inOrder = inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).beforeReaderOpening();
        inOrder.verify(recordReaderEventListener2).beforeReaderOpening();
    }

    @Test
    public void fireAfterReaderOpen() {
        localEventManager.fireAfterReaderOpening();

        InOrder inOrder = inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).afterReaderOpening();
        inOrder.verify(recordReaderEventListener2).afterReaderOpening();
    }

    @Test
    public void fireBeforeRecordReading() {
        localEventManager.fireBeforeRecordReading();

        InOrder inOrder = inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).beforeRecordReading();
        inOrder.verify(recordReaderEventListener2).beforeRecordReading();
    }

    @Test
    public void fireAfterRecordReading() {
        localEventManager.fireAfterRecordReading(record);

        InOrder inOrder = inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).afterRecordReading(record);
        inOrder.verify(recordReaderEventListener2).afterRecordReading(record);
    }

    @Test
    public void fireOnRecordReadingException() {
        localEventManager.fireOnRecordReadingException(throwable);

        InOrder inOrder = inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).onRecordReadingException(throwable);
        inOrder.verify(recordReaderEventListener2).onRecordReadingException(throwable);
    }

    @Test
    public void fireBeforeRecordReaderClosing() {
        localEventManager.fireBeforeRecordReaderClosing();

        InOrder inOrder = inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).beforeReaderClosing();
        inOrder.verify(recordReaderEventListener2).beforeReaderClosing();
    }

    @Test
    public void fireAfterRecordReaderClosing() {
        localEventManager.fireAfterRecordReaderClosing();

        InOrder inOrder = inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).afterReaderClosing();
        inOrder.verify(recordReaderEventListener2).afterReaderClosing();
    }

    @Test
    public void fireBeforeRecordFiltering() {
        when(recordFilterEventListener1.beforeRecordFiltering(record)).thenReturn(record1);
        when(recordFilterEventListener2.beforeRecordFiltering(record1)).thenReturn(record2);

        Record result = localEventManager.fireBeforeRecordFiltering(record);

        InOrder inOrder = inOrder(recordFilterEventListener1, recordFilterEventListener2);

        inOrder.verify(recordFilterEventListener1).beforeRecordFiltering(record);
        inOrder.verify(recordFilterEventListener2).beforeRecordFiltering(record1);

        assertThat(result).isEqualTo(record2);
    }

    @Test
    public void fireAfterRecordFiltering() {
        localEventManager.fireAfterRecordFiltering(record, true);

        InOrder inOrder = inOrder(recordFilterEventListener1, recordFilterEventListener2);

        inOrder.verify(recordFilterEventListener1).afterRecordFiltering(record, true);
        inOrder.verify(recordFilterEventListener2).afterRecordFiltering(record, true);
    }

    @Test
    public void fireBeforeRecordMapping() {
        when(recordMapperEventListener1.beforeRecordMapping(record)).thenReturn(record1);
        when(recordMapperEventListener2.beforeRecordMapping(record1)).thenReturn(record2);

        Record result = localEventManager.fireBeforeRecordMapping(record);

        InOrder inOrder = inOrder(recordMapperEventListener1, recordMapperEventListener2);

        inOrder.verify(recordMapperEventListener1).beforeRecordMapping(record);
        inOrder.verify(recordMapperEventListener2).beforeRecordMapping(record1);

        assertThat(result).isEqualTo(record2);
    }

    @Test
    public void fireAfterRecordMapping() {
        localEventManager.fireAfterRecordMapping(record, mappedRecord);

        InOrder inOrder = inOrder(recordMapperEventListener1, recordMapperEventListener2);

        inOrder.verify(recordMapperEventListener1).afterRecordMapping(record, mappedRecord);
        inOrder.verify(recordMapperEventListener2).afterRecordMapping(record, mappedRecord);
    }

    @Test
    public void fireBeforeRecordValidation() {
        when(recordValidatorEventListener1.beforeRecordValidation(record)).thenReturn(record1);
        when(recordValidatorEventListener2.beforeRecordValidation(record1)).thenReturn(record2);

        Object result = localEventManager.fireBeforeRecordValidation(record);

        InOrder inOrder = inOrder(recordValidatorEventListener1, recordValidatorEventListener2);

        inOrder.verify(recordValidatorEventListener1).beforeRecordValidation(record);
        inOrder.verify(recordValidatorEventListener2).beforeRecordValidation(record1);

        assertThat(result).isEqualTo(record2);
    }

    @Test
    public void fireAfterRecordValidation() {
        localEventManager.fireAfterRecordValidation(mappedRecord, validationErrors);

        InOrder inOrder = inOrder(recordValidatorEventListener1, recordValidatorEventListener2);

        inOrder.verify(recordValidatorEventListener1).afterRecordValidation(mappedRecord, validationErrors);
        inOrder.verify(recordValidatorEventListener2).afterRecordValidation(mappedRecord, validationErrors);
    }

    @Test
    public void fireBeforeRecordProcessing() {
        when(recordProcessorEventListener1.beforeRecordProcessing(record)).thenReturn(record1);
        when(recordProcessorEventListener2.beforeRecordProcessing(record1)).thenReturn(record2);

        Object result = localEventManager.fireBeforeRecordProcessing(record);

        InOrder inOrder = inOrder(recordProcessorEventListener1, recordProcessorEventListener2);

        inOrder.verify(recordProcessorEventListener1).beforeRecordProcessing(record);
        inOrder.verify(recordProcessorEventListener2).beforeRecordProcessing(record1);

        assertThat(result).isEqualTo(record2);
    }

    @Test
    public void fireAfterRecordProcessing() {
        localEventManager.fireAfterRecordProcessing(mappedRecord, processingResult);

        InOrder inOrder = inOrder(recordProcessorEventListener1, recordProcessorEventListener2);

        inOrder.verify(recordProcessorEventListener1).afterRecordProcessing(mappedRecord, processingResult);
        inOrder.verify(recordProcessorEventListener2).afterRecordProcessing(mappedRecord, processingResult);
    }

    @Test
    public void fireOnRecordProcessingException() {
        localEventManager.fireOnRecordProcessingException(mappedRecord, throwable);

        InOrder inOrder = inOrder(recordProcessorEventListener1, recordProcessorEventListener2);

        inOrder.verify(recordProcessorEventListener1).onRecordProcessingException(mappedRecord, throwable);
        inOrder.verify(recordProcessorEventListener2).onRecordProcessingException(mappedRecord, throwable);
    }

    /*
     * Integration tests for custom step listeners.
     */

    @Test
    public void testRecordModificationThroughCustomRecordFilterEventListener() throws Exception {
        Engine engine = EngineBuilder.aNewEngine()
                .reader(new StringRecordReader("foo" + LINE_SEPARATOR + "bar"))
                .filter(new StartWithStringRecordFilter("#"))
                .recordFilterEventListener(new RecordFilterEventListener() {
                    @Override
                    public Record beforeRecordFiltering(Record record) {
                        return new StringRecord(record.getHeader(), "#" + record.getPayload());
                    }

                    @Override
                    public void afterRecordFiltering(Record record, boolean filtered) {
                        //no op
                    }
                }).build();

        Report report = engine.call();

        assertThat(report.getFilteredRecordsCount()).isEqualTo(2);
    }

    @Test
    public void testRecordModificationThroughCustomRecordValidatorEventListener() throws Exception {
        Engine engine = EngineBuilder.aNewEngine()
                .reader(new StringRecordReader("foo" + LINE_SEPARATOR + "bar"))
                .validator(new RecordValidator<StringRecord>() {
                    @Override
                    public Set<ValidationError> validateRecord(StringRecord record) {
                        Set<ValidationError> errors = new HashSet<ValidationError>();
                        String payload = record.getPayload();
                        if (payload.startsWith("#")) {
                            errors.add(new ValidationError("Record " + payload + " must not start with #"));
                        }
                        return errors;
                    }
                })
                .recordValidatorEventListener(new RecordValidatorEventListener() {
                    @Override
                    public Record beforeRecordValidation(Object record) {
                        StringRecord stringRecord = (StringRecord) record;
                        return new StringRecord(stringRecord.getHeader(), "#" + stringRecord.getPayload());
                    }

                    @Override
                    public void afterRecordValidation(Object record, Set<ValidationError> validationErrors) {
                        //no op
                    }
                }).build();

        Report report = engine.call();

        assertThat(report.getRejectedRecordsCount()).isEqualTo(2);
    }
}