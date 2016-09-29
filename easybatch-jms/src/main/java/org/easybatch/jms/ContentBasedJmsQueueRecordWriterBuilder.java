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

package org.easybatch.jms;

import org.easybatch.core.writer.DefaultPredicate;
import org.easybatch.core.writer.Predicate;

import javax.jms.QueueSender;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder for {@link ContentBasedJmsQueueRecordWriter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ContentBasedJmsQueueRecordWriterBuilder {

    private Predicate predicate;

    private Map<Predicate, QueueSender> queueMap;

    /**
     * Create a new {@link ContentBasedJmsQueueRecordWriterBuilder}.
     */
    public ContentBasedJmsQueueRecordWriterBuilder() {
        queueMap = new HashMap<>();
    }

    /**
     * Register a predicate.
     *
     * @param predicate to register
     * @return the builder instance
     */
    public ContentBasedJmsQueueRecordWriterBuilder when(Predicate predicate) {
        this.predicate = predicate;
        return this;
    }

    /**
     * Register a queue.
     *
     * @param queue to register
     * @return the builder instance
     */
    public ContentBasedJmsQueueRecordWriterBuilder writeTo(QueueSender queue) {
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
     * @param queue default queue to register
     * @return the builder instance
     */
    public ContentBasedJmsQueueRecordWriterBuilder otherwise(QueueSender queue) {
        queueMap.put(new DefaultPredicate(), queue);
        predicate = null;
        return this;
    }

    /**
     * Create a new {@link ContentBasedJmsQueueRecordWriter}.
     *
     * @return a new {@link ContentBasedJmsQueueRecordWriter}
     */
    public ContentBasedJmsQueueRecordWriter build() {
        if (queueMap.isEmpty()) {
            throw new IllegalStateException("You can not build a ContentBasedJmsQueueRecordWriter with an empty <Predicate, Queue> mapping.");
        }
        return new ContentBasedJmsQueueRecordWriter(queueMap);
    }

}
