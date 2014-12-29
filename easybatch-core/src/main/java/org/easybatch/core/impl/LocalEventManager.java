package org.easybatch.core.impl;

import org.easybatch.core.api.EventManager;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.ValidationError;
import org.easybatch.core.api.event.global.BatchProcessEventListener;
import org.easybatch.core.api.event.record.*;

import java.util.LinkedHashSet;
import java.util.Set;


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
    public void addRecordReaderListener(RecordReaderEventListener recordReaderEventListener) {
        recordReaderEventListeners.add(recordReaderEventListener);
    }

    @Override
    public void addRecordFilterEventListener(RecordFilterEventListener recordFilterEventListener) {
        recordFilterEventListeners.add(recordFilterEventListener);
    }

    @Override
    public void addRecordMapperListener(RecordMapperEventListener recordMapperEventListener) {
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
    public void fireBeforeProcessRecord(Object record) {
        for (RecordProcessorEventListener eventListener : recordProcessorEventListeners) {
            eventListener.beforeProcessRecord(record);
        }
    }

    @Override
    public void fireAfterRecordProcessed(Object record, Object processResult) {
        for (RecordProcessorEventListener eventListener : recordProcessorEventListeners) {
            eventListener.afterRecordProcessed(record, processResult);
        }
    }
}
