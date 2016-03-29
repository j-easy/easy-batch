/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.extensions.apache.common.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.Batch;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;

public class ApacheCommonCsvBatchReaderTest {

    private static final int BATCH_SIZE = 2;

    private ApacheCommonCsvBatchReader apacheCommonCsvBatchReader;

    @Before
    public void setUp() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVParser parser = new CSVParser(new FileReader(this.getClass().getResource("/tweets.csv").getFile()), csvFormat);
        apacheCommonCsvBatchReader = new ApacheCommonCsvBatchReader(parser, BATCH_SIZE);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBatchProcessing() throws Exception {

        Job job = aNewJob()
                .reader(apacheCommonCsvBatchReader)
                .processor(new RecordCollector())
                .build();

        JobReport jobReport = JobExecutor.execute(job);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(2);

        List<Batch> batches = (List<Batch>) jobReport.getResult();
        assertThat(batches).hasSize(2);

        Batch batch1 = batches.get(0);
        assertThat(batch1.getPayload()).hasSize(2);
        CSVRecord tweet = (CSVRecord) batch1.getPayload().get(0).getPayload();
        assertThat(tweet).containsExactly("1", "foo", "hello");

        tweet = (CSVRecord) batch1.getPayload().get(1).getPayload();
        assertThat(tweet).containsExactly("2", "bar", "hey");

        Batch batch2 = batches.get(1);
        assertThat(batch2.getPayload()).hasSize(1);
        tweet = (CSVRecord) batch2.getPayload().get(0).getPayload();
        assertThat(tweet).containsExactly("3", "baz", "hi");
    }

}
