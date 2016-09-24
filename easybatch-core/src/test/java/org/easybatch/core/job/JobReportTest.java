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

package org.easybatch.core.job;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class JobReportTest {

    private static long START_TIME;

    private static long END_TIME;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, Calendar.JANUARY, 1, 1, 0, 0);
        START_TIME = calendar.getTime().getTime();
        END_TIME = START_TIME + 10 * 1000;
    }

    private JobReport jobReport;

    @Before
    public void setUp() throws Exception {
        jobReport = new JobReport();
        jobReport.getMetrics().setStartTime(START_TIME);
        jobReport.getMetrics().setEndTime(END_TIME);
        jobReport.getMetrics().setTotalCount(4L);
        jobReport.getMetrics().incrementSkippedCount();
        jobReport.getMetrics().incrementFilteredCount();
        jobReport.getMetrics().incrementErrorCount();
        jobReport.getMetrics().incrementSuccessCount();
    }

    @Test
    public void reportStatisticsShouldBeValid() {
        assertThat(jobReport.getMetrics().getSkippedCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(1);
    }

    @Test
    public void reportStatisticsPercentsShouldBeCorrectlyRounded() {
        jobReport = new JobReport();
        jobReport.getMetrics().setTotalCount(5L);
        jobReport.getMetrics().incrementFilteredCount();
        jobReport.getMetrics().incrementFilteredCount();
        jobReport.getMetrics().incrementSuccessCount();
        jobReport.getMetrics().incrementSuccessCount();
        jobReport.getMetrics().incrementSuccessCount();

        assertThat(jobReport.getFormattedFilteredCount()).isEqualTo("2 (40.0%)");
        assertThat(jobReport.getFormattedSuccessCount()).isEqualTo("3 (60.0%)");
    }

    @Test
    public void reportStatisticsShouldBeCorrectlyFormatted() {
        assertThat(jobReport.getFormattedStartTime()).isEqualTo("2015-01-01 01:00:00");
        assertThat(jobReport.getFormattedEndTime()).isEqualTo("2015-01-01 01:00:10");
        assertThat(jobReport.getFormattedDuration()).isEqualTo("10000ms");
        assertThat(jobReport.getFormattedSkippedCount()).isEqualTo("1 (25.0%)");
        assertThat(jobReport.getFormattedFilteredCount()).isEqualTo("1 (25.0%)");
        assertThat(jobReport.getFormattedErrorCount()).isEqualTo("1 (25.0%)");
        assertThat(jobReport.getFormattedSuccessCount()).isEqualTo("1 (25.0%)");
        assertThat(jobReport.getFormattedRecordProcessingTimeAverage()).isEqualTo("2500.0ms");
    }

    @Test
    public void whenTotalRecordsIsZero_ThenFormattedAverageRecordProcessingTimeShouldBeNA() {
        jobReport.getMetrics().setTotalCount(0L);
        assertThat(jobReport.getFormattedRecordProcessingTimeAverage()).isEqualTo("N/A");
    }

    @Test
    public void whenTotalRecordsIsZero_ThenFormattedProgressShouldBeNA() {
        jobReport.getMetrics().setTotalCount(0L);
        assertThat(jobReport.getFormattedRecordProcessingTimeAverage()).isEqualTo("N/A");
    }

    @Test
    public void whenTotalRecordsIsZero_ThenStatisticsPercentsShouldNotBePrintedOut() throws Exception {
        jobReport.getMetrics().setTotalCount(0L);
        assertThat(jobReport.getFormattedSkippedCount()).doesNotContain("%");
        assertThat(jobReport.getFormattedFilteredCount()).doesNotContain("%");
        assertThat(jobReport.getFormattedErrorCount()).doesNotContain("%");
        assertThat(jobReport.getFormattedSuccessCount()).doesNotContain("%");
    }

    @Test
    public void whenTotalRecordsIsNull_ThenFormattedAverageRecordProcessingTimeShouldBeNA() {
        jobReport.getMetrics().setTotalCount(null);
        assertThat(jobReport.getFormattedProgress()).isEqualTo("N/A");
    }

    @Test
    public void whenTotalRecordsIsNull_ThenFormattedProgressShouldBeNA() {
        jobReport.getMetrics().setTotalCount(null);
        assertThat(jobReport.getFormattedProgress()).isEqualTo("N/A");
    }

    @Test
    public void whenJobResultIsNull_ThenItShouldBeNA() {
        jobReport.setJobResult(null);
        assertThat(jobReport.toString()).contains("Result: N/A");
    }

    @Test
    public void whenDataSourceIsNull_ThenItShouldBeNA() {
        jobReport.getParameters().setDataSource(null);
        assertThat(jobReport.getFormattedDataSource()).isEqualTo("N/A");
    }

    @Test
    public void jobTimeoutShouldBeCorrectlyFormatted() throws Exception {
        long oneMinute = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
        jobReport.getParameters().setTimeout(oneMinute);
        assertThat(jobReport.toString()).contains("Timeout = 1m");

        long twoHours = TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS);
        jobReport.getParameters().setTimeout(twoHours);
        assertThat(jobReport.toString()).contains("Timeout = 120m");

        long threeDays = TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS);
        jobReport.getParameters().setTimeout(threeDays);
        assertThat(jobReport.toString()).contains("Timeout = 4320m");
    }
}
