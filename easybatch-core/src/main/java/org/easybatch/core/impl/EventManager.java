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
import org.easybatch.core.api.Report;
import org.easybatch.core.api.listener.JobListener;
import org.easybatch.core.api.listener.PipelineListener;
import org.easybatch.core.api.listener.RecordReaderListener;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Manager of job/steps listeners.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
class EventManager {

    private Set<JobListener> jobListeners = new LinkedHashSet<JobListener>();
    private Set<RecordReaderListener> recordReaderListeners = new LinkedHashSet<RecordReaderListener>();
    private Set<PipelineListener> pipelineListeners = new LinkedHashSet<PipelineListener>();

    public void addJobListener(JobListener jobListener) {
        jobListeners.add(jobListener);
    }

    public void addRecordReaderListener(RecordReaderListener recordReaderListener) {
        recordReaderListeners.add(recordReaderListener);
    }

    public void addPipelineListener(PipelineListener pipelineListener) {
        pipelineListeners.add(pipelineListener);
    }

    public void fireBeforeJobStart() {
        for (JobListener eventListener : jobListeners) {
            eventListener.beforeJobStart();
        }
    }

    public void fireAfterJobEnd(Report report) {
        for (JobListener eventListener : jobListeners) {
            eventListener.afterJobEnd(report);
        }
    }

    public void fireBeforeRecordReading() {
        for (RecordReaderListener eventListener : recordReaderListeners) {
            eventListener.beforeRecordReading();
        }
    }

    public void fireAfterRecordReading(Record record) {
        for (RecordReaderListener eventListener : recordReaderListeners) {
            eventListener.afterRecordReading(record);
        }
    }

    public void fireOnRecordReadingException(Throwable throwable) {
        for (RecordReaderListener eventListener : recordReaderListeners) {
            eventListener.onRecordReadingException(throwable);
        }
    }

    public Object fireBeforeRecordProcessing(Record record) {
        Object recordToProcess = record;
        for (PipelineListener eventListener : pipelineListeners) {
            recordToProcess = eventListener.beforeRecordProcessing(recordToProcess);
        }
        return recordToProcess;
    }

    public void fireAfterRecordProcessing(Object inputRecord, Object outputRecord) {
        for (PipelineListener eventListener : pipelineListeners) {
            eventListener.afterRecordProcessing(inputRecord, outputRecord);
        }
    }

    public void fireOnRecordProcessingException(final Object record, final Throwable throwable) {
        for (PipelineListener eventListener : pipelineListeners) {
            eventListener.onRecordProcessingException(record, throwable);
        }
    }
}
