package io.github.benas.easybatch.tools.reporting;

import io.github.benas.easybatch.core.api.EasyBatchReport;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link EasyBatchReportsAggregator}.
 */
public class EasyBatchReportsAggregatorTest {

    @Test
    public void testReportsMerging() throws Exception {

        EasyBatchReport report1 = new EasyBatchReport();
        long startTime1 = 1l;
        long endTime1 = 10l;
        report1.setTotalRecords(5l);
        report1.addFilteredRecord(1);
        report1.addIgnoredRecord(2);
        report1.addRejectedRecord(3);
        report1.addErrorRecord(4);
        report1.addSuccessRecord(5);
        report1.setStartTime(startTime1);
        report1.setEndTime(endTime1);
        report1.addProcessingTime(1, 1);
        report1.addProcessingTime(2, 1);
        report1.addProcessingTime(3, 1);
        report1.addProcessingTime(4, 1);
        report1.addProcessingTime(5,1);
        report1.setEasyBatchResult("result1");
        report1.setDataSource("datasource1");

        EasyBatchReport report2 = new EasyBatchReport();
        long startTime2 = 2l;
        long endTime2 = 11l;
        report2.setTotalRecords(5l);
        report2.addFilteredRecord(6);
        report2.addIgnoredRecord(7);
        report2.addRejectedRecord(8);
        report2.addErrorRecord(9);
        report2.addSuccessRecord(10);
        report2.setStartTime(startTime2);
        report2.setEndTime(endTime2);
        report2.addProcessingTime(6,1);
        report2.addProcessingTime(7,1);
        report2.addProcessingTime(8,1);
        report2.addProcessingTime(9,1);
        report2.addProcessingTime(10,1);
        report2.setEasyBatchResult("result2");
        report2.setDataSource("datasource2");

        EasyBatchReportsAggregator reportsAggregator = new DefaultEasyBatchReportsAggregator();
        EasyBatchReport finalReport = reportsAggregator.aggregateReports(report1, report2);

        assertEquals(new Long(10), finalReport.getTotalRecords()); //sum of total records
        assertEquals(2, finalReport.getFilteredRecordsCount());// sum of filtered records
        assertEquals(2, finalReport.getIgnoredRecordsCount());// sum of ignored records
        assertEquals(2, finalReport.getRejectedRecordsCount());// sum of rejected records
        assertEquals(2, finalReport.getErrorRecordsCount());// sum of error records
        assertEquals(2, finalReport.getSuccessRecordsCount());// sum of success records
        assertEquals(1, finalReport.getStartTime());// min of start times
        assertEquals(11, finalReport.getEndTime());// max of end times

        //processing times
        Map<Long, Long> processingTimes = finalReport.getProcessingTimes();
        for (long i = 1; i <= 10; i++) {
            assertTrue(processingTimes.containsKey(i));// all records should be merged
            assertEquals(new Long(1), processingTimes.get(i));// check processing time for each record
        }

        //batch results
        List<Object> results = (List<Object>) finalReport.getEasyBatchResult();
        assertEquals("result1", results.get(0));
        assertEquals("result2", results.get(1));

        //data sources
        assertEquals("datasource1\ndatasource2\n", finalReport.getDataSource());

    }
}
