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

import org.easybatch.core.api.EventManager;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.ValidationError;
import org.easybatch.core.api.event.batch.BatchProcessEventListener;
import org.easybatch.core.api.event.step.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Local implementation of {@link EventManager}.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public class LocalEventManager implements EventManager {

    private Set<BatchProcessEventListener> batchProcessEventListeners = new LinkedHashSet<BatchProcessEventListener>();
    private Set<RecordReaderEventListener> recordReaderEventListeners = new LinkedHashSet<RecordReaderEventListener>();
    private Set<RecordFilterEventListener> recordFilterEventListeners = new LinkedHashSet<RecordFilterEventListener>();
    private Set<RecordMapperEventListener> recordMapperEventListeners = new LinkedHashSet<RecordMapperEventListener>();
    private Set<RecordValidatorEventListener> recordValidatorEventListeners = new LinkedHashSet<RecordValidatorEventListener>();
    private Set<RecordProcessorEventListener> recordProcessorEventListeners = new LinkedHashSet<RecordProcessorEventListener>();

    @Override
    public void addBatchProcessListener(BatchProcessEventListener batchProcessEventListener) {
        batchProcessEventListeners.add(batchProcessEventListener);
    }

    @Override
    public void addRecordReaderEventListener(RecordReaderEventListener recordReaderEventListener) {
        recordReaderEventListeners.add(recordReaderEventListener);
    }

    @Override
    public void addRecordFilterEventListener(RecordFilterEventListener recordFilterEventListener) {
        recordFilterEventListeners.add(recordFilterEventListener);
    }

    @Override
    public void addRecordMapperEventListener(RecordMapperEventListener recordMapperEventListener) {
        recordMapperEventListeners.add(recordMapperEventListener);
    }

    @Override
    public void addRecordValidatorEventListener(RecordValidatorEventListener recordValidatorEventListener) {
        recordValidatorEventListeners.add(recordValidatorEventListener);
    }

    @Override
    public void addRecordProcessorEventListener(RecordProcessorEventListener recordProcessorEventListener) {
        recordProcessorEventListeners.add(recordProcessorEventListener);
    }

    @Override
    public void fireBeforeBatchStart() {
        for (BatchProcessEventListener eventListener : batchProcessEventListeners) {
            eventListener.beforeBatchStart();
        }
    }

    @Override
    public void fireAfterBatchEnd() {
        for (BatchProcessEventListener eventListener : batchProcessEventListeners) {
            eventListener.afterBatchEnd();
        }
    }

    @Override
    public void fireOnBatchException(Throwable t) {
        for (BatchProcessEventListener eventListener : batchProcessEventListeners) {
            eventListener.onException(t);
        }
    }

    @Override
    public void fireBeforeReaderOpen() {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.beforeReaderOpen();
        }
    }

    @Override
    public void fireAfterReaderOpen() {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.afterReaderOpen();
        }
    }

    @Override
    public void fireBeforeRecordRead() {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.beforeRecordRead();
        }
    }

    @Override
    public void fireAfterRecordRead(Record record) {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.afterRecordRead(record);
        }
    }

    @Override
    public void fireOnRecordReadException(Throwable throwable) {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.onRecordReadException(throwable);
        }
    }

    @Override
    public void fireBeforeRecordReaderClose() {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.beforeReaderClose();
        }
    }

    @Override
    public void fireAfterRecordReaderClose() {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.afterReaderClose();
        }
    }

    @Override
    public void fireBeforeFilterRecord(Record record) {
        for (RecordFilterEventListener eventListener : recordFilterEventListeners) {
            eventListener.beforeFilterRecord(record);
        }
    }

    @Override
    public void fireAfterFilterRecord(Record record, boolean filterRecord) {
        for (RecordFilterEventListener eventListener : recordFilterEventListeners) {
            eventListener.afterFilterRecord(record, filterRecord);
        }
    }

    @Override
    public void fireBeforeMapRecord(Record record) {
        for (RecordMapperEventListener eventListener : recordMapperEventListeners) {
            eventListener.beforeMapRecord(record);
        }
    }

    @Override
    public void fireAfterMapRecord(Record record, Object mapResult) {
        for (RecordMapperEventListener eventListener : recordMapperEventListeners) {
            eventListener.afterMapRecord(record, mapResult);
        }
    }

    @Override
    public void fireBeforeValidateRecord(Object mappedRecord) {
        for (RecordValidatorEventListener eventListener : recordValidatorEventListeners) {
            eventListener.beforeValidateRecord(mappedRecord);
        }
    }

    @Override
    public void fireAfterValidateRecord(Object validatedRecord, Set<ValidationError> validationErrors) {
        for (RecordValidatorEventListener eventListener : recordValidatorEventListeners) {
            eventListener.afterValidateRecord(validatedRecord, validationErrors);
        }
    }

    @Override
    public void fireBeforeProcessingRecord(Object record) {
        for (RecordProcessorEventListener eventListener : recordProcessorEventListeners) {
            eventListener.beforeProcessingRecord(record);
        }
    }

    @Override
    public void fireAfterProcessingRecord(Object record,Object processingResult) {
        for (RecordProcessorEventListener eventListener : recordProcessorEventListeners) {
            eventListener.afterProcessingRecord(record, processingResult);
        }
    }

    public void fireOnRecordProcessingException(final Object record, final Throwable throwable) {
        for (RecordProcessorEventListener eventListener : recordProcessorEventListeners) {
            eventListener.onRecordProcessingException(record, throwable);
        }
    }
}
