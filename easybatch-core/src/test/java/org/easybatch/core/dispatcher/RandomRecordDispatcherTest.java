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
 * Test class for {@link org.easybatch.core.dispatcher.RandomRecordDispatcher}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class RandomRecordDispatcherTest {

    private RandomRecordDispatcher randomRecordDispatcher;

    private BlockingQueue<Record> queue1, queue2;

    @Before
    public void setUp() throws Exception {
        queue1 = new ArrayBlockingQueue<Record>(5);
        queue2 = new ArrayBlockingQueue<Record>(5);
        randomRecordDispatcher = new RandomRecordDispatcher(Arrays.asList(queue1, queue2));
    }

    @Test
    public void regularRecordsShouldBeDispatchedRandomlyToOneOfTheQueues() throws Exception {

        StringRecord record = new StringRecord(1, "test record");
        randomRecordDispatcher.dispatchRecord(record);

        if(queue1.isEmpty()) {
            assertThat(queue2).isNotEmpty();
            assertThat(queue2).containsOnly(record);
            assertThat(queue2.peek()).isNotNull();
            assertThat(queue2.peek()).isInstanceOf(StringRecord.class);
            Record record2 = queue2.poll();
            assertThat(record2.getNumber()).isEqualTo(1);
            assertThat(record2.getPayload()).isEqualTo("test record");
        }
        if(!queue1.isEmpty()) {
            assertThat(queue2).isEmpty();
            assertThat(queue2.peek()).isNull();

            assertThat(queue1).containsOnly(record);
            assertThat(queue1.peek()).isNotNull();
            assertThat(queue1.peek()).isInstanceOf(StringRecord.class);
            Record record1 = queue1.poll();
            assertThat(record1.getNumber()).isEqualTo(1);
            assertThat(record1.getPayload()).isEqualTo("test record");
        }

    }

    @Test
    public void poisonRecordsShouldBeBroadcastToAllQueues() throws Exception {

        PoisonRecord poisonRecord = new PoisonRecord();
        randomRecordDispatcher.dispatchRecord(poisonRecord);

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
