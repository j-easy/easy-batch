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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test class for {@link org.easybatch.core.dispatcher.ContentBasedRecordDispatcher}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ContentBasedRecordDispatcherTest {

    private ContentBasedRecordDispatcher recordDispatcher;

    private BlockingQueue<Record> orangeQueue;

    private BlockingQueue<Record> defaultQueue;

    @Before
    public void setUp() throws Exception {
        orangeQueue = new LinkedBlockingQueue<Record>();
        defaultQueue = new LinkedBlockingQueue<Record>();
        recordDispatcher =
                new ContentBasedRecordDispatcherBuilder()
                    .when(new OrangePredicate()).dispatchTo(orangeQueue)
                    .otherwise(defaultQueue)
                    .build();

    }

    @Test
    public void orangeRecordShouldBeDispatchedToOrangeQueue() throws Exception {
        StringRecord orangeRecord = new StringRecord(1, "orange record");
        recordDispatcher.dispatchRecord(orangeRecord);
        assertThat(orangeQueue).isNotEmpty();
        assertThat(orangeQueue).contains(orangeRecord);
        assertThat(defaultQueue).isEmpty();
    }

    @Test
    public void nonOrangeRecordShouldBeDispatchedToDefaultQueue() throws Exception {
        StringRecord appleRecord = new StringRecord(2, "apple record");
        recordDispatcher.dispatchRecord(appleRecord);
        assertThat(defaultQueue).isNotEmpty();
        assertThat(defaultQueue).contains(appleRecord);
        assertThat(orangeQueue).isEmpty();
    }

    @Test
    public void poisonRecordShouldBeDispatchedToAllQueues() throws Exception {
        PoisonRecord poisonRecord = new PoisonRecord();
        recordDispatcher.dispatchRecord(poisonRecord);
        assertThat(defaultQueue).isNotEmpty();
        assertThat(defaultQueue).contains(poisonRecord);
        assertThat(orangeQueue).isNotEmpty();
        assertThat(orangeQueue).contains(poisonRecord);
    }


    // predicate that matches records containing "orange", used for tests
    private class OrangePredicate implements Predicate {

        @Override
        public boolean matches(Record record) {
            StringRecord stringRecord = (StringRecord) record;
            return stringRecord.getPayload().contains("orange");
        }

    }

}
