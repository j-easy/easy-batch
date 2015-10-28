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

package org.easybatch.extensions.hibernate;

import org.easybatch.core.reader.AbstractMultiRecordReader;
import org.easybatch.core.reader.RecordReadingException;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.MultiRecord;
import org.easybatch.core.record.Record;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Reads records in chunks using Hibernate.
 *
 * @param <T> the type of objects this reader will read.
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class HibernateMultiRecordReader<T> extends AbstractMultiRecordReader {

    /**
     * Create a hibernate multi-record reader.
     *
     * @param chunkSize      the number of records to read at a time
     * @param sessionFactory a pre-configured hibernate session factory
     * @param query          the HQL query to use to fetch data
     */
    public HibernateMultiRecordReader(int chunkSize, final SessionFactory sessionFactory, final String query) {
        super(chunkSize, new HibernateRecordReader<T>(sessionFactory, query));
    }

    @Override
    public MultiRecord readNextRecord() throws RecordReadingException {
        List<Record> records = new ArrayList<Record>();
        int items = 1;
        do {
            records.add(delegate.readNextRecord());
        } while (items++ < chunkSize && hasNextRecord());
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new MultiRecord(header, records);
    }
}
