/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.extensions.msexcel;

import java.io.File;

import org.easybatch.core.filter.HeaderRecordFilter;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.writer.StandardOutputRecordWriter;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MsExcelSupportIntegrationTest {

    @Test
    public void integrationTest() throws Exception {

        File inputTweets = new File(this.getClass().getResource("/tweets-in.xls").toURI());
        File outputTweets = new File(this.getClass().getResource("/tweets-out.xls").toURI());

        Job job = JobBuilder.aNewJob()
                .reader(new MsExcelRecordReader(inputTweets))
                .filter(new HeaderRecordFilter())
                .mapper(new MsExcelRecordMapper(Tweet.class, "id", "user", "message"))
                //.marshaller(new MsExcelRecordMarshaller<>(Tweet.class, "id", "user", "message"))
                //.writer(new MsExcelRecordWriter(outputTweets))
                .writer(new StandardOutputRecordWriter())
                .build();

        JobReport report = JobExecutor.execute(job);

        assertThat(report).isNotNull();
        assertThat(report.getMetrics().getTotalCount()).isEqualTo(3);
        assertThat(report.getMetrics().getFilteredCount()).isEqualTo(1);
        assertThat(report.getMetrics().getSuccessCount()).isEqualTo(2);
    }

}
