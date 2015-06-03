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

package org.easybatch.integration.jpa;

import org.easybatch.core.api.Header;
import org.easybatch.core.api.RecordReader;
import org.easybatch.core.record.GenericRecord;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Reader that reads data using the Java Persistence API.
 * <p/>
 * This reader produces {@link GenericRecord} instances that can be mapped
 * with {@link org.easybatch.core.mapper.GenericRecordMapper} in order to get the raw objects.
 *
 * @param <T> the type of objects this reader will read.
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JpaRecordReader<T> implements RecordReader {

    private EntityManager entityManager;

    private String query;

    private Class<T> type;

    private List<T> records;

    private Iterator<T> iterator;

    private boolean maxResultsEnabled;

    private int maxResults;

    private long currentRecordNumber;

    public JpaRecordReader(EntityManagerFactory entityManagerFactory, String query, Class<T> type) {
        this.entityManager = entityManagerFactory.createEntityManager();
        this.query = query;
        this.type = type;
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        TypedQuery<T> typedQuery = entityManager.createQuery(query, type);
        if (maxResultsEnabled) {
            typedQuery.setMaxResults(maxResults);
        }
        records = typedQuery.getResultList();
        iterator = records.iterator();
    }

    @Override
    public boolean hasNextRecord() {
        return iterator.hasNext();
    }

    @Override
    public GenericRecord<T> readNextRecord() throws Exception {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new GenericRecord<T>(header, iterator.next());
    }

    @Override
    public Long getTotalRecords() {
        return (long) records.size();
    }

    @Override
    public String getDataSourceName() {
        return "Result of JPA query: " + query;
    }

    @Override
    public void close() throws Exception {
        entityManager.close();
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        this.maxResultsEnabled = true;
    }

}
