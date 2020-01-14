/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.Date;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * A record reader that reads records from a JMS queue.
 *
 * This reader produces {@link JmsRecord} instances of type {@link javax.jms.Message}.
 *
 * It will stop reading records when a {@link JmsPoisonMessage} is sent to the queue.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JmsQueueRecordReader implements RecordReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsQueueRecordReader.class.getSimpleName());

    private long currentRecordNumber;
    private QueueConnectionFactory queueConnectionFactory;
    private QueueConnection queueConnection;
    private QueueSession queueSession;
    private QueueReceiver queueReceiver;
    private Queue queue;
    private boolean stop;

    /**
     * Create a new {@link JmsQueueRecordReader}.
     *
     * @param queueConnectionFactory to use to create connections
     * @param queue                  to read records from
     */
    public JmsQueueRecordReader(final QueueConnectionFactory queueConnectionFactory, final Queue queue) {
        checkNotNull(queueConnectionFactory, "queue connection factory");
        checkNotNull(queue, "queue");
        this.queueConnectionFactory = queueConnectionFactory;
        this.queue = queue;
    }

    @Override
    public void open() throws Exception {
        queueConnection = queueConnectionFactory.createQueueConnection();
        queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queueReceiver = queueSession.createReceiver(queue);
        queueConnection.start();
    }

    private boolean hasNextRecord() {
        return !stop;
    }

    @Override
    public JmsRecord readRecord() throws Exception {
        if (hasNextRecord()) {
            Message message = queueReceiver.receive();
            String type = message.getJMSType();
            stop = message instanceof JmsPoisonMessage || (type != null && JmsPoisonMessage.TYPE.equals(type));
            Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
            return new JmsRecord(header, message);
        } else {
            return null;
        }
    }

    private String getDataSourceName() {
        try {
            return "JMS queue: " + queue.getQueueName();
        } catch (JMSException e) {
            LOGGER.error("Unable to get jms queue name", e);
            return "N/A";
        }
    }

    @Override
    public void close() throws Exception {
        if (queueReceiver != null) {
            queueReceiver.close();
        }
        if (queueSession != null) {
            queueSession.close();
        }
        if (queueConnection != null) {
            queueConnection.close();
        }
    }

}
