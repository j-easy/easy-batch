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

import org.apache.activemq.broker.BrokerService;
import org.apache.commons.io.FileUtils;
import org.easybatch.core.api.Engine;
import org.easybatch.core.api.Header;
import org.easybatch.core.api.Report;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.core.processor.RecordCollector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for JMS support.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JmsIntegrationTest {

    public static final String EXPECTED_DATA_SOURCE_NAME = "JMS queue: q";
    public static final String MESSAGE_TEXT = "test";

    private BrokerService broker;

    @Before
    public void setUp() throws Exception {
        broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.start();
    }

    @Test
    public void testJmsSupport() throws Exception {
        Context jndiContext = getJndiContext();
        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
        Queue queue = (Queue) jndiContext.lookup("q");

        QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
        QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSender queueSender = queueSession.createSender(queue);
        queueConnection.start();

        //send a regular message to the queue
        TextMessage message = queueSession.createTextMessage();
        message.setText(MESSAGE_TEXT);
        queueSender.send(message);

        //send a poison record to the queue
        queueSender.send(new JmsPoisonMessage());

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new JmsRecordReader(queueConnectionFactory, queue))
                .filter(new JmsPoisonRecordFilter())
                .processor(new RecordCollector<JmsRecord>())
                .build();

        Report report = engine.call();

        assertThat(report).isNotNull();
        assertThat(report.getDataSource()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);
        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getFilteredRecordsCount()).isEqualTo(1);

        List<JmsRecord> records = (List<JmsRecord>) report.getBatchResult();

        assertThat(records).isNotNull().isNotEmpty().hasSize(1);

        JmsRecord jmsRecord = records.get(0);
        Header header = jmsRecord.getHeader();
        assertThat(header).isNotNull();
        assertThat(header.getNumber()).isEqualTo(1);
        assertThat(header.getSource()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);

        Message payload = jmsRecord.getPayload();
        assertThat(payload).isNotNull().isInstanceOf(TextMessage.class);

        TextMessage textMessage = (TextMessage) payload;
        assertThat(textMessage.getText()).isNotNull().isEqualTo(MESSAGE_TEXT);
    }

    @After
    public void tearDown() throws Exception {
        File brokerDataDirectoryFile = broker.getDataDirectoryFile();
        broker.stop();
        FileUtils.deleteDirectory(brokerDataDirectoryFile);
    }

    private Context getJndiContext() throws IOException, NamingException {
        Properties properties = new Properties();
        properties.load(JmsIntegrationTest.class.getResourceAsStream(("/jndi.properties")));
        return new InitialContext(properties);
    }
}
