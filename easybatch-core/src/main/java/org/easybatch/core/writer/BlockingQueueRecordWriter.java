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

package org.easybatch.core.writer;

import org.easybatch.core.record.Record;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import static java.util.Collections.singletonList;

/**
 * Write records to a (list of) {@link BlockingQueue}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BlockingQueueRecordWriter implements RecordWriter {

    private List<BlockingQueue<Record>> blockingQueues;

    public BlockingQueueRecordWriter(final BlockingQueue<Record> blockingQueue) {
        this(singletonList(blockingQueue));
    }

    public BlockingQueueRecordWriter(final List<BlockingQueue<Record>> blockingQueues) {
        this.blockingQueues = blockingQueues;
    }

    @Override
    public void open() throws Exception {

    }

    @Override
    public void writeRecord(Record record) throws Exception {
        for (BlockingQueue<Record> queue : blockingQueues) {
            queue.put(record);
        }
    }

    @Override
    public void close() throws Exception {

    }
}
