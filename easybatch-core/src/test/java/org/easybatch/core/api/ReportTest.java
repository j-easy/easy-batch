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

import java.util.Arrays;
import java.util.Calendar;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test class for {@link org.easybatch.core.api.Report} formatting utility methods.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ReportTest {

    private Report report;

    private static long START_TIME;
    private static long END_TIME;

    static{
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
        report.setTotalRecords(10);
        report.setCurrentRecordNumber(2);
        report.setBatchResult(50);
        report.addFilteredRecord(1);report.addFilteredRecord(2);
        report.addIgnoredRecord(3);report.addIgnoredRecord(4);
        report.addRejectedRecord(5);report.addRejectedRecord(6);
        report.addErrorRecord(7);report.addErrorRecord(8);
        report.addSuccessRecord(9);report.addSuccessRecord(10);
    }

    @Test
    public void reportStatisticsShouldBeValid() {
        assertThat(report).isNotNull();

        assertThat(report.getFilteredRecordsCount()).isEqualTo(2);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(2);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(2);
        assertThat(report.getErrorRecordsCount()).isEqualTo(2);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(2);

        assertThat(report.getFilteredRecords()).isEqualTo(Arrays.asList(1, 2));
        assertThat(report.getIgnoredRecords()).isEqualTo(Arrays.asList(3, 4));
        assertThat(report.getRejectedRecords()).isEqualTo(Arrays.asList(5, 6));
        assertThat(report.getErrorRecords()).isEqualTo(Arrays.asList(7, 8));
        assertThat(report.getSuccessRecords()).isEqualTo(Arrays.asList(9, 10));
    }


    @Test
    public void reportStatisticsShouldBeCorrectlyFormatted() {
        assertThat(report).isNotNull();

        assertThat(report.getFormattedStartTime()).isEqualTo("2015-01-01 01:00:00");
        assertThat(report.getFormattedEndTime()).isEqualTo("2015-01-01 01:00:10");
        assertThat(report.getFormattedBatchDuration()).isEqualTo("10000ms");
        assertThat(report.getFormattedFilteredRecords()).isEqualTo("2 (20.0%)");
        assertThat(report.getFormattedIgnoredRecords()).isEqualTo("2 (20.0%)");
        assertThat(report.getFormattedRejectedRecords()).isEqualTo("2 (20.0%)");
        assertThat(report.getFormattedErrorRecords()).isEqualTo("2 (20.0%)");
        assertThat(report.getFormattedSuccessRecords()).isEqualTo("2 (20.0%)");
        assertThat(report.getFormattedAverageRecordProcessingTime()).isEqualTo("1000.0ms");
        assertThat(report.getFormattedProgress()).isEqualTo("2/10 (20.0%)");
    }

    @Test
    public void whenTotalRecordsIsZeroThenFormattedAverageRecordProcessingTimeShouldBeNA() {
        report.setTotalRecords(0);
        assertThat(report.getFormattedAverageRecordProcessingTime()).isEqualTo("N/A");
    }

    @Test
    public void whenTotalRecordsIsZeroThenFormattedProgressShouldBeNA() {
        report.setTotalRecords(0);
        assertThat(report.getFormattedAverageRecordProcessingTime()).isEqualTo("N/A");
    }

    @Test
    public void whenTotalRecordsIsNullThenFormattedAverageRecordProcessingTimeShouldBeNA() {
        report.setTotalRecords(null);
        assertThat(report.getFormattedProgress()).isEqualTo("N/A");
    }

    @Test
    public void whenTotalRecordsIsNullThenFormattedProgressShouldBeNA() {
        report.setTotalRecords(null);
        assertThat(report.getFormattedProgress()).isEqualTo("N/A");
    }

    @Test
    public void whenBatchResultIsNullThenITShouldNotBePrintedOut() {
        report.setBatchResult(null);
        assertThat(report.toString()).doesNotContain("Result");
    }

}
