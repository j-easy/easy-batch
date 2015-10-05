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
import org.easybatch.core.api.RecordDispatchingException;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import static java.lang.String.format;

/**
 * A record dispatcher that broadcasts input records to a list of queues.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class BroadcastRecordDispatcher extends AbstractRecordDispatcher {

    /**
     * List of queues to which records should be dispatched.
     */
    private List<BlockingQueue<Record>> queues;

    /**
     * Create a {@link BroadcastRecordDispatcher} instance.
     *
     * @param queues the list of queues to which records should be dispatched
     */
    public BroadcastRecordDispatcher(List<BlockingQueue<Record>> queues) {
        this.queues = queues;
    }

    @Override
    public void dispatchRecord(final Record record) throws RecordDispatchingException {
        for (BlockingQueue<Record> queue : queues) {
            try {
                queue.put(record);
            } catch (InterruptedException e) {
                String message = format("Unable to put record %s in queue %s", record, queue);
                throw new RecordDispatchingException(message, e);
            }
        }
    }

}
