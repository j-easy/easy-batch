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

package org.easybatch.jpa;

import org.easybatch.core.api.event.JobEventListener;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Listener that commits a transaction at the end of the job.
 * <p/>
 * This listener should be used in conjunction with a {@link JpaTransactionPipelineListener} to commit last records.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JpaTransactionJobListener implements JobEventListener {

    private static final Logger LOGGER = Logger.getLogger(JpaTransactionJobListener.class.getSimpleName());

    private EntityManager entityManager;

    private boolean closeEntityManager;

    /**
     * Create a JPA transaction listener. The entity manager will <strong>not</strong> be closed at the end of the job.
     *
     * @param entityManager the JPA entity manager
     */
    public JpaTransactionJobListener(final EntityManager entityManager) {
        this(entityManager, false);
    }

    /**
     * Create a JPA transaction listener.
     *
     * @param entityManager      the JPA entity manager
     * @param closeEntityManager True if the entity manager should be closed at the end of the job
     */
    public JpaTransactionJobListener(final EntityManager entityManager, final boolean closeEntityManager) {
        checkNotNull(entityManager, "entity manager");
        this.entityManager = entityManager;
        this.closeEntityManager = closeEntityManager;
    }

    @Override
    public void beforeJobStart() {
        // No op
    }

    @Override
    public void afterJobEnd() {
        try {
            EntityTransaction transaction = entityManager.getTransaction();
            if (transaction != null && transaction.isActive()) {
                LOGGER.info("Committing transaction after job end");
                entityManager.flush();
                entityManager.clear();
                transaction.commit();
            }
            if (entityManager != null && closeEntityManager) {
                LOGGER.info("Closing entity manager after job end");
                entityManager.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to commit transaction after job end", e);
        }
    }
}
