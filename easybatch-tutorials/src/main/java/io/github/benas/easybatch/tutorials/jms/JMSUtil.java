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

    private static QueueConnection queueConnection;

    private static BrokerService broker;

    public static void initJMSFactory() throws Exception {

        Properties p = new Properties();
        p.load(JMSUtil.class.getResourceAsStream(("/jndi.properties")));
        Context jndiContext = new InitialContext(p);

        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
        Queue queue = (Queue) jndiContext.lookup("q");

        queueConnection = queueConnectionFactory.createQueueConnection();
        queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queueSender = queueSession.createSender(queue);
        queueConnection.start();

    }

    public static void startBroker() throws Exception {
        broker = new BrokerService();
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

    public static void closeJMSSession() throws JMSException {
        queueConnection.close();
        queueSender.close();
        queueSession.close();
    }

    public static void stopBroker() throws Exception {
        queueConnection.close();
        queueSender.close();
        queueSession.close();
        broker.stop();
    }

}
