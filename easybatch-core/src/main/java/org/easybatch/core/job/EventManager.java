/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core.job;

import org.easybatch.core.listener.JobListener;
import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.listener.RecordReaderListener;
import org.easybatch.core.record.Record;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Manager of job/steps listeners.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
class EventManager {

    private Set<JobListener> jobListeners = new LinkedHashSet<>();
    private Set<RecordReaderListener> recordReaderListeners = new LinkedHashSet<>();
    private Set<PipelineListener> pipelineListeners = new LinkedHashSet<>();

    public void addJobListener(JobListener jobListener) {
        jobListeners.add(jobListener);
    }

    public void addRecordReaderListener(RecordReaderListener recordReaderListener) {
        recordReaderListeners.add(recordReaderListener);
    }

    public void addPipelineListener(PipelineListener pipelineListener) {
        pipelineListeners.add(pipelineListener);
    }

    public void fireBeforeJobStart(JobParameters jobParameters) {
        for (JobListener jobListener : jobListeners) {
            jobListener.beforeJobStart(jobParameters);
        }
    }

    public void fireAfterJobEnd(JobReport jobReport) {
        for (JobListener jobListener : jobListeners) {
            jobListener.afterJobEnd(jobReport);
        }
    }

    public void fireBeforeRecordReading() {
        for (RecordReaderListener recordReaderListener : recordReaderListeners) {
            recordReaderListener.beforeRecordReading();
        }
    }

    public void fireAfterRecordReading(Record record) {
        for (RecordReaderListener recordReaderListener : recordReaderListeners) {
            recordReaderListener.afterRecordReading(record);
        }
    }

    public void fireOnRecordReadingException(Throwable throwable) {
        for (RecordReaderListener recordReaderListener : recordReaderListeners) {
            recordReaderListener.onRecordReadingException(throwable);
        }
    }

    public Record fireBeforeRecordProcessing(Record record) {
        Record recordToProcess = record;
        for (PipelineListener pipelineListener : pipelineListeners) {
            recordToProcess = pipelineListener.beforeRecordProcessing(recordToProcess);
        }
        return recordToProcess;
    }

    public void fireAfterRecordProcessing(Record inputRecord, Record outputRecord) {
        for (PipelineListener pipelineListener : pipelineListeners) {
            pipelineListener.afterRecordProcessing(inputRecord, outputRecord);
        }
    }

    public void fireOnRecordProcessingException(final Record record, final Throwable throwable) {
        for (PipelineListener pipelineListener : pipelineListeners) {
            pipelineListener.onRecordProcessingException(record, throwable);
        }
    }
}
