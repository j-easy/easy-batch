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

package org.easybatch.jpa;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.easybatch.core.util.Utils.checkArgument;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Read records using the Java Persistence API.
 * <p/>
 * This reader produces {@link GenericRecord} instances with JPA entities as payload.
 *
 * @param <T> the type of objects this reader will read.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JpaRecordReader<T> implements RecordReader {

    public static final int DEFAULT_FETCH_SIZE = 1000;

    private EntityManager entityManager;

    private String query;

    private TypedQuery<T> typedQuery;

    private Class<T> type;

    private List<T> records;

    private Iterator<T> iterator;

    private int offset;

    private int fetchSize;

    private long currentRecordNumber;

    /**
     * Reader that reads data using the Java Persistence API.
     * <p/>
     * This reader produces {@link GenericRecord} instances with JPA entities as payload.
     *
     * @param entityManagerFactory the entity manager factory
     * @param query                the JPQL query to fetch data
     * @param type                 the target type
     */
    public JpaRecordReader(final EntityManagerFactory entityManagerFactory, final String query, final Class<T> type) {
        checkNotNull(entityManagerFactory, "entity manager factory");
        checkNotNull(query, "query");
        checkNotNull(type, "target type");
        this.entityManager = entityManagerFactory.createEntityManager();
        this.query = query;
        this.type = type;
        this.fetchSize = DEFAULT_FETCH_SIZE;
    }

    @Override
    public void open() {
        currentRecordNumber = 0;
        offset = 0;
        typedQuery = entityManager.createQuery(query, type);
        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(fetchSize);
        records = typedQuery.getResultList();
        iterator = records.iterator();
    }

    @Override
    public boolean hasNextRecord() {
        if (!iterator.hasNext()) {
            typedQuery.setFirstResult(offset += records.size());
            records = typedQuery.getResultList();
            iterator = records.iterator();
        }
        return iterator.hasNext();
    }

    @Override
    public GenericRecord<T> readNextRecord() {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new GenericRecord<>(header, iterator.next());
    }

    @Override
    public Long getTotalRecords() {
        return null;
    }

    @Override
    public String getDataSourceName() {
        return "Result of JPA query: " + query;
    }

    @Override
    public void close() {
        entityManager.close();
    }

    /**
     * Set the fetch size.
     * @param fetchSize the fetch size
     */
    public void setFetchSize(final int fetchSize) {
        checkArgument(fetchSize >= 1, "fetch size parameter must be >= 1");
        this.fetchSize = fetchSize;
    }

}
