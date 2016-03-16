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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.QueueSender;
import java.util.Arrays;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RoundRobinJmsRecordDispatcherTest {

    private RoundRobinJmsRecordDispatcher roundRobinJmsRecordDispatcher;

    @Mock
    private QueueSender queue1, queue2;

    @Mock
    private JmsRecord record1, record2, record3;

    @Mock
    private JmsPoisonRecord poisonRecord;

    @Before
    public void setUp() throws Exception {
        roundRobinJmsRecordDispatcher = new RoundRobinJmsRecordDispatcher(Arrays.asList(queue1, queue2));
    }

    @Test
    public void regularRecordsShouldBeDispatchedToQueuesInRoundRobinFashion() throws Exception {

        roundRobinJmsRecordDispatcher.dispatchRecord(record1);
        roundRobinJmsRecordDispatcher.dispatchRecord(record2);
        roundRobinJmsRecordDispatcher.dispatchRecord(record3);

        InOrder inOrder = Mockito.inOrder(queue1, queue2, record1, record2, record3);
        inOrder.verify(queue1).send(record1.getPayload());
        inOrder.verify(queue2).send(record2.getPayload());
        inOrder.verify(queue1).send(record3.getPayload());
    }

    @Test
    public void poisonRecordsShouldBeBroadcastToAllQueues() throws Exception {

        roundRobinJmsRecordDispatcher.dispatchRecord(poisonRecord);

        verify(queue1).send(poisonRecord.getPayload());
        verify(queue2).send(poisonRecord.getPayload());
    }

}
