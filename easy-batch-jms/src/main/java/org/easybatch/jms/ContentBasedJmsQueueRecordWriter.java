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

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.DefaultPredicate;
import org.easybatch.core.writer.Predicate;
import org.easybatch.core.writer.RecordWriter;

import javax.jms.Message;
import javax.jms.QueueSender;
import java.util.Map;

/**
 * Write records to a list of Jms Queues based on their content.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ContentBasedJmsQueueRecordWriter implements RecordWriter {

    /**
     * Map a predicate to a queue: when the record content matches the predicate,
     * then it is written to the mapped queue.
     */
    private Map<Predicate, QueueSender> queueMap;

    ContentBasedJmsQueueRecordWriter(Map<Predicate, QueueSender> queueMap) {
        this.queueMap = queueMap;
    }

    @Override
    public void open() throws Exception {

    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        QueueSender defaultQueue = queueMap.get(new DefaultPredicate());
        for (Record record : batch) {
            boolean matched = false;
            Message payload = (Message) record.getPayload();
            for (Map.Entry<Predicate, QueueSender> entry : queueMap.entrySet()) {
                Predicate predicate = entry.getKey();
                //check if the record meets a given predicate
                if (!(predicate instanceof DefaultPredicate) && predicate.matches(record)) {
                    //put it in the mapped queue
                    queueMap.get(predicate).send(payload);
                    matched = true;
                    break;
                }
            }
            //if the record does not match any predicate, then put it in the default queue
            if (!matched && defaultQueue != null) {
                defaultQueue.send(payload);
            }
        }
    }

    @Override
    public void close() throws Exception {

    }

    Map<Predicate, QueueSender> getQueueMap() {
        return queueMap;
    }
}
