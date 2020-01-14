/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.jms;

import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Header;

import javax.jms.QueueSender;
import java.util.Date;
import java.util.List;

/**
 * A job listener that broadcasts a {@link JmsPoisonRecord} at the end of the job.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 * @deprecated This class is deprecated since v5.3 and will be removed in v6.
 */
@Deprecated
public class JmsPoisonRecordBroadcaster implements JobListener {

    private BroadcastJmsQueueRecordWriter broadcastJmsQueueRecordWriter;

    /**
     * Create a new {@link JmsPoisonRecordBroadcaster}.
     *
     * @param queues to which poison messages should be written
     */
    public JmsPoisonRecordBroadcaster(List<QueueSender> queues) {
        this.broadcastJmsQueueRecordWriter = new BroadcastJmsQueueRecordWriter(queues);
    }

    @Override
    public void beforeJobStart(final JobParameters jobParameters) {
        // no op
    }

    @Override
    public void afterJobEnd(final JobReport jobReport) {
        try {
            broadcastJmsQueueRecordWriter.writeRecords(new Batch(new JmsPoisonRecord(new Header(0L, "Poison record", new Date()), new JmsPoisonMessage())));
        } catch (Exception e) {
            throw new RuntimeException("Unable to broadcast poison record.", e);
        }
    }

}
