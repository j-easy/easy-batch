package org.easybatch.integration.jpa;

import org.easybatch.core.api.RecordReader;
import org.easybatch.core.record.GenericRecord;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Iterator;
import java.util.List;

/**
 * Reader that reads data using the JPA API.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JpaRecordReader<T> implements RecordReader {

    private EntityManager entityManager;

    private String query;

    private Class<T> type;
    
    private Iterator<T> iterator;

    private boolean maxResultsEnabled;

    private int maxResults;
    
    private int currentRecordNumber;

    public JpaRecordReader(EntityManagerFactory entityManagerFactory, String query, Class<T> type) {
        this.entityManager = entityManagerFactory.createEntityManager();
        this.query = query;
        this.type = type;
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        TypedQuery<T> typedQuery = entityManager.createQuery(this.query, type);
        if (maxResultsEnabled) {
            typedQuery.setMaxResults(maxResults);
        }
        List<T> list = typedQuery.getResultList();
        iterator = list.iterator();
    }

    @Override
    public boolean hasNextRecord() {
        return iterator.hasNext();
    }

    @Override
    public GenericRecord<T> readNextRecord() throws Exception {
        return new GenericRecord<T>(++currentRecordNumber, iterator.next());
    }

    @Override
    public Integer getTotalRecords() {
        return null;
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
