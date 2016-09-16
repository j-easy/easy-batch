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

import org.easybatch.core.listener.RecordWriterListener;
import org.easybatch.core.record.Record;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Listener that commits a JPA transaction after each batch of records.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JpaTransactionListener implements RecordWriterListener {

    private static final Logger LOGGER = Logger.getLogger(JpaTransactionListener.class.getSimpleName());

    private EntityManager entityManager;

    private EntityTransaction transaction;

    /**
     * Create a JPA transaction listener.
     * <p/>
     * A JPA transaction will be committed after each record.
     *
     * @param entityManager the JPA entity manager
     */
    public JpaTransactionListener(final EntityManager entityManager) {
        checkNotNull(entityManager, "entity manager");
        this.entityManager = entityManager;
    }

    @Override
    public void beforeRecordWriting(List<Record> batch) {
        this.transaction = entityManager.getTransaction();
        this.transaction.begin();
    }

    @Override
    public void afterRecordWriting(List<Record> batch) {
        try {
            entityManager.flush();
            entityManager.clear();
            transaction.commit();
            LOGGER.info("Transaction committed");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to commit transaction", e);
        }
    }

    @Override
    public void onRecordWritingException(List<Record> batch, Throwable throwable) {
        try {
            transaction.rollback();
            LOGGER.info("Transaction rolled back");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to rollback transaction", e);
        }
    }
}
