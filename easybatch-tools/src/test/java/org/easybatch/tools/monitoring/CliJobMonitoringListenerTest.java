package org.easybatch.tools.monitoring;

import org.easybatch.core.job.JobReport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
        when(report.getMetrics().getFilteredCount()).thenReturn(1L);
        when(report.getMetrics().getErrorCount()).thenReturn(1L);
        when(report.getMetrics().getWriteCount()).thenReturn(1L);
    }

    @Test
    public void testOnJobReportUpdate() throws Exception {
        cliJobMonitoringListener.onJobReportUpdate(report);

        assertThat(systemOut.getLog()).isEqualTo("\rRead count = 1 | Filtered count = 1 | Error count = 1 | Write count = 1");
    }

}