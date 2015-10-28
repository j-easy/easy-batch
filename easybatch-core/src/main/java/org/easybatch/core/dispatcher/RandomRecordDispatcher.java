/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

import org.easybatch.core.record.PoisonRecord;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import static java.lang.String.format;

/**
 * Dispatch records randomly to a list of {@link BlockingQueue}.
 *
 *  @param <T> type of record to dispatch
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class RandomRecordDispatcher<T> extends AbstractRecordDispatcher<T> {

    /**
     * The total number of queues this dispatcher operates on.
     */
    private int queuesNumber;

    /**
     * List of queues to which records should be dispatched.
     */
    private List<BlockingQueue<T>> queues;

    /**
     * A delegate dispatcher used to broadcast poison records to all queues.
     */
    private BroadcastRecordDispatcher<T> broadcastRecordDispatcher;

    /**
     * The random generator.
     */
    private Random random;

    /**
     * Create a {@link RandomRecordDispatcher} instance.
     *
     * @param queues the list of queues to which records should be dispatched
     */
    public RandomRecordDispatcher(List<BlockingQueue<T>> queues) {
        this.queues = queues;
        this.queuesNumber = queues.size();
        this.random = new Random();
        this.broadcastRecordDispatcher = new BroadcastRecordDispatcher<T>(queues);
    }

    @Override
    public void dispatchRecord(T record) throws RecordDispatchingException {
        // when receiving a poising record, broadcast it to all queues
        if (record instanceof PoisonRecord) {
            broadcastRecordDispatcher.dispatchRecord(record);
            return;
        }
        //dispatch record randomly to one of the queues
        BlockingQueue<T> queue = null;
        try {
            queue = queues.get(random.nextInt(queuesNumber));
            queue.put(record);
        } catch (InterruptedException e) {
            String message = format("Unable to put record %s in queue %s", record, queue);
            throw new RecordDispatchingException(message, e);
        }
    }

}
