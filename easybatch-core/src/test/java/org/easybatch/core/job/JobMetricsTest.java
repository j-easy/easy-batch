package org.easybatch.core.job;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link JobMetrics}
 *
 * @author verdi8 (https://github.com/verdi8)
 */
public class JobMetricsTest {

    private JobMetrics jobMetrics;

    @Before
    public void setUp() throws Exception {
        jobMetrics = new JobMetrics();
    }

    @Test
    public void testFilterCount() {
        assertThat(jobMetrics.getFilterCount()).isEqualTo(0);
        jobMetrics.incrementFilterCount();
        jobMetrics.incrementFilterCount();
        assertThat(jobMetrics.getFilterCount()).isEqualTo(2);
        jobMetrics.incrementFilterCount(5);
        jobMetrics.incrementFilterCount(3);
        assertThat(jobMetrics.getFilterCount()).isEqualTo(10);
    }

    @Test
    public void testErrorCount() {
        assertThat(jobMetrics.getErrorCount()).isEqualTo(0);
        jobMetrics.incrementErrorCount();
        jobMetrics.incrementErrorCount();
        jobMetrics.incrementErrorCount();
        assertThat(jobMetrics.getErrorCount()).isEqualTo(3);
        jobMetrics.incrementErrorCount(7);
        jobMetrics.incrementErrorCount(5);
        assertThat(jobMetrics.getErrorCount()).isEqualTo(15);
    }

    @Test
    public void testReadCount() {
        assertThat(jobMetrics.getReadCount()).isEqualTo(0);
        jobMetrics.incrementReadCount(3);
        jobMetrics.incrementReadCount(4);
        assertThat(jobMetrics.getReadCount()).isEqualTo(7);
        jobMetrics.incrementReadCount();
        jobMetrics.incrementReadCount();
        jobMetrics.incrementReadCount();
        assertThat(jobMetrics.getReadCount()).isEqualTo(10);
    }

    @Test
    public void testWriteCount() {
        assertThat(jobMetrics.getWriteCount()).isEqualTo(0);
        jobMetrics.incrementWriteCount(11);
        jobMetrics.incrementWriteCount(9);
        assertThat(jobMetrics.getWriteCount()).isEqualTo(20);
    }

    @Test
    public void testStartEndTimesAndDuration() {
        assertThat(jobMetrics.getStartTime()).isEqualTo(0);
        assertThat(jobMetrics.getEndTime()).isEqualTo(0);
        assertThat(jobMetrics.getDuration()).isEqualTo(0);

        long expectedStartTime = System.currentTimeMillis();
        long expectedEndTime = expectedStartTime + 12543;
        jobMetrics.setStartTime(expectedStartTime);
        jobMetrics.setEndTime(expectedEndTime);

        assertThat(jobMetrics.getStartTime()).isEqualTo(expectedStartTime);
        assertThat(jobMetrics.getEndTime()).isEqualTo(expectedEndTime);
        assertThat(jobMetrics.getDuration()).isEqualTo(12543);
    }

    @Test
    public void testCustomMetrics() {
        assertThat(jobMetrics.getCustomMetrics().isEmpty()).isTrue();
        jobMetrics.addMetric("metric1", 987654321L);
        jobMetrics.addMetric("metric2", "aValue");
        assertThat(jobMetrics.getCustomMetrics().size()).isEqualTo(2);
        assertThat(jobMetrics.getCustomMetrics().get("metric1")).isEqualTo(987654321L);
        assertThat(jobMetrics.getCustomMetrics().get("metric2")).isEqualTo("aValue");
    }

}