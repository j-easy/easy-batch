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

package org.easybatch.core.writer;

import org.easybatch.core.record.Record;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Builder for {@link ContentBasedBlockingQueueRecordWriter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ContentBasedBlockingQueueRecordWriterBuilder {

    private Predicate predicate;

    private Map<Predicate, BlockingQueue<Record>> queueMap;

    /**
     * Create a {@link ContentBasedBlockingQueueRecordWriterBuilder}.
     */
    public ContentBasedBlockingQueueRecordWriterBuilder() {
        queueMap = new HashMap<>();
    }

    /**
     * Register a predicate.
     *
     * @param predicate to register
     * @return the builder instance
     */
    public ContentBasedBlockingQueueRecordWriterBuilder when(Predicate predicate) {
        this.predicate = predicate;
        return this;
    }

    /**
     * Register a queue.
     *
     * @param queue to register
     * @return the builder instance
     */
    public ContentBasedBlockingQueueRecordWriterBuilder writeTo(BlockingQueue<Record> queue) {
        if (predicate == null) {
            // TODO use step builder pattern to assist user in calling methods in the right order
            throw new IllegalStateException("You should specify a predicate before mapping a queue." +
                    " Please ensure that you call when() -> writeTo() -> otherwise()  methods in that order");
        }
        queueMap.put(predicate, queue);
        predicate = null;
        return this;
    }

    /**
     * Register a default queue.
     *
     * @param queue default queue
     * @return the builder instance
     */
    public ContentBasedBlockingQueueRecordWriterBuilder otherwise(BlockingQueue<Record> queue) {
        queueMap.put(new DefaultPredicate(), queue);
        predicate = null;
        return this;
    }

    /**
     * Create a {@link ContentBasedBlockingQueueRecordWriter}.
     *
     * @return a new {@link ContentBasedBlockingQueueRecordWriter}
     */
    public ContentBasedBlockingQueueRecordWriter build() {
        if (queueMap.isEmpty()) {
            throw new IllegalStateException("You can not build a ContentBasedQueueRecordWriter with an empty <Predicate, Queue> mapping.");
        }
        return new ContentBasedBlockingQueueRecordWriter(queueMap);
    }

}
