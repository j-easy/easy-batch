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

    private ContentBasedJmsQueueRecordWriterBuilder() {
        // force usage of static factory method newContentBasedJmsQueueRecordWriterBuilder
    }

    /**
     * Create a new {@link ContentBasedJmsQueueRecordWriterBuilder}.
     */
    public static WhenStep newContentBasedJmsQueueRecordWriterBuilder() {
        return new Steps();
    }

    public interface WhenStep {
        /**
         * Register a predicate.
         *
         * @param predicate to register
         * @return the builder instance
         */
        WriteToStep when(Predicate predicate);
    }

    public interface WriteToStep {
        /**
         * Register a queue.
         *
         * @param queue to register
         * @return the builder instance
         */
        OtherwiseStep writeTo(QueueSender queue);
    }

    public interface OtherwiseStep {
        /**
         * Register a default queue.
         *
         * @param queue default queue to register
         * @return the builder instance
         */
        BuildStep otherwise(QueueSender queue);

        /**
         * Register a predicate.
         *
         * @param predicate to register
         * @return the builder instance
         */
        WriteToStep when(Predicate predicate);

        /**
         * Create a new {@link ContentBasedJmsQueueRecordWriter}.
         *
         * @return a new {@link ContentBasedJmsQueueRecordWriter}
         */
        ContentBasedJmsQueueRecordWriter build();
    }

    public interface BuildStep {
        /**
         * Create a new {@link ContentBasedJmsQueueRecordWriter}.
         *
         * @return a new {@link ContentBasedJmsQueueRecordWriter}
         */
        ContentBasedJmsQueueRecordWriter build();
    }

    private static class Steps implements WhenStep, WriteToStep, OtherwiseStep, BuildStep {

        private Predicate predicate;

        private Map<Predicate, QueueSender> queueMap;

        Steps() {
            queueMap = new HashMap<>();
        }

        @Override
        public WriteToStep when(Predicate predicate) {
            this.predicate = predicate;
            return this;
        }

        @Override
        public OtherwiseStep writeTo(QueueSender queue) {
            queueMap.put(predicate, queue);
            return this;
        }

        @Override
        public BuildStep otherwise(QueueSender queue) {
            queueMap.put(new DefaultPredicate(), queue);
            return this;
        }

        @Override
        public ContentBasedJmsQueueRecordWriter build() {
            return new ContentBasedJmsQueueRecordWriter(queueMap);
        }
    }

}
