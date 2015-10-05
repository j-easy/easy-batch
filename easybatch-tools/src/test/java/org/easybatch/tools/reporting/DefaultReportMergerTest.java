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

package org.easybatch.tools.reporting;

import org.easybatch.core.api.Report;
import org.easybatch.core.api.Status;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.easybatch.core.util.Utils.LINE_SEPARATOR;
import static org.junit.Assert.assertEquals;

public class DefaultReportMergerTest {

    private ReportMerger reportMerger;

    @Before
    public void setUp() throws Exception {
        reportMerger = new DefaultReportMerger();
    }

    @Test
    public void testReportsMerging() throws Exception {

        Report report1 = new Report();
        long startTime1 = 1l;
        long endTime1 = 10l;
        report1.setTotalRecords(6l);
        report1.incrementTotalSkippedRecords();
        report1.incrementTotalFilteredRecords();
        report1.incrementTotalRejectedRecord();
        report1.incrementTotalErrorRecord();
        report1.incrementTotalSuccessRecord();
        report1.setStartTime(startTime1);
        report1.setEndTime(endTime1);
        report1.setJobResult("result1");
        report1.setDataSource("datasource1");
        report1.setStatus(Status.ABORTED);

        Report report2 = new Report();
        long startTime2 = 2l;
        long endTime2 = 11l;
        report2.setTotalRecords(6l);
        report2.incrementTotalSkippedRecords();
        report2.incrementTotalFilteredRecords();
        report2.incrementTotalRejectedRecord();
        report2.incrementTotalErrorRecord();
        report2.incrementTotalSuccessRecord();
        report2.setStartTime(startTime2);
        report2.setEndTime(endTime2);
        report2.setJobResult("result2");
        report2.setDataSource("datasource2");
        report2.setStatus(Status.ABORTED);

        Report finalReport = reportMerger.mergerReports(report1, report2);

        assertEquals(new Long(12), finalReport.getTotalRecords()); //sum of total records
        assertEquals(2, finalReport.getSkippedRecordsCount());// sum of skipped records
        assertEquals(2, finalReport.getFilteredRecordsCount());// sum of filtered records
        assertEquals(2, finalReport.getRejectedRecordsCount());// sum of rejected records
        assertEquals(2, finalReport.getErrorRecordsCount());// sum of error records
        assertEquals(2, finalReport.getSuccessRecordsCount());// sum of success records
        assertEquals(1, finalReport.getStartTime());// min of start times
        assertEquals(11, finalReport.getEndTime());// max of end times

        //batch results
        List<Object> results = (List<Object>) finalReport.getJobResult();
        assertEquals("result1", results.get(0));
        assertEquals("result2", results.get(1));

        //data sources
        assertEquals("datasource1" + LINE_SEPARATOR + "datasource2" + LINE_SEPARATOR, finalReport.getDataSource());

        //if one partial report has aborted, the final result should be also aborted
        assertEquals(Status.ABORTED, finalReport.getStatus());

    }
}
