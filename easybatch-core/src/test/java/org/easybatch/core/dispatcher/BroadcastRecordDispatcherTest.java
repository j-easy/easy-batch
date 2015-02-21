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

import org.easybatch.core.api.Header;
import org.easybatch.core.api.Record;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link org.easybatch.core.dispatcher.BroadcastRecordDispatcher}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class BroadcastRecordDispatcherTest {

    private BroadcastRecordDispatcher broadcastRecordDispatcher;

    private BlockingQueue<Record> queue1, queue2;

    @Before
    public void setUp() throws Exception {
        queue1 = new LinkedBlockingQueue<Record>();
        queue2 = new LinkedBlockingQueue<Record>();
        broadcastRecordDispatcher = new BroadcastRecordDispatcher(Arrays.asList(queue1, queue2));
    }

    @Test
    public void testBroadcastRecord() throws Exception {

        Header header = new Header(1l, "In-Memory String", new Date());
        String payload = "test record";
        StringRecord record = new StringRecord(header, payload);

        broadcastRecordDispatcher.dispatchRecord(record);

        assertThat(queue1).isNotEmpty().containsExactly(record);
        assertThat(queue2).isNotEmpty().containsExactly(record);

        assertThat(queue1.peek()).isNotNull().isInstanceOf(StringRecord.class);
        Record record1 = queue1.poll();
        assertThat(record1.getHeader().getNumber()).isEqualTo(1l);
        assertThat(record1.getPayload()).isEqualTo(payload);

        assertThat(queue2.peek()).isNotNull().isInstanceOf(StringRecord.class);
        Record record2 = queue2.poll();
        assertThat(record2.getHeader().getNumber()).isEqualTo(1l);
        assertThat(record2.getPayload()).isEqualTo(payload);
    }

}
