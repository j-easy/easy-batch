/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.core.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobExecutorTest {

    @Mock
    private Job job, anotherJob;
    @Mock
    JobReport report;
    @Mock
    Future<JobReport> reportFuture;
    @Mock
    private ExecutorService executorService;

    JobExecutor jobExecutor;

    @Before
    public void setUp() throws Exception {
        jobExecutor = new JobExecutor(executorService);
        when(executorService.submit(job)).thenReturn(reportFuture);
        when(reportFuture.get()).thenReturn(report);
    }

    @Test
    public void execute() {
        //when
        JobReport actual = jobExecutor.execute(job);

        //then
        assertThat(actual).isEqualTo(report);
    }

    @Test
    public void submit() throws Exception {
        //when
        jobExecutor.submit(job);

        //then
        verify(executorService).submit(job);
    }

    @Test
    public void submitAll() throws Exception {
        //when
        jobExecutor.submitAll(job, anotherJob);

        //then
        verify(executorService).invokeAll(asList(job, anotherJob));
    }

    @Test
    public void shutdown() throws Exception {
        //when
        jobExecutor.shutdown();

        //then
        verify(executorService).shutdown();
    }
}
