/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

import org.easybatch.core.record.MultiRecord;

import static org.easybatch.core.util.Utils.checkArgument;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Read a chunk of records at a time.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public abstract class MultiRecordReader implements RecordReader {
    
    protected RecordReader delegate;
    
    protected int chunkSize;
    
    protected long currentRecordNumber;

    public MultiRecordReader(final int chunkSize, final RecordReader delegate) {
        checkArgument(chunkSize >= 1, "chunk size must be greater than or equal to 1");
        checkNotNull(delegate, "delegate record reader");
        this.chunkSize = chunkSize;
        this.delegate = delegate;
    }

    public void open() throws RecordReaderOpeningException {
        delegate.open();
    }

    public abstract boolean hasNextRecord();

    public abstract MultiRecord readNextRecord() throws RecordReadingException;

    public Long getTotalRecords() {
        return delegate.getTotalRecords();
    }

    public String getDataSourceName() {
        return delegate.getDataSourceName();
    }

    public void close() throws RecordReaderClosingException {
        delegate.close();
    }
}
