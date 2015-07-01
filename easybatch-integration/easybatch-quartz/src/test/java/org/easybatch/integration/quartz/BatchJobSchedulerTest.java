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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link BatchJobScheduler}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class BatchJobSchedulerTest {

    private static final Date now = new Date();

    private BatchJobScheduler batchJobScheduler;

    @Mock
    private Engine engine1, engine2;

    @Before
    public void setUp() throws Exception {
        batchJobScheduler = BatchJobScheduler.getInstance();

        when(engine1.getExecutionId()).thenReturn("engine1");
        when(engine1.getExecutionId()).thenReturn("123");

        when(engine2.getExecutionId()).thenReturn("engine2");
        when(engine2.getExecutionId()).thenReturn("456");
    }

    @Test
    public void testJobScheduling() throws Exception {
        batchJobScheduler.scheduleAt(engine1, now);
        batchJobScheduler.scheduleAt(engine2, now);

        assertThat(batchJobScheduler.isScheduled(engine1)).isTrue();
        assertThat(batchJobScheduler.isScheduled(engine2)).isTrue();

        batchJobScheduler.start();
        assertThat(batchJobScheduler.isStarted()).isTrue();

        Thread.sleep(500); // sleep to ensure the next verify is called after calling the engine

        verify(engine1).call();
        verify(engine2).call();

        batchJobScheduler.unschedule(engine1);
        assertThat(batchJobScheduler.isScheduled(engine1)).isFalse();

        batchJobScheduler.unschedule(engine2);
        assertThat(batchJobScheduler.isScheduled(engine2)).isFalse();

        verify(engine1, times(4)).getExecutionId();
        verify(engine2, times(4)).getExecutionId();

        batchJobScheduler.stop();
        assertThat(batchJobScheduler.isStopped()).isTrue();
    }

}
