/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import org.easybatch.core.record.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmsQueueRecordReaderTest {

    public static final String EXPECTED_DATA_SOURCE_NAME = "JMS queue: queue";

    private JmsQueueRecordReader jmsQueueRecordReader;

    @Mock
    private QueueConnectionFactory queueConnectionFactory;
    @Mock
    private Queue queue;
    @Mock
    private QueueConnection queueConnection;
    @Mock
    private QueueSession queueSession;
    @Mock
    private QueueReceiver queueReceiver;
    @Mock
    private Message message;

    @Before
    public void setUp() throws Exception {
        jmsQueueRecordReader = new JmsQueueRecordReader(queueConnectionFactory, queue);

        when(queue.getQueueName()).thenReturn("queue");
        when(queueConnectionFactory.createQueueConnection()).thenReturn(queueConnection);
        when(queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(queueSession);
        when(queueSession.createReceiver(queue)).thenReturn(queueReceiver);
        when(queueReceiver.receive()).thenReturn(message);
    }

    @Test
    public void testOpen() throws Exception {
        jmsQueueRecordReader.open();

        verify(queueConnectionFactory).createQueueConnection();
        verify(queueConnection).createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        verify(queueSession).createReceiver(queue);
        verify(queueConnection).start();
    }

    @Test
    public void testReadNextRecord() throws Exception {
        jmsQueueRecordReader.open();

        Record record = jmsQueueRecordReader.readNextRecord();

        verify(queueReceiver).receive();
        assertThat(record).isNotNull().isInstanceOf(JmsRecord.class);
        assertThat(record.getPayload()).isEqualTo(message);
    }

    @Test
    public void testGetTotalRecords() throws Exception {
        assertThat(jmsQueueRecordReader.getTotalRecords()).isNull();
    }

    @Test
    public void testGetDataSourceName() throws Exception {
        assertThat(jmsQueueRecordReader.getDataSourceName()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        jmsQueueRecordReader.close();
    }
}
