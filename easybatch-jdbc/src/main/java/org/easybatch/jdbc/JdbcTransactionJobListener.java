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

package org.easybatch.jdbc;

import org.easybatch.core.api.event.job.JobEventListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listener that commits a transaction at the end of the job.
 * <p/>
 * This is useful to commit last records when using a {@link JdbcTransactionStepListener} with a commit-interval.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JdbcTransactionJobListener implements JobEventListener {

    private static final Logger LOGGER = Logger.getLogger(JdbcTransactionJobListener.class.getSimpleName());

    private Connection connection;

    private boolean closeConnection;

    /**
     * Create a JDBC transaction listener. The connection will <strong>not</strong> be closed at the end of the job.
     *
     * @param connection the JDBC connection (should be in auto-commit = false)
     */
    public JdbcTransactionJobListener(final Connection connection) {
        this(connection, false);
    }

    /**
     * Create a JDBC transaction listener.
     *
     * @param connection      the JDBC connection (should be in auto-commit = false)
     * @param closeConnection True if the connection should be closed at the end of the job
     */
    public JdbcTransactionJobListener(final Connection connection, final boolean closeConnection) {
        this.connection = connection;
        this.closeConnection = closeConnection;
    }

    @Override
    public void beforeJobStart() {
        // No op
    }

    @Override
    public void afterJobEnd() {
        try {
            LOGGER.info("Committing transaction after job end");
            connection.commit();
            if (closeConnection) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Unable to commit transaction", e);
        }
    }

    @Override
    public void onJobException(final Throwable throwable) {
        // No op
    }
}
