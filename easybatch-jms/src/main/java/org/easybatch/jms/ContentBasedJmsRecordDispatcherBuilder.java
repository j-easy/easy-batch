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

import org.easybatch.core.dispatcher.DefaultPredicate;
import org.easybatch.core.dispatcher.Predicate;

import javax.jms.QueueSender;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder for {@link ContentBasedJmsRecordDispatcher}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ContentBasedJmsRecordDispatcherBuilder {

    private Predicate<JmsRecord> predicate;

    private Map<Predicate<JmsRecord>, QueueSender> queueMap;

    public ContentBasedJmsRecordDispatcherBuilder() {
        queueMap = new HashMap<>();
    }

    public ContentBasedJmsRecordDispatcherBuilder when(Predicate<JmsRecord> predicate) {
        this.predicate = predicate;
        return this;
    }

    public ContentBasedJmsRecordDispatcherBuilder dispatchTo(QueueSender queue) {
        if (predicate == null) {
            throw new IllegalStateException("You should specify a predicate before mapping a queue." +
                    " Please ensure that you call when() -> dispatchTo() -> otherwise()  methods in that order");
        }
        queueMap.put(predicate, queue);
        predicate = null;
        return this;
    }

    public ContentBasedJmsRecordDispatcherBuilder otherwise(QueueSender queue) {
        queueMap.put(new DefaultPredicate<JmsRecord>(), queue);
        predicate = null;
        return this;
    }

    public ContentBasedJmsRecordDispatcher build() {
        if (queueMap.isEmpty()) {
            throw new IllegalStateException("You can not build a ContentBasedJmsRecordDispatcher with an empty <Predicate, Queue> mapping.");
        }
        return new ContentBasedJmsRecordDispatcher(queueMap);
    }

}
