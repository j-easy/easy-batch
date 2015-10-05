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

import org.easybatch.core.api.event.JobEventListener;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Listener that commits a transaction at the end of the job.
 * <p/>
 * This listener should be used in conjunction with a {@link HibernateTransactionPipelineListener} to commit last records.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class HibernateTransactionJobListener implements JobEventListener {

    private static final Logger LOGGER = Logger.getLogger(HibernateTransactionJobListener.class.getSimpleName());

    private Session session;

    private boolean closeSession;

    /**
     * Create a Hibernate transaction listener. The session will <strong>not</strong> be closed at the end of the job.
     *
     * @param session the Hibernate session
     */
    public HibernateTransactionJobListener(final Session session) {
        this(session, false);
    }

    /**
     * Create a Hibernate transaction listener.
     *
     * @param session      the Hibernate session
     * @param closeSession True if the session should be closed at the end of the job
     */
    public HibernateTransactionJobListener(final Session session, final boolean closeSession) {
        checkNotNull(session, "session");
        this.session = session;
        this.closeSession = closeSession;
    }

    @Override
    public void beforeJobStart() {
        // No op
    }

    @Override
    public void afterJobEnd() {
        try {
            Transaction transaction = session.getTransaction();
            if (transaction != null && transaction.isActive()) {
                LOGGER.info("Committing transaction after job end");
                session.flush();
                session.clear();
                transaction.commit();
            }
            if (session != null && closeSession) {
                LOGGER.info("Closing session after job end");
                session.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to commit transaction after job end", e);
        }
    }
}
