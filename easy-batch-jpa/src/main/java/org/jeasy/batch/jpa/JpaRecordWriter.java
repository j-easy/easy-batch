/*
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
package org.jeasy.batch.jpa;

import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.writer.RecordWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import static org.jeasy.batch.core.util.Utils.checkNotNull;

/**
 * Write entities to a relational database using JPA.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 * @param <P> type of the record's payload
 */
public class JpaRecordWriter<P> implements RecordWriter<P> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaRecordWriter.class.getSimpleName());

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Create a new {@link JpaRecordWriter}.
     *
     * @param entityManagerFactory the entity manager factory.
     */
    public JpaRecordWriter(final EntityManagerFactory entityManagerFactory) {
        checkNotNull(entityManagerFactory, "entity manager factory");
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void open() {
        LOGGER.debug("Creating a JPA entity manager");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public void writeRecords(Batch<P> batch) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            for (Record<P> record : batch) {
                entityManager.persist(record.getPayload());
            }
            entityManager.flush();
            entityManager.clear();
            transaction.commit();
            LOGGER.debug("Transaction committed");
        } catch (Exception e) {
            LOGGER.error("Unable to commit transaction", e);
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void close() {
        if (entityManager != null ) {
            LOGGER.debug("Closing JPA entity manager");
            entityManager.close();
        }
    }
}
