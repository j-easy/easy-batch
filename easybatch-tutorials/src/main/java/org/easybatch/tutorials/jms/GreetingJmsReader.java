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

package org.easybatch.tutorials.jms;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordReader;
import org.easybatch.core.util.StringRecord;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

/**
 * Reader that reads greetings from a JMS queue.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class GreetingJmsReader implements RecordReader {

    private int id;

    private int currentRecordNumber;

    QueueConnection queueConnection;

    QueueSession queueSession;

    QueueReceiver queueReceiver;

    Queue queue;

    private boolean stop;

    public GreetingJmsReader(int id) {
        this.id = id;
    }

    @Override
    public void open() throws Exception {
        Properties p = new Properties();
        p.load(GreetingJmsReader.class.getResourceAsStream(("/jndi.properties")));

        Context jndiContext = new InitialContext(p);
        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
        queue = (Queue) jndiContext.lookup("q");

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
    public Record readNextRecord() throws Exception {
        String record;
        Message m = queueReceiver.receive();
        TextMessage message = (TextMessage) m;
        record = message.getText();
        stop = record.equalsIgnoreCase("quit");
        System.out.println("Greeting Reader " + id + " : received JMS message: " + record);
        return new StringRecord(++currentRecordNumber, record);
    }

    @Override
    public Integer getTotalRecords() {
        //undefined, cannot be calculated in advance
        return null;
    }

    @Override
    public String getDataSourceName() {
        try {
            return "JMS queue: " + queue.getQueueName();
        } catch (JMSException e) {
            throw new RuntimeException("Unable to get queue name", e);
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
