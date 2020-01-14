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
package org.easybatch.extensions.hibernate;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.hibernate.*;

import java.util.Date;

import static org.easybatch.core.util.Utils.checkArgument;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Read records using Hibernate API.
 *
 * This reader produces {@link GenericRecord} instances with domain objects as payload.
 *
 * @param <T> the type of objects this reader will read.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */

@SuppressWarnings(value = "unchecked") // argh hibernate APIs that return raw types ..
public class HibernateRecordReader<T> implements RecordReader {

    private SessionFactory sessionFactory;
    private Session session;
    private String query;
    private ScrollableResults scrollableResults;

    //parameters
    private int maxResults;
    private int fetchSize;
    private long currentRecordNumber;

    /**
     * Create a new {@link HibernateRecordReader}.
     *
     * @param sessionFactory a pre-configured hibernate session factory
     * @param query          the HQL query to use to fetch data
     */
    public HibernateRecordReader(final SessionFactory sessionFactory, final String query) {
        checkNotNull(sessionFactory, "session factory");
        checkNotNull(query, "query");
        this.sessionFactory = sessionFactory;
        this.query = query;
    }

    @Override
    public void open() {
        session = sessionFactory.openSession();
        currentRecordNumber = 0;
        Query hibernateQuery = session.createQuery(query);
        hibernateQuery.setReadOnly(true);
        if (maxResults >= 1) {
            hibernateQuery.setMaxResults(maxResults);
        }
        if (fetchSize >= 1) {
            hibernateQuery.setFetchSize(fetchSize);
        }
        scrollableResults = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
    }

    private boolean hasNextRecord() {
        return scrollableResults.next();
    }

    @Override
    public GenericRecord<T> readRecord() {
        if (hasNextRecord()) {
            Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
            return new GenericRecord<>(header, (T) scrollableResults.get()[0]);
        } else {
            return null;
        }
    }

    private String getDataSourceName() {
        return "Result of HQL query: " + query;
    }

    @Override
    public void close() {
        session.close();
    }

    /**
     * Set the max results to fetch.
     *
     * @param maxResults the maximum results to fetch
     */
    public void setMaxResults(final int maxResults) {
        checkArgument(maxResults >= 1, "max result parameter must be greater than or equal to 1");
        this.maxResults = maxResults;
    }

    /**
     * Set the fetch size
     *
     * @param fetchSize the fetch size
     */
    public void setFetchSize(final int fetchSize) {
        checkArgument(fetchSize >= 1, "fetch size parameter must be greater than or equal to 1");
        this.fetchSize = fetchSize;
    }

}
