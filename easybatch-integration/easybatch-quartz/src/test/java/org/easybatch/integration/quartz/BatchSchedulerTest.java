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

import org.easybatch.core.api.Engine;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link BatchScheduler}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class BatchSchedulerTest {

    private static final Date now = new Date();

    private static final int everyMinute = 1;

    @Mock
    private Engine engine1, engine2;

    @Before
    public void setUp() throws Exception {
        when(engine1.getExecutionId()).thenReturn("123");
        when(engine2.getExecutionId()).thenReturn("456");
    }

    @org.junit.Test
    public void testJobScheduling() throws Exception {

        BatchScheduler batchScheduler = new BatchScheduler(engine1);
        batchScheduler.scheduleAtWithInterval(now, everyMinute);
        batchScheduler.start();

        assertThat(batchScheduler.isStarted()).isTrue();

        verify(engine1).call();

        batchScheduler.stop();

    }

    @org.junit.Test
    public void testMultipleJobsScheduling() throws Exception {

        BatchScheduler batchScheduler1 = new BatchScheduler(engine1);
        batchScheduler1.scheduleAtWithInterval(now, everyMinute);
        batchScheduler1.start();

        BatchScheduler batchScheduler2 = new BatchScheduler(engine2);
        batchScheduler2.scheduleAtWithInterval(now, everyMinute);
        batchScheduler2.start();

        assertThat(batchScheduler1).isNotEqualTo(batchScheduler2);

        assertThat(batchScheduler1.isStarted()).isTrue();
        assertThat(batchScheduler2.isStarted()).isTrue();

        verify(engine1).call();
        verify(engine2).call();

        batchScheduler1.stop();
        batchScheduler2.stop();

    }

}
