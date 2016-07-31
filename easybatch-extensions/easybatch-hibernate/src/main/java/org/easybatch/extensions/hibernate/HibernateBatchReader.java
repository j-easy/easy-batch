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

package org.easybatch.extensions.hibernate;

import org.easybatch.core.reader.AbstractBatchReader;
import org.easybatch.core.reader.RecordReadingException;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Reads records in batches using Hibernate.
 *
 * @param <T> the type of objects this reader will read.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class HibernateBatchReader<T> extends AbstractBatchReader {

    /**
     * Create a hibernate batch reader.
     *
     * @param batchSize      the batch size
     * @param sessionFactory a pre-configured hibernate session factory
     * @param query          the HQL query to use to fetch data
     */
    public HibernateBatchReader(int batchSize, final SessionFactory sessionFactory, final String query) {
        super(batchSize, new HibernateRecordReader<T>(sessionFactory, query));
    }

    @Override
    public Batch readNextRecord() throws RecordReadingException {
        List<Record> records = new ArrayList<>();
        int items = 1;
        do {
            records.add(delegate.readNextRecord());
        } while (items++ < batchSize && hasNextRecord());
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new Batch(header, records);
    }
}
