package org.jeasy.batch.tutorials.advanced.jms;

import javax.jms.Message;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.jeasy.batch.core.processor.RecordProcessor;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.jms.JmsMessageTransformer;
import org.jeasy.batch.jms.JmsRecord;

/**
 * {@link JmsMessageTransformer} is agnostic of any JMS implementation and requires
 * a JMS session to create messages. However, this session should be opened/closed by the client..
 *
 * Since we know which implementation we use in this tutorial (ActiveMQ), this processor
 * creates JMS records with {@link ActiveMQTextMessage}s as payload as those messages can
 * be created without requiring a JMS session.
 */
public class JmsRecordTransformer implements RecordProcessor<String, Message> {

    @Override
    public JmsRecord processRecord(Record<String> record) throws Exception {
        ActiveMQTextMessage activeMQTextMessage = new ActiveMQTextMessage();
        activeMQTextMessage.setText(record.getPayload());
        return new JmsRecord(record.getHeader(), activeMQTextMessage);
    }

}
