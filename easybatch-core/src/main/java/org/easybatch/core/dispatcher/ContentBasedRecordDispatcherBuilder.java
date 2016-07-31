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

package org.easybatch.core.dispatcher;

import org.easybatch.core.record.Record;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Builder for {@link ContentBasedRecordDispatcher}.
 *
 * @param <T> type of record
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ContentBasedRecordDispatcherBuilder<T extends Record> {

    private Predicate<T> predicate;

    private Map<Predicate<T>, BlockingQueue<T>> queueMap;

    public ContentBasedRecordDispatcherBuilder() {
        queueMap = new HashMap<>();
    }

    public ContentBasedRecordDispatcherBuilder<T> when(Predicate<T> predicate) {
        this.predicate = predicate;
        return this;
    }

    public ContentBasedRecordDispatcherBuilder<T> dispatchTo(BlockingQueue<T> queue) {
        if (predicate == null) {
            throw new IllegalStateException("You should specify a predicate before mapping a queue." +
                    " Please ensure that you call when() -> dispatchTo() -> otherwise()  methods in that order");
        }
        queueMap.put(predicate, queue);
        predicate = null;
        return this;
    }

    public ContentBasedRecordDispatcherBuilder<T> otherwise(BlockingQueue<T> queue) {
        queueMap.put(new DefaultPredicate<T>(), queue);
        predicate = null;
        return this;
    }

    public ContentBasedRecordDispatcher<T> build() {
        if (queueMap.isEmpty()) {
            throw new IllegalStateException("You can not build a ContentBasedRecordDispatcher with an empty <Predicate, Queue> mapping.");
        }
        return new ContentBasedRecordDispatcher<>(queueMap);
    }

}
