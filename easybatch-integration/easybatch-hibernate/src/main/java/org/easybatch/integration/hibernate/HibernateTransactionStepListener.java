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

package org.easybatch.integration.hibernate;

import org.easybatch.core.api.event.step.RecordProcessorEventListener;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.checkArgument;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Listener that commits a Hibernate transaction after inserting a predefined number of records (commit-interval).
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class HibernateTransactionStepListener implements RecordProcessorEventListener {

    public static final int DEFAULT_COMMIT_INTERVAL = 1;

    private static final Logger LOGGER = Logger.getLogger(HibernateTransactionStepListener.class.getSimpleName());

    private Session session;

    private Transaction transaction;

    private int commitInterval;

    private int recordNumber;

    /**
     * Create a Hibernate transaction listener.
     * <p/>
     * A Hibernate transaction will be committed after each record.
     *
     * @param session the Hibernate session
     */
    public HibernateTransactionStepListener(final Session session) {
        this(session, DEFAULT_COMMIT_INTERVAL);
    }

    /**
     * Create a Hibernate transaction listener with a commit-interval value.
     *
     * @param session        the Hibernate session
     * @param commitInterval the commit interval
     */
    public HibernateTransactionStepListener(final Session session, final int commitInterval) {
        checkNotNull(session, "session");
        checkArgument(commitInterval >= 1, "max commit interval parameter must be greater than or equal to 1");
        this.commitInterval = commitInterval;
        this.session = session;
        this.recordNumber = 0;
        this.transaction = this.session.beginTransaction();
    }

    @Override
    public Object beforeRecordProcessing(final Object record) {
        return record;
    }

    @Override
    public void afterRecordProcessing(final Object record, final Object processingResult) {
        recordNumber++;
        try {
            if (recordNumber % commitInterval == 0) {
                LOGGER.info("Committing transaction after " + recordNumber + " record(s)");
                //commit current transaction
                session.flush();
                session.clear();
                transaction.commit();

                //begin a new transaction for next chunk
                transaction = session.beginTransaction();
                recordNumber = 0;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to commit transaction", e);
        }
    }

    @Override
    public void onRecordProcessingException(final Object record, final Throwable throwable) {
        try {
            transaction.rollback();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to rollback transaction", e);
        }
    }

}