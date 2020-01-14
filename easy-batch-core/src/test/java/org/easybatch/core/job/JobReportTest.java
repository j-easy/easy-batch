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

import java.util.Calendar;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

public class JobReportTest {

	private static long START_TIME;
	private static long END_TIME;

	static {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, Calendar.JANUARY, 1, 1, 0, 0);
		START_TIME = calendar.getTime().getTime();
		END_TIME = START_TIME + 10 * 1000;
	}

	private JobReport report;

	@Before
	public void setUp() throws Exception {
		Properties systemProperties = new Properties();
		systemProperties.setProperty("sysprop1", "foo");
		systemProperties.setProperty("sysprop2", "bar");
		JobParameters parameters = new JobParameters();
		JobMetrics metrics = new JobMetrics();
		metrics.setStartTime(START_TIME);
		metrics.setEndTime(END_TIME);
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
						"\tStart time = 2015-01-01 01:00:00" + LINE_SEPARATOR +
						"\tEnd time = 2015-01-01 01:00:10" + LINE_SEPARATOR +
						"\tDuration = 0d 0hr 0min 10sec 0ms" + LINE_SEPARATOR +
						"\tRead count = 0" + LINE_SEPARATOR +
						"\tWrite count = 0" + LINE_SEPARATOR +
						"\tFilter count = 0" + LINE_SEPARATOR +
						"\tError count = 0" + LINE_SEPARATOR +
						"\tnbFoos = 1" + LINE_SEPARATOR +
						"System properties:" + LINE_SEPARATOR +
						"\tsysprop1 = foo" + LINE_SEPARATOR +
						"\tsysprop2 = bar";

		String formattedReport = report.toString();

		assertThat(formattedReport).isEqualTo(expectedReport);
	}

}
