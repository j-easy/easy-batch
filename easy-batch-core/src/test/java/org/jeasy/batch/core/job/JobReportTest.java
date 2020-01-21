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
package org.jeasy.batch.core.job;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jeasy.batch.core.util.Utils.LINE_SEPARATOR;

public class JobReportTest {

	private JobReport report;

	@Before
	public void setUp() throws Exception {
		LocalDateTime startTime = LocalDateTime.of(2020, 1, 20, 10, 15, 20);
		LocalDateTime endTime = startTime.plus(10, ChronoUnit.SECONDS);
		Properties systemProperties = new Properties();
		systemProperties.setProperty("sysprop1", "foo");
		systemProperties.setProperty("sysprop2", "bar");
		JobParameters parameters = new JobParameters();
		JobMetrics metrics = new JobMetrics();
		metrics.setStartTime(startTime);
		metrics.setEndTime(endTime);
		metrics.addMetric("nbFoos", 1);
		report = new JobReport();
		report.setParameters(parameters);
		report.setMetrics(metrics);
		report.setJobName("job");
		report.setStatus(JobStatus.COMPLETED);
		report.setSystemProperties(systemProperties);
	}

	@Test
	public void testFormatReport() {
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
						"\tStart time = 2020-01-20 10:15:20" + LINE_SEPARATOR +
						"\tEnd time = 2020-01-20 10:15:30" + LINE_SEPARATOR +
						"\tDuration = 10sec 0ms" + LINE_SEPARATOR +
						"\tRead count = 0" + LINE_SEPARATOR +
						"\tWrite count = 0" + LINE_SEPARATOR +
						"\tFilter count = 0" + LINE_SEPARATOR +
						"\tError count = 0" + LINE_SEPARATOR +
						"\tnbFoos = 1";

		String formattedReport = report.toString();

		assertThat(formattedReport).isEqualTo(expectedReport);
	}

}
