/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.core.dispatcher;

import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.record.PoisonRecord;
import org.easybatch.core.record.Record;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * A utility job listener that broadcasts a {@link PoisonRecord} record at the end of the job.
 *
 * @param <T> record type
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@SuppressWarnings("unchecked")
public class PoisonRecordBroadcaster<T extends Record> implements JobListener {

    private BroadcastRecordDispatcher recordDispatcher;

    /**
     * Create a new {@link PoisonRecordBroadcaster}.
     *
     * @param queues the list of queues to which poison records should be dispatched
     */
    public PoisonRecordBroadcaster(List<BlockingQueue<T>> queues) {
        this.recordDispatcher = new BroadcastRecordDispatcher(queues);
    }

    @Override
    public void beforeJobStart(final JobParameters jobParameters) {
        // no op
    }

    @Override
    public void afterJobEnd(final JobReport jobReport) {
        try {
            recordDispatcher.processRecord(new PoisonRecord());
        } catch (RecordDispatchingException e) {
            throw new RuntimeException("Unable to broadcast poison record.", e);
        }
    }

}
