/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.core.api;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportTest {
    
    private Report report;

    private static long START_TIME;
    private static long END_TIME;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, Calendar.JANUARY, 1, 1, 0, 0);
        START_TIME = calendar.getTime().getTime();
        END_TIME = START_TIME + 10 * 1000;
    }

    @Before
    public void setUp() throws Exception {
        report = new Report();
        report.setStartTime(START_TIME);
        report.setEndTime(END_TIME);
        report.setStatus(Status.FINISHED);
        report.setDataSource("In-Memory");
        report.setTotalRecords(4L);
        report.setCurrentRecordNumber(2);
        report.setJobResult(50);
        report.incrementTotalSkippedRecords();
        report.incrementTotalFilteredRecords();
        report.incrementTotalErrorRecord();
        report.incrementTotalSuccessRecord();
    }

    @Test
    public void reportStatisticsShouldBeValid() {
        assertThat(report.getSkippedRecordsCount()).isEqualTo(1);
        assertThat(report.getFilteredRecordsCount()).isEqualTo(1);
        assertThat(report.getErrorRecordsCount()).isEqualTo(1);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(1);
    }

    @Test
    public void reportStatisticsPercentsShouldBeCorrectlyRounded() {
        report = new Report();
        report.setTotalRecords(5L);
        report.incrementTotalFilteredRecords();
        report.incrementTotalFilteredRecords();
        report.incrementTotalSuccessRecord();
        report.incrementTotalSuccessRecord();
        report.incrementTotalSuccessRecord();

        assertThat(report.getFormattedFilteredRecords()).isEqualTo("2 (40.0%)");
        assertThat(report.getFormattedSuccessRecords()).isEqualTo("3 (60.0%)");
    }

    @Test
    public void reportStatisticsShouldBeCorrectlyFormatted() {
        assertThat(report.getFormattedStartTime()).isEqualTo("2015-01-01 01:00:00");
        assertThat(report.getFormattedEndTime()).isEqualTo("2015-01-01 01:00:10");
        assertThat(report.getFormattedJobDuration()).isEqualTo("10000ms");
        assertThat(report.getFormattedSkippedRecords()).isEqualTo("1 (25.0%)");
        assertThat(report.getFormattedFilteredRecords()).isEqualTo("1 (25.0%)");
        assertThat(report.getFormattedErrorRecords()).isEqualTo("1 (25.0%)");
        assertThat(report.getFormattedSuccessRecords()).isEqualTo("1 (25.0%)");
        assertThat(report.getFormattedAverageRecordProcessingTime()).isEqualTo("2500.0ms");
        assertThat(report.getFormattedProgress()).isEqualTo("2/4 (50.0%)");
    }

    @Test
    public void whenTotalRecordsIsZero_ThenFormattedAverageRecordProcessingTimeShouldBeNA() {
        report.setTotalRecords(0L);
        assertThat(report.getFormattedAverageRecordProcessingTime()).isEqualTo("N/A");
    }

    @Test
    public void whenTotalRecordsIsZero_ThenFormattedProgressShouldBeNA() {
        report.setTotalRecords(0L);
        assertThat(report.getFormattedAverageRecordProcessingTime()).isEqualTo("N/A");
    }

    @Test
    public void whenTotalRecordsIsZero_ThenStatisticsPercentsShouldNotBePrintedOut() throws Exception {
        report.setTotalRecords(0L);
        assertThat(report.getFormattedSkippedRecords()).doesNotContain("%");
        assertThat(report.getFormattedFilteredRecords()).doesNotContain("%");
        assertThat(report.getFormattedErrorRecords()).doesNotContain("%");
        assertThat(report.getFormattedSuccessRecords()).doesNotContain("%");
    }

    @Test
    public void whenTotalRecordsIsNull_ThenFormattedAverageRecordProcessingTimeShouldBeNA() {
        report.setTotalRecords(null);
        assertThat(report.getFormattedProgress()).isEqualTo("N/A");
    }

    @Test
    public void whenTotalRecordsIsNull_ThenFormattedProgressShouldBeNA() {
        report.setTotalRecords(null);
        assertThat(report.getFormattedProgress()).isEqualTo("N/A");
    }

    @Test
    public void whenBatchResultIsNull_ThenITShouldNotBePrintedOut() {
        report.setJobResult(null);
        assertThat(report.toString()).doesNotContain("Result");
    }

}
