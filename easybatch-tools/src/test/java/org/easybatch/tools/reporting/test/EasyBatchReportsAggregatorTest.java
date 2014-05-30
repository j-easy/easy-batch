package org.easybatch.tools.reporting.test;

import org.easybatch.core.api.EasyBatchReport;
import org.easybatch.tools.reporting.DefaultEasyBatchReportsAggregator;
import org.easybatch.tools.reporting.EasyBatchReportsAggregator;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link org.easybatch.tools.reporting.EasyBatchReportsAggregator}.
 */
public class EasyBatchReportsAggregatorTest {

    @Test
    public void testReportsMerging() throws Exception {

        EasyBatchReport report1 = new EasyBatchReport();
        long startTime1 = 1l;
        long endTime1 = 10l;
        report1.setTotalRecords(5);
        report1.addFilteredRecord(1);
        report1.addIgnoredRecord(2);
        report1.addRejectedRecord(3);
        report1.addErrorRecord(4);
        report1.addSuccessRecord(5);
        report1.setStartTime(startTime1);
        report1.setEndTime(endTime1);
        report1.setEasyBatchResult("result1");
        report1.setDataSource("datasource1");

        EasyBatchReport report2 = new EasyBatchReport();
        long startTime2 = 2l;
        long endTime2 = 11l;
        report2.setTotalRecords(5);
        report2.addFilteredRecord(6);
        report2.addIgnoredRecord(7);
        report2.addRejectedRecord(8);
        report2.addErrorRecord(9);
        report2.addSuccessRecord(10);
        report2.setStartTime(startTime2);
        report2.setEndTime(endTime2);
        report2.setEasyBatchResult("result2");
        report2.setDataSource("datasource2");

        EasyBatchReportsAggregator reportsAggregator = new DefaultEasyBatchReportsAggregator();
        EasyBatchReport finalReport = reportsAggregator.aggregateReports(report1, report2);

        assertEquals(new Integer(10), finalReport.getTotalRecords()); //sum of total records
        assertEquals(2, finalReport.getFilteredRecordsCount());// sum of filtered records
        assertEquals(2, finalReport.getIgnoredRecordsCount());// sum of ignored records
        assertEquals(2, finalReport.getRejectedRecordsCount());// sum of rejected records
        assertEquals(2, finalReport.getErrorRecordsCount());// sum of error records
        assertEquals(2, finalReport.getSuccessRecordsCount());// sum of success records
        assertEquals(1, finalReport.getStartTime());// min of start times
        assertEquals(11, finalReport.getEndTime());// max of end times

        //batch results
        List<Object> results = (List<Object>) finalReport.getEasyBatchResult();
        assertEquals("result1", results.get(0));
        assertEquals("result2", results.get(1));

        //data sources
        assertEquals("datasource1\ndatasource2\n", finalReport.getDataSource());

    }
}
