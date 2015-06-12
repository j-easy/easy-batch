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
import org.easybatch.core.api.event.EventManager;
import org.easybatch.core.api.event.job.JobEventListener;
import org.easybatch.core.api.event.step.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Local implementation of {@link EventManager}.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
class LocalEventManager implements EventManager {

    private Set<JobEventListener> jobEventListeners = new LinkedHashSet<JobEventListener>();
    private Set<RecordReaderEventListener> recordReaderEventListeners = new LinkedHashSet<RecordReaderEventListener>();
    private Set<RecordFilterEventListener> recordFilterEventListeners = new LinkedHashSet<RecordFilterEventListener>();
    private Set<RecordMapperEventListener> recordMapperEventListeners = new LinkedHashSet<RecordMapperEventListener>();
    private Set<RecordValidatorEventListener> recordValidatorEventListeners = new LinkedHashSet<RecordValidatorEventListener>();
    private Set<RecordProcessorEventListener> recordProcessorEventListeners = new LinkedHashSet<RecordProcessorEventListener>();

    @Override
    public void addJobEventListener(JobEventListener jobEventListener) {
        jobEventListeners.add(jobEventListener);
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
    public void fireBeforeJobStart() {
        for (JobEventListener eventListener : jobEventListeners) {
            eventListener.beforeJobStart();
        }
    }

    @Override
    public void fireAfterJobEnd() {
        for (JobEventListener eventListener : jobEventListeners) {
            eventListener.afterJobEnd();
        }
    }

    @Override
    public void fireOnJobException(Throwable t) {
        for (JobEventListener eventListener : jobEventListeners) {
            eventListener.onJobException(t);
        }
    }

    @Override
    public void fireBeforeReaderOpening() {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.beforeReaderOpening();
        }
    }

    @Override
    public void fireAfterReaderOpening() {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.afterReaderOpening();
        }
    }

    @Override
    public void fireBeforeRecordReading() {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.beforeRecordReading();
        }
    }

    @Override
    public void fireAfterRecordReading(Record record) {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.afterRecordReading(record);
        }
    }

    @Override
    public void fireOnRecordReadingException(Throwable throwable) {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.onRecordReadingException(throwable);
        }
    }

    @Override
    public void fireBeforeRecordReaderClosing() {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.beforeReaderClosing();
        }
    }

    @Override
    public void fireAfterRecordReaderClosing() {
        for (RecordReaderEventListener eventListener : recordReaderEventListeners) {
            eventListener.afterReaderClosing();
        }
    }

    @Override
    public void fireBeforeRecordFiltering(Record record) {
        for (RecordFilterEventListener eventListener : recordFilterEventListeners) {
            eventListener.beforeRecordFiltering(record);
        }
    }

    @Override
    public void fireAfterRecordFiltering(Record record, boolean filtered) {
        for (RecordFilterEventListener eventListener : recordFilterEventListeners) {
            eventListener.afterRecordFiltering(record, filtered);
        }
    }

    @Override
    public void fireBeforeRecordMapping(Record record) {
        for (RecordMapperEventListener eventListener : recordMapperEventListeners) {
            eventListener.beforeRecordMapping(record);
        }
    }

    @Override
    public void fireAfterRecordMapping(Record record, Object mappedRecord) {
        for (RecordMapperEventListener eventListener : recordMapperEventListeners) {
            eventListener.afterRecordMapping(record, mappedRecord);
        }
    }

    @Override
    public void fireBeforeRecordValidation(Object mappedRecord) {
        for (RecordValidatorEventListener eventListener : recordValidatorEventListeners) {
            eventListener.beforeRecordValidation(mappedRecord);
        }
    }

    @Override
    public void fireAfterRecordValidation(Object validatedRecord, Set<ValidationError> validationErrors) {
        for (RecordValidatorEventListener eventListener : recordValidatorEventListeners) {
            eventListener.afterRecordValidation(validatedRecord, validationErrors);
        }
    }

    @Override
    public void fireBeforeRecordProcessing(Object record) {
        for (RecordProcessorEventListener eventListener : recordProcessorEventListeners) {
            eventListener.beforeRecordProcessing(record);
        }
    }

    @Override
    public void fireAfterRecordProcessing(Object record, Object processingResult) {
        for (RecordProcessorEventListener eventListener : recordProcessorEventListeners) {
            eventListener.afterRecordProcessing(record, processingResult);
        }
    }

    @Override
    public void fireOnRecordProcessingException(final Object record, final Throwable throwable) {
        for (RecordProcessorEventListener eventListener : recordProcessorEventListeners) {
            eventListener.onRecordProcessingException(record, throwable);
        }
    }
}
