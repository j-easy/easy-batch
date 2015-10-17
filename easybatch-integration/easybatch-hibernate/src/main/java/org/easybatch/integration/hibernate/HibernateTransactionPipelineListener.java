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

import org.easybatch.core.listener.PipelineListener;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Listener that commits a Hibernate transaction after inserting a record.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class HibernateTransactionPipelineListener implements PipelineListener {

    private static final Logger LOGGER = Logger.getLogger(HibernateTransactionPipelineListener.class.getSimpleName());

    private Session session;

    private Transaction transaction;

    private long recordNumber;

    /**
     * Create a Hibernate transaction listener.
     *
     * @param session the Hibernate session
     */
    public HibernateTransactionPipelineListener(final Session session) {
        checkNotNull(session, "session");
        this.session = session;
    }

    @Override
    public Object beforeRecordProcessing(final Object record) {
        transaction = session.getTransaction();
        transaction.begin();
        recordNumber++;
        return record;
    }

    @Override
    public void afterRecordProcessing(final Object record, final Object processingResult) {
        try {
            session.flush();
            session.clear();
            transaction.commit();
            LOGGER.info("Transaction committed after record " + recordNumber);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to commit transaction after record " + recordNumber, e);
        }
    }

    @Override
    public void onRecordProcessingException(final Object record, final Throwable throwable) {
        try {
            transaction.rollback();
            LOGGER.info("Transaction rolled back after record " + recordNumber);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to rollback transaction after record " + recordNumber, e);
        }
    }

}
