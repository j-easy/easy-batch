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

import org.easybatch.core.api.Header;
import org.easybatch.core.api.RecordReader;
import org.easybatch.core.record.GenericRecord;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * A convenient {@link org.easybatch.core.api.RecordReader} that reads data from a {@link java.util.List} of objects.
 * <p/>
 * This reader returns {@link org.easybatch.core.record.GenericRecord} instances that should be mapped
 * with {@link org.easybatch.core.mapper.GenericRecordMapper} in order to get the raw objects from the list.
 *
 * @param <T> the type of objects contained in the list
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ListRecordReader<T> implements RecordReader {

    /**
     * The data source.
     */
    private List<T> dataSource;

    /**
     * The current record number.
     */
    private long currentRecordNumber;

    /**
     * The data source iterator.
     */
    private Iterator<T> iterator;

    /**
     * Constructs a {@link ListRecordReader}.
     *
     * @param dataSource The list data source
     */
    public ListRecordReader(final List<T> dataSource) {
        this.dataSource = dataSource;
        this.iterator = dataSource.listIterator();
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
    }

    @Override
    public boolean hasNextRecord() {
        return iterator.hasNext();
    }

    @Override
    public GenericRecord<T> readNextRecord() {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new GenericRecord<T>(header, iterator.next());
    }

    @Override
    public Long getTotalRecords() {
        return (long) dataSource.size();
    }

    @Override
    public String getDataSourceName() {
        return "In-Memory List";
    }

    @Override
    public void close() {
    }
}
