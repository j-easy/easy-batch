/*
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
package org.jeasy.batch.extensions.integration;

import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.writer.RecordWriter;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Write records to a list of {@link BlockingQueue} based on their content.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ContentBasedBlockingQueueRecordWriter<P> implements RecordWriter<P> {

    /**
     * Map a predicate to a queue: when the record content matches the predicate,
     * then it is written to the mapped queue.
     */
    private Map<Predicate<P>, BlockingQueue<Record<P>>> queueMap;

    /**
     * Create a new content based blocking queue writer.
     * @param queueMap mapping between predicates and queues
     */
    public ContentBasedBlockingQueueRecordWriter(Map<Predicate<P>, BlockingQueue<Record<P>>> queueMap) {
        this.queueMap = queueMap;
    }

    @Override
    public void writeRecords(Batch<P> batch) throws Exception {
        DefaultPredicate<P> defaultPredicate = new DefaultPredicate<>();
        BlockingQueue<Record<P>> defaultQueue = queueMap.get(defaultPredicate);
        for (Record<P> record : batch) {
            boolean matched = false;
            for (Map.Entry<Predicate<P>, BlockingQueue<Record<P>>> entry : queueMap.entrySet()) {
                Predicate<P> predicate = entry.getKey();
                BlockingQueue<Record<P>> queue = entry.getValue();
                //check if the record meets a given predicate
                if (!(predicate instanceof DefaultPredicate) && predicate.matches(record)) {
                    //if so, put it in the mapped queue
                    queue.put(record);
                    matched = true;
                    break;
                }
            }
            //if the record does not match any predicate, then put it in the default queue
            if (!matched && defaultQueue != null) {
                defaultQueue.put(record);
            }
        }
    }

    public Map<Predicate<P>, BlockingQueue<Record<P>>> getQueueMap() {
        return queueMap;
    }
}
