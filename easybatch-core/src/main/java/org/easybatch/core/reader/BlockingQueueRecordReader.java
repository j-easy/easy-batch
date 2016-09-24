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

package org.easybatch.core.reader;

import org.easybatch.core.record.PoisonRecord;
import org.easybatch.core.record.Record;

import java.util.concurrent.BlockingQueue;

/**
 * A convenient {@link RecordReader} that reads record from a {@link BlockingQueue}.
 *
 * @param <T> the type of elements in the queue
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BlockingQueueRecordReader<T extends Record> implements RecordReader {

    /**
     * The stop reading flag.
     */
    private boolean stop;

    /**
     * The number of poison records received.
     */
    private int poisonRecords;

    /**
     * The total number of poison records to receive to stop the queue.
     */
    private int totalPoisonRecords;

    /**
     * The source queue.
     */
    private BlockingQueue<T> queue;

    /**
     * Create a {@link BlockingQueueRecordReader}.
     *
     * @param queue the queue to read records from
     */
    public BlockingQueueRecordReader(final BlockingQueue<T> queue) {
        this(queue, 1);
    }

    /**
     * Create a {@link BlockingQueueRecordReader}.
     *
     * @param queue the queue to read records from
     * @param totalPoisonRecords number of poison records to receive to stop reading from the queue
     */
    public BlockingQueueRecordReader(final BlockingQueue<T> queue, int totalPoisonRecords) {
        this.queue = queue;
        this.totalPoisonRecords = totalPoisonRecords;
    }

    @Override
    public void open() {
        poisonRecords = 0;
    }

    @Override
    public boolean hasNextRecord() {
        return !stop;
    }

    @Override
    public T readNextRecord() throws RecordReadingException {
        try {
            T record = queue.take();
            if (record instanceof PoisonRecord) {
                poisonRecords++;
            }
            stop = poisonRecords == totalPoisonRecords;
            return record;
        } catch (InterruptedException e) {
            throw new RecordReadingException("Unable to read next record from the queue", e);
        }
    }

    @Override
    public Long getTotalRecords() {
        return null;
    }

    @Override
    public String getDataSourceName() {
        return "In-Memory Queue";
    }

    @Override
    public void close() {
        // no op
    }

}
