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

import javax.jms.QueueSender;
import java.util.List;
import java.util.Random;

/**
 * Dispatch records randomly to a list of Jms queues.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class RandomJmsRecordDispatcher extends AbstractRecordDispatcher<JmsRecord> {

    /**
     * The total number of queues this dispatcher operates on.
     */
    private int queuesNumber;

    /**
     * List of queues to which records should be dispatched.
     */
    private List<QueueSender> queues;

    /**
     * A delegate dispatcher used to broadcast poison records to all queues.
     */
    private BroadcastJmsRecordDispatcher broadcastJmsRecordDispatcher;

    /**
     * The random generator.
     */
    private Random random;

    /**
     * Create a {@link RandomJmsRecordDispatcher} instance.
     *
     * @param queues the list of queues to which records should be dispatched
     */
    public RandomJmsRecordDispatcher(List<QueueSender> queues) {
        this.queues = queues;
        this.queuesNumber = queues.size();
        this.random = new Random();
        this.broadcastJmsRecordDispatcher = new BroadcastJmsRecordDispatcher(queues);
    }

    @Override
    public void dispatchRecord(JmsRecord record) throws Exception {
        // when receiving a poising record, broadcast it to all queues
        if (record instanceof JmsPoisonRecord) {
            broadcastJmsRecordDispatcher.dispatchRecord(record);
            return;
        }
        //dispatch record randomly to one of the queues
        QueueSender queue = queues.get(random.nextInt(queuesNumber));
        queue.send(record.getPayload());
    }

}
