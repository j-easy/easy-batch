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

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class DefaultJobReportMergerTest {

    private JobReportMerger jobReportMerger;

    @Before
    public void setUp() throws Exception {
        jobReportMerger = new DefaultJobReportMerger();
    }

    @Test
    public void testReportsMerging() {

        Properties systemProperties = new Properties();
        JobMetrics metrics1 = new JobMetrics();
        JobReport jobReport1 = new JobReport();
        jobReport1.setMetrics(metrics1);
        long startTime1 = 1L;
        long endTime1 = 10L;
        jobReport1.getMetrics().incrementReadCount();
        jobReport1.getMetrics().incrementReadCount();
        jobReport1.getMetrics().incrementReadCount();
        jobReport1.getMetrics().incrementReadCount();
        jobReport1.getMetrics().incrementErrorCount();
        jobReport1.getMetrics().incrementFilterCount();
        jobReport1.getMetrics().incrementWriteCount(2);
        jobReport1.getMetrics().setStartTime(startTime1);
        jobReport1.getMetrics().setEndTime(endTime1);
        jobReport1.setStatus(JobStatus.FAILED);
        jobReport1.setJobName("job1");
        jobReport1.setSystemProperties(systemProperties);

        JobMetrics metrics2 = new JobMetrics();
        JobReport jobReport2 = new JobReport();
        jobReport2.setMetrics(metrics2);
        long startTime2 = 2L;
        long endTime2 = 11L;
        jobReport2.getMetrics().incrementReadCount();
        jobReport2.getMetrics().incrementReadCount();
        jobReport2.getMetrics().incrementReadCount();
        jobReport2.getMetrics().incrementReadCount();
        jobReport2.getMetrics().incrementErrorCount();
        jobReport2.getMetrics().incrementFilterCount();
        jobReport2.getMetrics().incrementWriteCount(2);
        jobReport2.getMetrics().setStartTime(startTime2);
        jobReport2.getMetrics().setEndTime(endTime2);
        jobReport2.setStatus(JobStatus.COMPLETED);
        jobReport2.setJobName("job2");
        jobReport2.setSystemProperties(systemProperties); // assume both jobs run on the same JVMs


        JobReport finalJobReport = jobReportMerger.mergerReports(jobReport1, jobReport2);

        assertEquals(8, finalJobReport.getMetrics().getReadCount()); // sum of read records
        assertEquals(4, finalJobReport.getMetrics().getWriteCount());// sum of written records
        assertEquals(2, finalJobReport.getMetrics().getFilterCount());// sum of filtered records
        assertEquals(2, finalJobReport.getMetrics().getErrorCount());// sum of error records
        assertEquals(1, finalJobReport.getMetrics().getStartTime());// min of start times
        assertEquals(11, finalJobReport.getMetrics().getEndTime());// max of end times

        //if one partial report has aborted, the final result should be also aborted
        assertEquals(JobStatus.FAILED, finalJobReport.getStatus());

        // job name is the concatenation of partial job names
        assertThat(finalJobReport.getJobName()).isEqualTo("job1|job2");
        assertThat(finalJobReport.getSystemProperties()).isEqualTo(systemProperties);
    }
}
