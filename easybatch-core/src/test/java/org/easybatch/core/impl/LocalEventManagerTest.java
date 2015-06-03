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

import org.easybatch.core.api.Record;
import org.easybatch.core.api.ValidationError;
import org.easybatch.core.api.event.batch.BatchProcessEventListener;
import org.easybatch.core.api.event.step.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

/**
 * Test class for {@link LocalEventManager}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class LocalEventManagerTest {

    @Mock
    private BatchProcessEventListener batchProcessEventListener1, batchProcessEventListener2;
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
    private Record record;
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
        
        localEventManager.addBatchProcessEventListener(batchProcessEventListener1);
        localEventManager.addBatchProcessEventListener(batchProcessEventListener2);
        
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
        localEventManager.fireBeforeBatchStart();

        InOrder inOrder = Mockito.inOrder(batchProcessEventListener1, batchProcessEventListener2);

        inOrder.verify(batchProcessEventListener1).beforeBatchStart();
        inOrder.verify(batchProcessEventListener2).beforeBatchStart();
    }

    @Test
    public void fireAfterBatchEnd() {
        localEventManager.fireAfterBatchEnd();

        InOrder inOrder = Mockito.inOrder(batchProcessEventListener1, batchProcessEventListener2);

        inOrder.verify(batchProcessEventListener1).afterBatchEnd();
        inOrder.verify(batchProcessEventListener2).afterBatchEnd();
    }

    @Test
    public void fireOnBatchException() {
        localEventManager.fireOnBatchException(throwable);

        InOrder inOrder = Mockito.inOrder(batchProcessEventListener1, batchProcessEventListener2);

        inOrder.verify(batchProcessEventListener1).onBatchException(throwable);
        inOrder.verify(batchProcessEventListener2).onBatchException(throwable);
    }

    @Test
    public void fireBeforeReaderOpen() {
        localEventManager.fireBeforeReaderOpening();

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).beforeReaderOpening();
        inOrder.verify(recordReaderEventListener2).beforeReaderOpening();
    }

    @Test
    public void fireAfterReaderOpen() {
        localEventManager.fireAfterReaderOpening();

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).afterReaderOpening();
        inOrder.verify(recordReaderEventListener2).afterReaderOpening();
    }

    @Test
    public void fireBeforeRecordRead() {
        localEventManager.fireBeforeRecordReading();

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).beforeRecordReading();
        inOrder.verify(recordReaderEventListener2).beforeRecordReading();
    }

    @Test
    public void fireAfterRecordRead() {
        localEventManager.fireAfterRecordReading(record);

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).afterRecordReading(record);
        inOrder.verify(recordReaderEventListener2).afterRecordReading(record);
    }

    @Test
    public void fireOnRecordReadException() {
        localEventManager.fireOnRecordReadingException(throwable);

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).onRecordReadingException(throwable);
        inOrder.verify(recordReaderEventListener2).onRecordReadingException(throwable);
    }

    @Test
    public void fireBeforeRecordReaderClose() {
        localEventManager.fireBeforeRecordReaderClosing();

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).beforeReaderClosing();
        inOrder.verify(recordReaderEventListener2).beforeReaderClosing();
    }

    @Test
    public void fireAfterRecordReaderClose() {
        localEventManager.fireAfterRecordReaderClosing();

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).afterReaderClosing();
        inOrder.verify(recordReaderEventListener2).afterReaderClosing();
    }

    @Test
    public void fireBeforeFilterRecord() {
        localEventManager.fireBeforeRecordFiltering(record);

        InOrder inOrder = Mockito.inOrder(recordFilterEventListener1, recordFilterEventListener2);

        inOrder.verify(recordFilterEventListener1).beforeRecordFiltering(record);
        inOrder.verify(recordFilterEventListener2).beforeRecordFiltering(record);
    }

    @Test
    public void fireAfterFilterRecord() {
        localEventManager.fireAfterRecordFiltering(record, true);

        InOrder inOrder = Mockito.inOrder(recordFilterEventListener1, recordFilterEventListener2);

        inOrder.verify(recordFilterEventListener1).afterRecordFiltering(record, true);
        inOrder.verify(recordFilterEventListener2).afterRecordFiltering(record, true);
    }

    @Test
    public void fireBeforeMapRecord() {
        localEventManager.fireBeforeRecordMapping(record);

        InOrder inOrder = Mockito.inOrder(recordMapperEventListener1, recordMapperEventListener2);

        inOrder.verify(recordMapperEventListener1).beforeRecordMapping(record);
        inOrder.verify(recordMapperEventListener2).beforeRecordMapping(record);
    }

    @Test
    public void fireAfterMapRecord() {
        localEventManager.fireAfterRecordMapping(record, mappedRecord);

        InOrder inOrder = Mockito.inOrder(recordMapperEventListener1, recordMapperEventListener2);

        inOrder.verify(recordMapperEventListener1).afterRecordMapping(record, mappedRecord);
        inOrder.verify(recordMapperEventListener2).afterRecordMapping(record, mappedRecord);
    }

    @Test
    public void fireBeforeValidateRecord() {
        localEventManager.fireBeforeRecordValidation(mappedRecord);

        InOrder inOrder = Mockito.inOrder(recordValidatorEventListener1, recordValidatorEventListener2);

        inOrder.verify(recordValidatorEventListener1).beforeRecordValidation(mappedRecord);
        inOrder.verify(recordValidatorEventListener2).beforeRecordValidation(mappedRecord);
    }

    @Test
    public void fireAfterValidateRecord() {
        localEventManager.fireAfterRecordValidation(mappedRecord, validationErrors);

        InOrder inOrder = Mockito.inOrder(recordValidatorEventListener1, recordValidatorEventListener2);

        inOrder.verify(recordValidatorEventListener1).afterRecordValidation(mappedRecord, validationErrors);
        inOrder.verify(recordValidatorEventListener2).afterRecordValidation(mappedRecord, validationErrors);
    }

    @Test
    public void fireBeforeProcessingRecord() {
        localEventManager.fireBeforeRecordProcessing(mappedRecord);

        InOrder inOrder = Mockito.inOrder(recordProcessorEventListener1, recordProcessorEventListener2);

        inOrder.verify(recordProcessorEventListener1).beforeRecordProcessing(mappedRecord);
        inOrder.verify(recordProcessorEventListener2).beforeRecordProcessing(mappedRecord);
    }

    @Test
    public void fireAfterProcessingRecord() {
        localEventManager.fireAfterRecordProcessing(mappedRecord, processingResult);

        InOrder inOrder = Mockito.inOrder(recordProcessorEventListener1, recordProcessorEventListener2);

        inOrder.verify(recordProcessorEventListener1).afterRecordProcessing(mappedRecord, processingResult);
        inOrder.verify(recordProcessorEventListener2).afterRecordProcessing(mappedRecord, processingResult);
    }

    @Test
    public void fireOnRecordProcessingException() {
        localEventManager.fireOnRecordProcessingException(mappedRecord, throwable);

        InOrder inOrder = Mockito.inOrder(recordProcessorEventListener1, recordProcessorEventListener2);

        inOrder.verify(recordProcessorEventListener1).onRecordProcessingException(mappedRecord, throwable);
        inOrder.verify(recordProcessorEventListener2).onRecordProcessingException(mappedRecord, throwable);
    }
}