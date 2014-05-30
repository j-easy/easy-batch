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
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
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
