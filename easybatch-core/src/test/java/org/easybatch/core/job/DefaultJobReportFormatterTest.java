package org.easybatch.core.job;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

public class DefaultJobReportFormatterTest {

    private static long START_TIME;
    private static long END_TIME;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, Calendar.JANUARY, 1, 1, 0, 0);
        START_TIME = calendar.getTime().getTime();
        END_TIME = START_TIME + 10 * 1000;
    }

    private JobReport report;

    private DefaultJobReportFormatter reportFormatter;

    @Before
    public void setUp() throws Exception {
        JobParameters parameters = new JobParameters();
        JobMetrics metrics = new JobMetrics();
        metrics.setStartTime(START_TIME);
        metrics.setEndTime(END_TIME);
        report = new JobReport();
        report.setParameters(parameters);
        report.setMetrics(metrics);
        report.setJobName("job");
        report.setStatus(JobStatus.COMPLETED);
        reportFormatter = new DefaultJobReportFormatter();
    }

    @Test
    public void testFormatReport() throws Exception {
        String expectedReport = 
                "Job Report:" + LINE_SEPARATOR +
                "===========" + LINE_SEPARATOR +
                "Name: job" + LINE_SEPARATOR +
                "Status: COMPLETED" + LINE_SEPARATOR +
                "Parameters:" + LINE_SEPARATOR +
                "\tBatch size = 100" + LINE_SEPARATOR +
                "\tError threshold = N/A" + LINE_SEPARATOR +
                "\tJmx monitoring = false" + LINE_SEPARATOR +
                "Metrics:" + LINE_SEPARATOR +
                "\tStart time = 2015-01-01 01:00:00" + LINE_SEPARATOR +
                "\tEnd time = 2015-01-01 01:00:10" + LINE_SEPARATOR +
                "\tDuration = 0hr 0min 10sec 0ms" + LINE_SEPARATOR +
                "\tRead count = 0" + LINE_SEPARATOR +
                "\tWrite count = 0" + LINE_SEPARATOR +
                "\tFiltered count = 0" + LINE_SEPARATOR +
                "\tError count = 0";

        String formattedReport = reportFormatter.formatReport(report);

        Assertions.assertThat(formattedReport).isEqualTo(expectedReport);
    }
}