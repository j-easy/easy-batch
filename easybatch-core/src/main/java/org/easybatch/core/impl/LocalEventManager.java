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
import org.easybatch.core.api.event.EventManager;
import org.easybatch.core.api.event.JobEventListener;
import org.easybatch.core.api.event.PipelineEventListener;
import org.easybatch.core.api.event.RecordReaderEventListener;

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
    private Set<PipelineEventListener> pipelineEventListeners = new LinkedHashSet<PipelineEventListener>();

    @Override
    public void addJobEventListener(JobEventListener jobEventListener) {
        jobEventListeners.add(jobEventListener);
    }

    @Override
    public void addRecordReaderEventListener(RecordReaderEventListener recordReaderEventListener) {
        recordReaderEventListeners.add(recordReaderEventListener);
    }

    @Override
    public void addPipelineEventListener(PipelineEventListener pipelineEventListener) {
        pipelineEventListeners.add(pipelineEventListener);
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
    public Object fireBeforeRecordProcessing(Record record) {
        Object recordToProcess = record;
        for (PipelineEventListener eventListener : pipelineEventListeners) {
            recordToProcess = eventListener.beforeRecordProcessing(recordToProcess);
        }
        return recordToProcess;
    }

    @Override
    public void fireAfterRecordProcessing(Object record, Object processingResult) {
        for (PipelineEventListener eventListener : pipelineEventListeners) {
            eventListener.afterRecordProcessing(record, processingResult);
        }
    }

    @Override
    public void fireOnRecordProcessingException(final Object record, final Throwable throwable) {
        for (PipelineEventListener eventListener : pipelineEventListeners) {
            eventListener.onRecordProcessingException(record, throwable);
        }
    }
}
