/*
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
package org.jeasy.batch.jms;

import org.jeasy.batch.core.record.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.jms.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JmsRecordReaderTest {

    private JmsRecordReader jmsRecordReader;
    private long timout = 10000;

    @Mock
    private ConnectionFactory connectionFactory;
    @Mock
    private Destination destination;
    @Mock
    private Connection connection;
    @Mock
    private Session session;
    @Mock
    private MessageConsumer messageConsumer;
    @Mock
    private Message message;

    @Before
    public void setUp() throws Exception {
        jmsRecordReader = new JmsRecordReader(connectionFactory, destination, timout);

        when(connectionFactory.createConnection()).thenReturn(connection);
        when(connection.createSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
        when(session.createConsumer(destination)).thenReturn(messageConsumer);
        when(messageConsumer.receive(timout)).thenReturn(message);
    }

    @Test
    public void testOpen() throws Exception {
        jmsRecordReader.open();

        verify(connectionFactory).createConnection();
        verify(connection).createSession(false, Session.AUTO_ACKNOWLEDGE);
        verify(session).createConsumer(destination);
        verify(connection).start();
    }

    @Test
    public void testReadNextRecord() throws Exception {
        jmsRecordReader.open();

        Record<Message> record = jmsRecordReader.readRecord();

        verify(messageConsumer).receive(timout);
        assertThat(record).isNotNull().isInstanceOf(JmsRecord.class);
        assertThat(record.getPayload()).isEqualTo(message);
    }

    @After
    public void tearDown() throws Exception {
        jmsRecordReader.close();
    }
}
