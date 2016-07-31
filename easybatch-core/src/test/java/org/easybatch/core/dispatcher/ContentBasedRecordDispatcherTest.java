/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import org.easybatch.core.record.PoisonRecord;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentBasedRecordDispatcherTest {

    private ContentBasedRecordDispatcher<Record> recordDispatcher;

    private BlockingQueue<Record> orangeQueue;

    private BlockingQueue<Record> defaultQueue;

    @Mock
    private Record orangeRecord, appleRecord;

    @Mock
    private PoisonRecord poisonRecord;

    @Mock
    private Predicate<Record> orangePredicate;

    @Before
    public void setUp() throws Exception {
        orangeQueue = new LinkedBlockingQueue<>();
        defaultQueue = new LinkedBlockingQueue<>();
        recordDispatcher = new ContentBasedRecordDispatcherBuilder<>()
                .when(orangePredicate).dispatchTo(orangeQueue)
                .otherwise(defaultQueue)
                .build();

        when(orangePredicate.matches(orangeRecord)).thenReturn(true);
        when(orangePredicate.matches(appleRecord)).thenReturn(false);
    }

    @Test
    public void orangeRecordShouldBeDispatchedToOrangeQueue() throws Exception {
        recordDispatcher.dispatchRecord(orangeRecord);
        assertThat(orangeQueue).containsOnly(orangeRecord);
        assertThat(defaultQueue).isEmpty();
    }

    @Test
    public void nonOrangeRecordShouldBeDispatchedToDefaultQueue() throws Exception {
        recordDispatcher.dispatchRecord(appleRecord);
        assertThat(defaultQueue).containsExactly(appleRecord);
        assertThat(orangeQueue).isEmpty();
    }

    @Test
    public void poisonRecordShouldBeDispatchedToAllQueues() throws Exception {
        recordDispatcher.dispatchRecord(poisonRecord);
        assertThat(defaultQueue).contains(poisonRecord);
        assertThat(orangeQueue).contains(poisonRecord);
    }

}
