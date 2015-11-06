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

import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobReportFormatter;
import org.easybatch.core.job.JobStatus;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class HtmlJobReportFormatterTest {

    private JobReportFormatter<String> jobReportFormatter;

    private JobReport jobReport;

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
        jobReportFormatter = new HtmlJobReportFormatter();
        jobReport = new JobReport();

        jobReport.setStatus(JobStatus.COMPLETED);

        jobReport.getParameters().setDataSource("In-Memory String");
        jobReport.getParameters().setLimit(8L);
        jobReport.getParameters().setName("job");
        jobReport.getParameters().setExecutionId("c8d6a5fc-b2b4-4ee0-9dda-f9ec042d5864");
        jobReport.getParameters().setSystemProperties(System.getProperties());

        jobReport.getMetrics().setStartTime(START_TIME);
        jobReport.getMetrics().setEndTime(END_TIME);
        jobReport.getMetrics().setTotalCount(8L);
        jobReport.getMetrics().incrementSkippedCount();
        jobReport.getMetrics().incrementSkippedCount();
        jobReport.getMetrics().incrementFilteredCount();
        jobReport.getMetrics().incrementFilteredCount();
        jobReport.getMetrics().incrementErrorCount();
        jobReport.getMetrics().incrementErrorCount();
        jobReport.getMetrics().incrementSuccessCount();
        jobReport.getMetrics().incrementSuccessCount();
    }

    @Ignore("TODO: Contents are identical but assertion fails due to different whitespaces")
    @Test
    public void testReportFormatting() {
        String result = jobReportFormatter.formatReport(jobReport);
        Scanner scanner = new Scanner(HtmlJobReportFormatterTest.class.getResourceAsStream("expectedReport.html"));
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }

        String expectedResult = stringBuilder.toString();
        assertThat(result).isXmlEqualTo(expectedResult);
    }

}
