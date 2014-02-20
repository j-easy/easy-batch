package io.github.benas.easybatch.tutorials.jms;

import io.github.benas.easybatch.core.api.Record;
import io.github.benas.easybatch.core.api.RecordReader;
import io.github.benas.easybatch.core.util.StringRecord;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

/**
 * Reader that reads greetings from a JMS queue.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class GreetingJmsReader implements RecordReader {

    private int currentRecordNumber;

    QueueConnection queueConnection;

    QueueSession queueSession;

    QueueReceiver queueReceiver;

    Queue queue;

    private boolean stop;

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
        if (record.equalsIgnoreCase("quit")){
            stop = true;
        }
        System.out.println("Greeting Reader : received JMS message: " + record);
        return new StringRecord(++currentRecordNumber, record);
    }

    @Override
    public Long getTotalRecords() {
        //undefined
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
