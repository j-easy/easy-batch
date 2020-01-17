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
package org.jeasy.batch.core.reader;

import org.jeasy.batch.core.record.Record;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A {@link RecordReader} that reads record from a {@link BlockingQueue}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BlockingQueueRecordReader implements RecordReader {

    public static final long DEFAULT_TIMEOUT = 60000;

    private BlockingQueue<Record> queue;
    private long timeout;

    /**
     * Create a new {@link BlockingQueueRecordReader}.
     *
     * @param queue the queue to read records from
     */
    public BlockingQueueRecordReader(final BlockingQueue<Record> queue) {
        this(queue, DEFAULT_TIMEOUT);
    }

    /**
     * Create a new {@link BlockingQueueRecordReader}.
     *
     * @param queue the queue to read records from
     * @param timeout in milliseconds after which the reader will return {@code null}
     */
    public BlockingQueueRecordReader(final BlockingQueue<Record> queue, final long timeout) {
        this.queue = queue;
        this.timeout = timeout;
    }

    @Override
    public void open() {

    }

    @Override
    public Record readRecord() throws Exception {
        return queue.poll(timeout, TimeUnit.MILLISECONDS); // returns null after timeout (See javadoc)
    }

    @Override
    public void close() {
        // no op
    }

}
