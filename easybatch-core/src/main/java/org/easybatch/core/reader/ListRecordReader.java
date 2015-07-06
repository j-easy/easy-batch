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

import java.util.List;

/**
 * A convenient {@link org.easybatch.core.api.RecordReader} that reads data from a {@link java.util.List} of objects.
 * <p/>
 * This reader returns {@link org.easybatch.core.record.GenericRecord} instances that should be mapped
 * with {@link org.easybatch.core.mapper.GenericRecordMapper} in order to get the raw objects from the list.
 *
 * <p/>
 * <strong>Deprecated:</strong> use {@link IterableRecordReader} instead.
 *
 * @param <T> the type of objects contained in the list
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@Deprecated
public class ListRecordReader<T> extends IterableRecordReader<T> {

    private List<T> dataSource;

    /**
     * Constructs a {@link ListRecordReader}.
     *
     * @param dataSource The list data source
     */
    public ListRecordReader(final List<T> dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public Long getTotalRecords() {
        return (long) dataSource.size();
    }

    @Override
    public String getDataSourceName() {
        return "In-Memory List";
    }

}
