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

import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;

import java.util.Date;
import java.util.Iterator;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Reads record from an {@link Iterable} data source.
 *
 * This reader produces {@link GenericRecord} instances containing original objects from the data source.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class IterableRecordReader implements RecordReader {

    private long currentRecordNumber;
    private Iterator iterator;

    /**
     * Create a new {@link IterableRecordReader}.
     *
     * @param dataSource to read records from.
     */
    public IterableRecordReader(final Iterable dataSource) {
        checkNotNull(dataSource, "data source");
        this.iterator = dataSource.iterator();
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
    }

    @Override
    public GenericRecord readRecord() throws Exception {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        if (iterator.hasNext()) {
            return new GenericRecord<>(header, iterator.next());
        } else {
            return null;
        }
    }

    private String getDataSourceName() {
        return "In-Memory Iterable";
    }

    @Override
    public void close() {
        // no op
    }
}
