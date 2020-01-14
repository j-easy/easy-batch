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
package org.easybatch.tools.monitoring;

import org.easybatch.core.job.JobReport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CliJobMonitoringListenerTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JobReport report;

    @Rule
    public final SystemOutRule systemOut = new SystemOutRule().enableLog();

    private CliJobMonitoringListener cliJobMonitoringListener;

    @Before
    public void setUp() throws Exception {
        cliJobMonitoringListener = new CliJobMonitoringListener();
        when(report.getMetrics().getReadCount()).thenReturn(1L);
        when(report.getMetrics().getFilterCount()).thenReturn(1L);
        when(report.getMetrics().getErrorCount()).thenReturn(1L);
        when(report.getMetrics().getWriteCount()).thenReturn(1L);
    }

    @Test
    public void testOnJobReportUpdate() throws Exception {
        cliJobMonitoringListener.onJobReportUpdate(report);

        assertThat(systemOut.getLog()).isEqualTo("\rRead count = 1 | Filter count = 1 | Error count = 1 | Write count = 1");
    }

}
