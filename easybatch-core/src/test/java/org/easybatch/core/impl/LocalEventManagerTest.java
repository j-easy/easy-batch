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

        inOrder.verify(batchProcessEventListener1).onException(throwable);
        inOrder.verify(batchProcessEventListener2).onException(throwable);
    }

    @Test
    public void fireBeforeReaderOpen() {
        localEventManager.fireBeforeReaderOpen();

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).beforeReaderOpen();
        inOrder.verify(recordReaderEventListener2).beforeReaderOpen();
    }

    @Test
    public void fireAfterReaderOpen() {
        localEventManager.fireAfterReaderOpen();

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).afterReaderOpen();
        inOrder.verify(recordReaderEventListener2).afterReaderOpen();
    }

    @Test
    public void fireBeforeRecordRead() {
        localEventManager.fireBeforeRecordRead();

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).beforeRecordRead();
        inOrder.verify(recordReaderEventListener2).beforeRecordRead();
    }

    @Test
    public void fireAfterRecordRead() {
        localEventManager.fireAfterRecordRead(record);

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).afterRecordRead(record);
        inOrder.verify(recordReaderEventListener2).afterRecordRead(record);
    }

    @Test
    public void fireOnRecordReadException() {
        localEventManager.fireOnRecordReadException(throwable);

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).onRecordReadException(throwable);
        inOrder.verify(recordReaderEventListener2).onRecordReadException(throwable);
    }

    @Test
    public void fireBeforeRecordReaderClose() {
        localEventManager.fireBeforeRecordReaderClose();

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).beforeReaderClose();
        inOrder.verify(recordReaderEventListener2).beforeReaderClose();
    }

    @Test
    public void fireAfterRecordReaderClose() {
        localEventManager.fireAfterRecordReaderClose();

        InOrder inOrder = Mockito.inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).afterReaderClose();
        inOrder.verify(recordReaderEventListener2).afterReaderClose();
    }

    @Test
    public void fireBeforeFilterRecord() {
        localEventManager.fireBeforeFilterRecord(record);

        InOrder inOrder = Mockito.inOrder(recordFilterEventListener1, recordFilterEventListener2);

        inOrder.verify(recordFilterEventListener1).beforeFilterRecord(record);
        inOrder.verify(recordFilterEventListener2).beforeFilterRecord(record);
    }

    @Test
    public void fireAfterFilterRecord() {
        localEventManager.fireAfterFilterRecord(record, true);

        InOrder inOrder = Mockito.inOrder(recordFilterEventListener1, recordFilterEventListener2);

        inOrder.verify(recordFilterEventListener1).afterFilterRecord(record, true);
        inOrder.verify(recordFilterEventListener2).afterFilterRecord(record, true);
    }

    @Test
    public void fireBeforeMapRecord() {
        localEventManager.fireBeforeMapRecord(record);

        InOrder inOrder = Mockito.inOrder(recordMapperEventListener1, recordMapperEventListener2);

        inOrder.verify(recordMapperEventListener1).beforeMapRecord(record);
        inOrder.verify(recordMapperEventListener2).beforeMapRecord(record);
    }

    @Test
    public void fireAfterMapRecord() {
        localEventManager.fireAfterMapRecord(record, mappedRecord);

        InOrder inOrder = Mockito.inOrder(recordMapperEventListener1, recordMapperEventListener2);

        inOrder.verify(recordMapperEventListener1).afterMapRecord(record, mappedRecord);
        inOrder.verify(recordMapperEventListener2).afterMapRecord(record, mappedRecord);
    }

    @Test
    public void fireBeforeValidateRecord() {
        localEventManager.fireBeforeValidateRecord(mappedRecord);

        InOrder inOrder = Mockito.inOrder(recordValidatorEventListener1, recordValidatorEventListener2);

        inOrder.verify(recordValidatorEventListener1).beforeValidateRecord(mappedRecord);
        inOrder.verify(recordValidatorEventListener2).beforeValidateRecord(mappedRecord);
    }

    @Test
    public void fireAfterValidateRecord() {
        localEventManager.fireAfterValidateRecord(mappedRecord, validationErrors);

        InOrder inOrder = Mockito.inOrder(recordValidatorEventListener1, recordValidatorEventListener2);

        inOrder.verify(recordValidatorEventListener1).afterValidateRecord(mappedRecord, validationErrors);
        inOrder.verify(recordValidatorEventListener2).afterValidateRecord(mappedRecord, validationErrors);
    }

    @Test
    public void fireBeforeProcessingRecord() {
        localEventManager.fireBeforeProcessingRecord(mappedRecord);

        InOrder inOrder = Mockito.inOrder(recordProcessorEventListener1, recordProcessorEventListener2);

        inOrder.verify(recordProcessorEventListener1).beforeProcessingRecord(mappedRecord);
        inOrder.verify(recordProcessorEventListener2).beforeProcessingRecord(mappedRecord);
    }

    @Test
    public void fireAfterProcessingRecord() {
        localEventManager.fireAfterProcessingRecord(mappedRecord, processingResult);

        InOrder inOrder = Mockito.inOrder(recordProcessorEventListener1, recordProcessorEventListener2);

        inOrder.verify(recordProcessorEventListener1).afterProcessingRecord(mappedRecord, processingResult);
        inOrder.verify(recordProcessorEventListener2).afterProcessingRecord(mappedRecord, processingResult);
    }

    @Test
    public void fireOnRecordProcessingException() {
        localEventManager.fireOnRecordProcessingException(mappedRecord, throwable);

        InOrder inOrder = Mockito.inOrder(recordProcessorEventListener1, recordProcessorEventListener2);

        inOrder.verify(recordProcessorEventListener1).onRecordProcessingException(mappedRecord, throwable);
        inOrder.verify(recordProcessorEventListener2).onRecordProcessingException(mappedRecord, throwable);
    }
}