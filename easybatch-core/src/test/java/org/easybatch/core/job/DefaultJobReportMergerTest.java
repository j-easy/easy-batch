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

import java.util.List;

import static org.easybatch.core.util.Utils.LINE_SEPARATOR;
import static org.junit.Assert.assertEquals;

public class DefaultJobReportMergerTest {

    private JobReportMerger jobReportMerger;

    @Before
    public void setUp() throws Exception {
        jobReportMerger = new DefaultJobReportMerger();
    }

    @Test
    public void testReportsMerging() throws Exception {

        JobReport jobReport1 = new JobReport();
        long startTime1 = 1L;
        long endTime1 = 10L;
        jobReport1.getMetrics().setTotalCount(6L);
        jobReport1.getMetrics().incrementSkippedCount();
        jobReport1.getMetrics().incrementFilteredCount();
        jobReport1.getMetrics().incrementErrorCount();
        jobReport1.getMetrics().incrementSuccessCount();
        jobReport1.getMetrics().setStartTime(startTime1);
        jobReport1.getMetrics().setEndTime(endTime1);
        jobReport1.setJobResult(new JobResult("result1"));
        jobReport1.getParameters().setDataSource("datasource1");
        jobReport1.setStatus(JobStatus.ABORTED);

        JobReport jobReport2 = new JobReport();
        long startTime2 = 2L;
        long endTime2 = 11L;
        jobReport2.getMetrics().setTotalCount(6L);
        jobReport2.getMetrics().incrementSkippedCount();
        jobReport2.getMetrics().incrementFilteredCount();
        jobReport2.getMetrics().incrementErrorCount();
        jobReport2.getMetrics().incrementSuccessCount();
        jobReport2.getMetrics().setStartTime(startTime2);
        jobReport2.getMetrics().setEndTime(endTime2);
        jobReport2.setJobResult(new JobResult("result2"));
        jobReport2.getParameters().setDataSource("datasource2");
        jobReport2.setStatus(JobStatus.ABORTED);

        JobReport finalJobReport = jobReportMerger.mergerReports(jobReport1, jobReport2);

        assertEquals(new Long(12), finalJobReport.getMetrics().getTotalCount()); //sum of total records
        assertEquals(2, finalJobReport.getMetrics().getSkippedCount());// sum of skipped records
        assertEquals(2, finalJobReport.getMetrics().getFilteredCount());// sum of filtered records
        assertEquals(2, finalJobReport.getMetrics().getErrorCount());// sum of error records
        assertEquals(2, finalJobReport.getMetrics().getSuccessCount());// sum of success records
        assertEquals(1, finalJobReport.getMetrics().getStartTime());// min of start times
        assertEquals(11, finalJobReport.getMetrics().getEndTime());// max of end times

        //batch results
        List<Object> results = (List<Object>) finalJobReport.getResult();
        assertEquals("result1", results.get(0));
        assertEquals("result2", results.get(1));

        //data sources
        assertEquals("datasource1" + LINE_SEPARATOR + "datasource2" + LINE_SEPARATOR, finalJobReport.getParameters().getDataSource());

        //if one partial report has aborted, the final result should be also aborted
        assertEquals(JobStatus.ABORTED, finalJobReport.getStatus());

    }
}
