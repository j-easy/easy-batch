/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.dispatcher;

import org.easybatch.core.api.Record;
import org.easybatch.core.record.PoisonRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link org.easybatch.core.dispatcher.RoundRobinRecordDispatcher}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class RoundRobinRecordDispatcherTest {

    private RoundRobinRecordDispatcher roundRobinRecordDispatcher;

    private BlockingQueue<Record> queue1, queue2;

    @Mock
    private Record record1, record2, record3;

    @Mock
    private PoisonRecord poisonRecord;

    @Before
    public void setUp() throws Exception {
        queue1 = new LinkedBlockingQueue<Record>();
        queue2 = new LinkedBlockingQueue<Record>();
        roundRobinRecordDispatcher = new RoundRobinRecordDispatcher(Arrays.asList(queue1, queue2));
    }

    @Test
    public void regularRecordsShouldBeDispatchedToQueuesInRoundRobinFashion() throws Exception {

        roundRobinRecordDispatcher.dispatchRecord(record1);
        roundRobinRecordDispatcher.dispatchRecord(record2);
        roundRobinRecordDispatcher.dispatchRecord(record3);

        assertThat(queue1).isNotEmpty().containsExactly(record1, record3);
        assertThat(queue2).isNotEmpty().containsOnly(record2);
    }

    @Test
    public void poisonRecordsShouldBeBroadcastToAllQueues() throws Exception {

        roundRobinRecordDispatcher.dispatchRecord(poisonRecord);

        assertThat(queue1).isNotEmpty().containsOnly(poisonRecord);
        assertThat(queue2).isNotEmpty().containsOnly(poisonRecord);

    }

}
