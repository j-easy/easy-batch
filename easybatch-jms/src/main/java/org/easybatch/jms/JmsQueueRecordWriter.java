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

import org.easybatch.core.writer.AbstractRecordWriter;
import org.easybatch.core.writer.RecordWritingException;

import javax.jms.*;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Sends a Jms message to a given queue.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JmsQueueRecordWriter extends AbstractRecordWriter<Message> {

    private QueueSender queueSender;

    /**
     * Create a Jms queue record writer.
     *
     * @param queueConnectionFactory the factory to use to create connections.
     * @param queue                  the target queue
     * @throws JMSException if an exception occurs while sending the Jms message
     */
    public JmsQueueRecordWriter(final QueueConnectionFactory queueConnectionFactory, final Queue queue) throws JMSException {
        checkNotNull(queueConnectionFactory, "queue connection factory");
        checkNotNull(queue, "queue");
        QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
        QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queueSender = queueSession.createSender(queue);
    }

    @Override
    public void writeRecord(final Message message) throws RecordWritingException {
        try {
            queueSender.send(message);
        } catch (JMSException e) {
            throw new RecordWritingException("Unable to send message " + message + " to queue", e);
        }
    }

}
