/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.extensions.quartz;

import org.easybatch.core.job.Job;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JobSchedulerTest {

    private static final Date now = new Date();

    private JobScheduler jobScheduler;

    @Mock
    private Job job1, job2;

    @Before
    public void setUp() throws Exception {
        jobScheduler = JobScheduler.getInstance();

        when(job1.getExecutionId()).thenReturn("job1");
        when(job1.getExecutionId()).thenReturn("123");

        when(job2.getExecutionId()).thenReturn("job2");
        when(job2.getExecutionId()).thenReturn("456");
    }

    @Test
    public void testJobScheduling() throws Exception {
        jobScheduler.scheduleAt(job1, now);
        jobScheduler.scheduleAt(job2, now);

        assertThat(jobScheduler.isScheduled(job1)).isTrue();
        assertThat(jobScheduler.isScheduled(job2)).isTrue();

        jobScheduler.start();
        assertThat(jobScheduler.isStarted()).isTrue();

        Thread.sleep(500); // sleep to ensure the next verify is called after calling the job

        verify(job1).call();
        verify(job2).call();

        jobScheduler.unschedule(job1);
        assertThat(jobScheduler.isScheduled(job1)).isFalse();

        jobScheduler.unschedule(job2);
        assertThat(jobScheduler.isScheduled(job2)).isFalse();

        verify(job1, times(4)).getExecutionId();
        verify(job2, times(4)).getExecutionId();

        jobScheduler.stop();
        assertThat(jobScheduler.isStopped()).isTrue();
    }

}
