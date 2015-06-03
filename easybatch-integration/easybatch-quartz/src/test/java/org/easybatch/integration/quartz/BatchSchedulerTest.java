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

package org.easybatch.integration.quartz;

import org.easybatch.core.api.RecordProcessor;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.record.StringRecord;
import org.junit.After;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link BatchScheduler}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class BatchSchedulerTest {

    private BatchScheduler batchScheduler;

    @Mock
    private RecordProcessor<StringRecord, StringRecord> recordProcessor;

    @org.junit.Test
    public void testJobScheduling() throws Exception {

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new StringRecordReader("foo\nbar\n"))
                .processor(recordProcessor)
                .build();

        batchScheduler = new BatchScheduler(engine);
        batchScheduler.scheduleAtWithInterval(new Date(), 1);
        batchScheduler.start();

        assertThat(batchScheduler.isStarted()).isTrue();

        verify(recordProcessor, times(2)).processRecord(any(StringRecord.class));

    }

    @org.junit.Test
    public void testMultipleJobsScheduling() throws Exception {

        Engine engine1 = new EngineBuilder().build();
        Engine engine2 = new EngineBuilder().build();

        BatchScheduler batchScheduler1 = new BatchScheduler(engine1);
        batchScheduler1.scheduleAtWithInterval(new Date(), 1);
        batchScheduler1.start();

        BatchScheduler batchScheduler2 = new BatchScheduler(engine2);
        batchScheduler2.scheduleAtWithInterval(new Date(), 1);
        batchScheduler2.start();

        assertThat(batchScheduler1.isStarted()).isTrue();
        assertThat(batchScheduler2.isStarted()).isTrue();

        batchScheduler1.stop();
        batchScheduler2.stop();

    }

    @After
    public void tearDown() throws Exception {
        if (batchScheduler != null) {
            batchScheduler.stop();
        }
    }

}
