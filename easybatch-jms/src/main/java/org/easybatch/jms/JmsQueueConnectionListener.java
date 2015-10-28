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

package org.easybatch.jms;

import static org.easybatch.core.util.Utils.checkNotNull;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.listener.JobListener;

/**
 * Listener that closes a JMS queue connection at the end of the job.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JmsQueueConnectionListener implements JobListener {

    private static final Logger LOGGER = Logger.getLogger(JmsQueueConnectionListener.class.getSimpleName());

    private QueueConnection connection;

    /**
     * Create a JMS queue connection listener.
     *
     * @param connection the JMS queue connection
     */
    public JmsQueueConnectionListener(final QueueConnection connection) {
        checkNotNull(connection, "connection");
        this.connection = connection;
    }

    @Override
    public void beforeJobStart(final JobParameters jobParameters) {
        // No op
    }

    @Override
    public void afterJobEnd(final JobReport jobReport) {
        try {
            if (connection != null) {
                LOGGER.info("Closing JMS queue connection after job end");
                connection.close();
            }
        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, "Unable to close JMS queue connection after job end", e);
        }
    }
}
