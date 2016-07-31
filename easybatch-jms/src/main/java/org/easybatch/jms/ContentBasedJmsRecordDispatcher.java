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

import org.easybatch.core.dispatcher.AbstractRecordDispatcher;
import org.easybatch.core.dispatcher.DefaultPredicate;
import org.easybatch.core.dispatcher.Predicate;

import javax.jms.Message;
import javax.jms.QueueSender;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Dispatch records to a list of Jms Queues based on their content.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ContentBasedJmsRecordDispatcher extends AbstractRecordDispatcher<JmsRecord> {

    /**
     * Map a predicate to a queue: when the record content matches the predicate,
     * then it is dispatched to the mapped queue.
     */
    private Map<Predicate<JmsRecord>, QueueSender> queueMap;

    /**
     * A delegate dispatcher used to broadcast poison records to all queues.
     */
    private BroadcastJmsRecordDispatcher broadcastJmsRecordDispatcher;

    ContentBasedJmsRecordDispatcher(Map<Predicate<JmsRecord>, QueueSender> queueMap) {
        this.queueMap = queueMap;
        List<QueueSender> queues = new ArrayList<>();
        for (QueueSender queue : queueMap.values()) {
            queues.add(queue);
        }
        broadcastJmsRecordDispatcher = new BroadcastJmsRecordDispatcher(queues);
    }

    @Override
    protected void dispatchRecord(final JmsRecord record) throws Exception {
        // when receiving a poising record, broadcast it to all queues
        if (record instanceof JmsPoisonRecord) {
            broadcastJmsRecordDispatcher.dispatchRecord(record);
            return;
        }

        Message payload = record.getPayload();
        for (Map.Entry<Predicate<JmsRecord>, QueueSender> entry : queueMap.entrySet()) {
            Predicate<JmsRecord> predicate = entry.getKey();
            //check if the record meets a given predicate
            if (!(predicate instanceof DefaultPredicate) && predicate.matches(record)) {
                //put it in the mapped queue
                queueMap.get(predicate).send(payload);
                return;
            }
        }
        //if the record does not match any predicate, then put it in the default queue
        QueueSender defaultQueue = queueMap.get(new DefaultPredicate());
        if (defaultQueue != null) {
            defaultQueue.send(payload);
        }

    }

}
