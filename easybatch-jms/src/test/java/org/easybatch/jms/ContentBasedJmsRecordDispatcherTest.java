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

import org.easybatch.core.dispatcher.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.Message;
import javax.jms.QueueSender;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ContentBasedJmsRecordDispatcherTest {

    private ContentBasedJmsRecordDispatcher recordDispatcher;

    @Mock
    private QueueSender orangeQueue, defaultQueue;

    @Mock
    private JmsRecord orangeRecord, appleRecord;

    @Mock
    private JmsPoisonRecord poisonRecord;

    @Mock
    private Predicate<JmsRecord> orangePredicate;

    @Before
    public void setUp() throws Exception {
        recordDispatcher = new ContentBasedJmsRecordDispatcherBuilder()
                .when(orangePredicate).dispatchTo(orangeQueue)
                .otherwise(defaultQueue)
                .build();

        when(orangePredicate.matches(orangeRecord)).thenReturn(true);
        when(orangePredicate.matches(appleRecord)).thenReturn(false);
    }

    @Test
    public void orangeRecordShouldBeDispatchedToOrangeQueue() throws Exception {
        recordDispatcher.dispatchRecord(orangeRecord);
        verify(orangeQueue).send(orangeRecord.getPayload());
        verifyZeroInteractions(defaultQueue);
    }

    @Test
    public void nonOrangeRecordShouldBeDispatchedToDefaultQueue() throws Exception {
        recordDispatcher.dispatchRecord(appleRecord);
        verify(defaultQueue).send(appleRecord.getPayload());
        verifyZeroInteractions(orangeQueue);
    }

    @Test
    public void poisonRecordShouldBeDispatchedToAllQueues() throws Exception {
        recordDispatcher.dispatchRecord(poisonRecord);
        verify(defaultQueue).send(poisonRecord.getPayload());
        verify(orangeQueue).send(poisonRecord.getPayload());
    }

}
