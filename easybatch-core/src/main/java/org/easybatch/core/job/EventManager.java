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

import java.util.ArrayList;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.listener.RecordReaderListener;
import org.easybatch.core.record.Record;

import java.util.List;
import java.util.ListIterator;

/**
 * Manager of job/steps listeners.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
class EventManager {

    private final List<JobListener> jobListeners = new ArrayList<>();
    private final List<RecordReaderListener> recordReaderListeners = new ArrayList<>();
    private final List<PipelineListener> pipelineListeners = new ArrayList<>();

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
        for (ListIterator<JobListener> iterator =
                jobListeners.listIterator(jobListeners.size());
                iterator.hasPrevious();) {
            iterator.previous().afterJobEnd(jobReport);
        }
    }

    public void fireBeforeRecordReading() {
        for (RecordReaderListener recordReaderListener : recordReaderListeners) {
            recordReaderListener.beforeRecordReading();
        }
    }

    public void fireAfterRecordReading(Record record) {
        for (ListIterator<RecordReaderListener> iterator = 
                recordReaderListeners.listIterator(recordReaderListeners.size());
                iterator.hasPrevious();) {
            iterator.previous().afterRecordReading(record);
        }
    }

    public void fireOnRecordReadingException(Throwable throwable) {
        for (ListIterator<RecordReaderListener> iterator = 
                recordReaderListeners.listIterator(recordReaderListeners.size());
                iterator.hasPrevious();) {
            iterator.previous().onRecordReadingException(throwable);
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
        for (ListIterator<PipelineListener> iterator = 
                pipelineListeners.listIterator(pipelineListeners.size());
                iterator.hasPrevious();) {
            iterator.previous().afterRecordProcessing(inputRecord, outputRecord);
        }
    }

    public void fireOnRecordProcessingException(final Record record, final Throwable throwable) {
        for (ListIterator<PipelineListener> iterator = 
                pipelineListeners.listIterator(pipelineListeners.size());
                iterator.hasPrevious();) {
            iterator.previous().onRecordProcessingException(record, throwable);
        }
    }
}
