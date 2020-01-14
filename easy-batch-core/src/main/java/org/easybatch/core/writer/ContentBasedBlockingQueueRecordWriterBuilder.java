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

    private ContentBasedBlockingQueueRecordWriterBuilder() {
         // force usage of static factory method newContentBasedBlockingQueueRecordWriterBuilder
    }

    /**
     * Create a new {@link ContentBasedBlockingQueueRecordWriter}.
     */
    public static WhenStep newContentBasedBlockingQueueRecordWriterBuilder() {
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
        OtherwiseStep writeTo(BlockingQueue<Record> queue);
    }

    public interface OtherwiseStep {
        /**
         * Register a default queue.
         *
         * @param queue default queue to register
         * @return the builder instance
         */
        BuildStep otherwise(BlockingQueue<Record> queue);

        /**
         * Register a predicate.
         *
         * @param predicate to register
         * @return the builder instance
         */
        WriteToStep when(Predicate predicate);

        /**
         * Create a new {@link ContentBasedBlockingQueueRecordWriter}.
         *
         * @return a new {@link ContentBasedBlockingQueueRecordWriter}
         */
        ContentBasedBlockingQueueRecordWriter build();
    }

    public interface BuildStep {
        /**
         * Create a new {@link ContentBasedBlockingQueueRecordWriter}.
         *
         * @return a new {@link ContentBasedBlockingQueueRecordWriter}
         */
        ContentBasedBlockingQueueRecordWriter build();
    }

    private static class Steps implements WhenStep, WriteToStep, OtherwiseStep, BuildStep {

        private Predicate predicate;

        private Map<Predicate, BlockingQueue<Record>> queueMap;

        Steps() {
            queueMap = new HashMap<>();
        }

        @Override
        public WriteToStep when(Predicate predicate) {
            this.predicate = predicate;
            return this;
        }

        @Override
        public OtherwiseStep writeTo(BlockingQueue<Record> queue) {
            queueMap.put(predicate, queue);
            return this;
        }

        @Override
        public BuildStep otherwise(BlockingQueue<Record> queue) {
            queueMap.put(new DefaultPredicate(), queue);
            return this;
        }

        @Override
        public ContentBasedBlockingQueueRecordWriter build() {
            return new ContentBasedBlockingQueueRecordWriter(queueMap);
        }
    }

}
