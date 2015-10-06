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

import org.easybatch.core.api.event.JobEventListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Listener that closes a JDBC connection at the end of the job.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JdbcConnectionJobListener implements JobEventListener {

    private static final Logger LOGGER = Logger.getLogger(JdbcConnectionJobListener.class.getSimpleName());

    private Connection connection;

    /**
     * Create a JDBC connection listener.
     *
     * @param connection      the JDBC connection.
     */
    public JdbcConnectionJobListener(final Connection connection) {
        checkNotNull(connection, "connection");
        this.connection = connection;
    }

    @Override
    public void beforeJobStart() {
        // No op
    }

    @Override
    public void afterJobEnd() {
        try {
            if (connection != null && !connection.isClosed()) {
                LOGGER.info("Closing connection after job end");
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Unable to close connection after job end", e);
        }
    }

}
