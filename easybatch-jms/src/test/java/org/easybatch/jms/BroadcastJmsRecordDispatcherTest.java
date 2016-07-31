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

import org.easybatch.core.record.Header;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.Message;
import javax.jms.QueueSender;
import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BroadcastJmsRecordDispatcherTest {

    private BroadcastJmsRecordDispatcher broadcastJmsRecordDispatcher;

    @Mock
    private QueueSender queue1, queue2;

    @Mock
    private JmsRecord jmsRecord;
    @Mock
    private Header header;
    @Mock
    private Message message;

    @Before
    public void setUp() throws Exception {
        when(jmsRecord.getHeader()).thenReturn(header);
        when(jmsRecord.getPayload()).thenReturn(message);

        broadcastJmsRecordDispatcher = new BroadcastJmsRecordDispatcher(Arrays.asList(queue1, queue2));
    }

    @Test
    public void testBroadcastJmsRecord() throws Exception {

        broadcastJmsRecordDispatcher.dispatchRecord(jmsRecord);

        verify(queue1).send(message);
        verify(queue2).send(message);
    }

}
