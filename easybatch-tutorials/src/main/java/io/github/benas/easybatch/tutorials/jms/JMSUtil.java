package io.github.benas.easybatch.tutorials.jms;

import org.apache.activemq.broker.BrokerService;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

/**
 * JMS Utilities class.
 */
public class JMSUtil {

    private static QueueSender queueSender;

    private static QueueSession queueSession;

    public static void initJMSFactory() throws Exception {

        Properties p = new Properties();
        p.load(JMSUtil.class.getResourceAsStream(("/jndi.properties")));
        Context jndiContext = new InitialContext(p);

        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
        Queue queue = (Queue) jndiContext.lookup("q");

        QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
        queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queueSender = queueSession.createSender(queue);
        queueConnection.start();

    }

    public static void startBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.start();
        initJMSFactory();
    }

    public static void sendJmsMessage(String jmsMessage) throws JMSException {
        TextMessage message = queueSession.createTextMessage();
        message.setText(jmsMessage);
        queueSender.send(message);
        System.out.println("Message '" + jmsMessage + "' sent to JMS queue");
    }

}
