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
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test class for {@link org.easybatch.core.dispatcher.RoundRobinRecordDispatcher}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class RoundRobinRecordDispatcherTest {

    private RoundRobinRecordDispatcher roundRobinRecordDispatcher;

    private BlockingQueue<Record> queue1, queue2;

    @Before
    public void setUp() throws Exception {
        queue1 = new ArrayBlockingQueue<Record>(5);
        queue2 = new ArrayBlockingQueue<Record>(5);
        roundRobinRecordDispatcher = new RoundRobinRecordDispatcher(Arrays.asList(queue1, queue2));
    }

    @Test
    public void regularRecordsShouldBeDispatchedToQueuesInRoundRobinFashion() throws Exception {

        StringRecord record1 = new StringRecord(1, "test record1");
        StringRecord record2 = new StringRecord(2, "test record2");
        roundRobinRecordDispatcher.dispatchRecord(record1);
        roundRobinRecordDispatcher.dispatchRecord(record2);

        assertThat(queue1).isNotEmpty();
        assertThat(queue1).containsOnly(record1);
        assertThat(queue1.peek()).isNotNull();
        assertThat(queue1.peek()).isInstanceOf(StringRecord.class);
        Record r1 = queue1.poll();
        assertThat(r1.getNumber()).isEqualTo(1);
        assertThat(r1.getPayload()).isEqualTo("test record1");

        assertThat(queue2).isNotEmpty();
        assertThat(queue2).containsOnly(record2);
        assertThat(queue2.peek()).isNotNull();
        assertThat(queue2.peek()).isInstanceOf(StringRecord.class);
        Record r2 = queue2.poll();
        assertThat(r2.getNumber()).isEqualTo(2);
        assertThat(r2.getPayload()).isEqualTo("test record2");

        StringRecord record3 = new StringRecord(3, "test record3");
        roundRobinRecordDispatcher.dispatchRecord(record3);

        assertThat(queue1).isNotEmpty();
        assertThat(queue1.peek()).isNotNull();
        assertThat(queue1.peek()).isInstanceOf(StringRecord.class);
        Record r3 = queue1.poll();
        assertThat(r3.getNumber()).isEqualTo(3);
        assertThat(r3.getPayload()).isEqualTo("test record3");

        assertThat(queue2).isEmpty();

    }

    @Test
    public void poisonRecordsShouldBeBroadcastToAllQueues() throws Exception {

        PoisonRecord poisonRecord = new PoisonRecord();
        roundRobinRecordDispatcher.dispatchRecord(poisonRecord);

        assertThat(queue1).isNotEmpty();
        assertThat(queue1).containsOnly(poisonRecord);
        assertThat(queue1.peek()).isNotNull();
        assertThat(queue1.peek()).isInstanceOf(PoisonRecord.class);

        assertThat(queue2).isNotEmpty();
        assertThat(queue2).containsOnly(poisonRecord);
        assertThat(queue2.peek()).isNotNull();
        assertThat(queue2.peek()).isInstanceOf(PoisonRecord.class);

    }

}
