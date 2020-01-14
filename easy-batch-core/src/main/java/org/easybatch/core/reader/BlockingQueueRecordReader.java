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
package org.easybatch.core.reader;

import org.easybatch.core.record.PoisonRecord;
import org.easybatch.core.record.Record;

import java.util.concurrent.BlockingQueue;

/**
 * A {@link RecordReader} that reads record from a {@link BlockingQueue}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BlockingQueueRecordReader implements RecordReader {

    private int poisonRecords;
    private int totalPoisonRecords;
    private BlockingQueue<Record> queue;

    /**
     * Create a new {@link BlockingQueueRecordReader}.
     *
     * @param queue the queue to read records from
     */
    public BlockingQueueRecordReader(final BlockingQueue<Record> queue) {
        this(queue, 1);
    }

    /**
     * Create a new {@link BlockingQueueRecordReader}.
     *
     * @param queue              the queue to read records from
     * @param totalPoisonRecords number of poison records to receive to stop reading from the queue
     */
    public BlockingQueueRecordReader(final BlockingQueue<Record> queue, int totalPoisonRecords) {
        this.queue = queue;
        this.totalPoisonRecords = totalPoisonRecords;
    }

    @Override
    public void open() {
        poisonRecords = 0;
    }

    @Override
    public Record readRecord() throws Exception {
        if (poisonRecords == totalPoisonRecords) {
            return null;
        }
        Record record = queue.take();
        if (record instanceof PoisonRecord) {
            poisonRecords++;
        }
        return record;
    }

    @Override
    public void close() {
        // no op
    }

}
