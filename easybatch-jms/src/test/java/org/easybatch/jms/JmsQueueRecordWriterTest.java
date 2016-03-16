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

import org.easybatch.core.processor.RecordProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JmsQueueRecordWriterTest {

    @Mock
    private QueueConnectionFactory queueConnectionFactory;
    @Mock
    private QueueConnection queueConnection;
    @Mock
    private QueueSession queueSession;
    @Mock
    private Queue queue;
    @Mock
    private QueueSender queueSender;
    @Mock
    private Message message;
    @Mock
    private JmsRecord record;
    @Mock
    private JMSException jmsException;

    private JmsQueueRecordWriter jmsQueueRecordWriter;

    @Before
    public void setUp() throws Exception {
        when(queueConnectionFactory.createQueueConnection()).thenReturn(queueConnection);
        when(queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(queueSession);
        when(queueSession.createSender(queue)).thenReturn(queueSender);

        jmsQueueRecordWriter = new JmsQueueRecordWriter(queueConnectionFactory, queue);
    }

    @Test
    public void testProcessRecord() throws Exception {
        jmsQueueRecordWriter.writePayload(message);

        verify(queueSender).send(message);
    }

    @Test(expected = RecordProcessingException.class)
    public void testRecordProcessingWithError() throws Exception {
        Mockito.when(record.getPayload()).thenReturn(message);
        doThrow(jmsException).when(queueSender).send(message);

        jmsQueueRecordWriter.processRecord(record);
    }
}
