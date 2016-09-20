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

import org.easybatch.core.listener.BatchListener;
import org.easybatch.core.record.Batch;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Listener that commits a Hibernate transaction after inserting a record.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class HibernateTransactionListener implements BatchListener {

    private static final Logger LOGGER = Logger.getLogger(HibernateTransactionListener.class.getSimpleName());

    private Session session;

    private Transaction transaction;

    /**
     * Create a Hibernate transaction listener.
     *
     * @param session the Hibernate session
     */
    public HibernateTransactionListener(final Session session) {
        checkNotNull(session, "session");
        this.session = session;
    }

    @Override
    public void beforeBatchReading() {
        transaction = session.getTransaction();
        transaction.begin();
    }

    @Override
    public void afterBatchProcessing(Batch batch) {
        // no op
    }

    @Override
    public void afterBatchWriting(Batch batch) {
        try {
            session.flush();
            session.clear();
            transaction.commit();
            LOGGER.info("Transaction committed");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to commit transaction", e);
        }
    }

    @Override
    public void onBatchWritingException(Batch batch, Throwable throwable) {
        try {
            transaction.rollback();
            LOGGER.info("Transaction rolled back");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to rollback transaction", e);
        }
    }
}
