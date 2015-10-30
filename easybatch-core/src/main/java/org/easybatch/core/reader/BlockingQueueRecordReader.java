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

package org.easybatch.core.reader;

import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.PoisonRecord;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * A convenient {@link RecordReader} that reads record from a {@link BlockingQueue}.
 *
 * @param <T> the type of elements in the queue
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class BlockingQueueRecordReader<T> implements RecordReader {

    /**
     * The stop reading flag.
     */
    private boolean stop;

    /**
     * The source queue.
     */
    private BlockingQueue<T> queue;

    /**
     * The current record number.
     */
    private long currentRecordNumber;

    /**
     * Create a {@link BlockingQueueRecordReader}.
     *
     * @param queue the queue to read records from
     */
    public BlockingQueueRecordReader(final BlockingQueue<T> queue) {
        this.queue = queue;
    }

    @Override
    public void open() {
        currentRecordNumber = 0;
    }

    @Override
    public boolean hasNextRecord() {
        return !stop;
    }

    @Override
    public GenericRecord<T> readNextRecord() throws RecordReadingException {
        try {
            T record = queue.take();
            stop = record instanceof PoisonRecord;
            Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
            return new GenericRecord<>(header, record);
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
        return "Queue:" + queue;
    }

    @Override
    public void close() {
        // no op
    }

}
