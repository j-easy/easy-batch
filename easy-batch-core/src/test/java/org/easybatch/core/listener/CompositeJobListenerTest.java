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
package org.easybatch.core.listener;

import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.class)
public class CompositeJobListenerTest {

    @Mock
    private JobParameters parameters;
    @Mock
    private JobReport report;
    @Mock
    private JobListener jobListener1, jobListener2;

    private CompositeJobListener compositeJobListener;

    @Before
    public void setUp() throws Exception {
        compositeJobListener = new CompositeJobListener(asList(jobListener1, jobListener2));
    }

    @Test
    public void testBeforeJobStart() throws Exception {
        compositeJobListener.beforeJobStart(parameters);

        InOrder inOrder = inOrder(jobListener1, jobListener2);
        inOrder.verify(jobListener1).beforeJobStart(parameters);
        inOrder.verify(jobListener2).beforeJobStart(parameters);
    }

    @Test
    public void testAfterJobEnd() throws Exception {
        compositeJobListener.afterJobEnd(report);

        InOrder inOrder = inOrder(jobListener1, jobListener2);
        inOrder.verify(jobListener2).afterJobEnd(report);
        inOrder.verify(jobListener1).afterJobEnd(report);
    }
}
