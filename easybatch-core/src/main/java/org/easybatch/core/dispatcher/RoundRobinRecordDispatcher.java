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

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordDispatcher;
import org.easybatch.core.record.PoisonRecord;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * A {@link org.easybatch.core.api.RecordDispatcher} that dispatches records to a list of queues in round-robin fashion.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class RoundRobinRecordDispatcher implements RecordDispatcher {

    /**
     * The total number of queues this dispatcher operates on.
     */
    private int queuesNumber;

    /**
     * Next queue to which dispatch next incoming record.
     */
    private int next;

    /**
     * List of queues to which records should be dispatched.
     */
    private List<BlockingQueue<Record>> queues;

    /**
     * A delegate dispatcher used to broadcast poison records to all queues.
     */
    private BroadcastRecordDispatcher broadcastRecordDispatcher;

    public RoundRobinRecordDispatcher(List<BlockingQueue<Record>> queues) {
        this.queues = queues;
        this.queuesNumber = queues.size();
        this.broadcastRecordDispatcher = new BroadcastRecordDispatcher(queues);
    }

    @Override
    public void dispatchRecord(Record record) throws Exception {
        // when receiving a poising record, broadcast it to all queues
        if (record instanceof PoisonRecord) {
            broadcastRecordDispatcher.dispatchRecord(record);
            return;
        }
        //dispatch records to queues in round-robin fashion
        queues.get(next++ % queuesNumber).put(record);
    }

}
