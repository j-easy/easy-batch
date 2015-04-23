/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.integration.jms;

import org.easybatch.core.api.Header;
import org.easybatch.core.api.RecordReader;

import javax.jms.*;
import java.util.Date;

/**
 * A record reader that reads records from a JMS queue.
 * <p/>
 * This reader produces {@link JmsRecord} instances of type {@link javax.jms.Message}.
 * <p/>
 * It will stop reading records when a {@link JmsPoisonMessage} is sent to the queue.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JmsRecordReader implements RecordReader {

    private long currentRecordNumber;

    private QueueConnectionFactory queueConnectionFactory;

    private QueueConnection queueConnection;

    private QueueSession queueSession;

    private QueueReceiver queueReceiver;

    private Queue queue;

    private boolean stop;

    /**
     * Create a Jms queue record reader.
     *
     * @param queueConnectionFactory the queue connection factory
     * @param queue                  the jms queue to read records from
     */
    public JmsRecordReader(final QueueConnectionFactory queueConnectionFactory, final Queue queue) {
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

    @Override
    public boolean hasNextRecord() {
        return !stop;
    }

    @Override
    public JmsRecord readNextRecord() throws Exception {
        Message message = queueReceiver.receive();
        String type = message.getStringProperty("type");
        stop = message instanceof JmsPoisonMessage || (type != null && type.equals("poison"));
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new JmsRecord(header, message);
    }

    @Override
    public Long getTotalRecords() {
        //undefined, cannot be calculated upfront
        return null;
    }

    @Override
    public String getDataSourceName() {
        try {
            return "JMS queue: " + queue.getQueueName();
        } catch (JMSException e) {
            throw new RuntimeException("Unable to get jms queue name", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (queueConnection != null) {
            queueConnection.close();
        }
        if (queueSession != null) {
            queueSession.close();
        }
        if (queueReceiver != null) {
            queueReceiver.close();
        }
    }

}
