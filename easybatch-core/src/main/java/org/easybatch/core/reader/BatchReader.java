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

package org.easybatch.core.reader;

import org.easybatch.core.record.Batch;

import static org.easybatch.core.util.Utils.checkArgument;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Read a batch of records.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class BatchReader implements RecordReader {

    protected RecordReader delegate;

    protected int batchSize;

    protected long currentRecordNumber;

    public BatchReader(final int batchSize, final RecordReader delegate) {
        checkArgument(batchSize >= 1, "batch size must be >= 1");
        checkNotNull(delegate, "delegate record reader");
        this.batchSize = batchSize;
        this.delegate = delegate;
    }

    @Override
    public void open() throws RecordReaderOpeningException {
        delegate.open();
    }

    @Override
    public abstract boolean hasNextRecord();

    @Override
    public abstract Batch readNextRecord() throws RecordReadingException;

    @Override
    public Long getTotalRecords() {
        return delegate.getTotalRecords();
    }

    @Override
    public String getDataSourceName() {
        return delegate.getDataSourceName();
    }

    @Override
    public void close() throws RecordReaderClosingException {
        delegate.close();
    }
}
