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
package org.easybatch.extensions.stream;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;

import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Reader that reads records form a {@link Stream}.
 *
 * This reader produces {@link GenericRecord} instances.
 *
 * @param <T> Type of elements in the stream.
 * @author Charles Fleury
 * @since 5.0
 */
public class StreamRecordReader<T> implements RecordReader {

    protected Stream<T> dataSource;
    protected Iterator<T> iterator;
    protected long currentRecordNumber;

    /**
     * Create a new {@link StreamRecordReader} to read records from a {@link Stream}.
     *
     * @param dataSource to read records from
     */
    public StreamRecordReader(final Stream<T> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void open() throws Exception {
        if (dataSource == null) {
            throw new IllegalArgumentException("The stream must not be null");
        }
        currentRecordNumber = 0;
        iterator = dataSource.iterator();
    }

    @Override
    public Record<T> readRecord() throws Exception {
        if (iterator.hasNext()) {
            Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
            return new GenericRecord<>(header, iterator.next());
        } else {
            return null;
        }
    }

    private String getDataSourceName() {
        return "In-Memory Stream";
    }

    @Override
    public void close() throws Exception {
        dataSource.close();
    }
}
