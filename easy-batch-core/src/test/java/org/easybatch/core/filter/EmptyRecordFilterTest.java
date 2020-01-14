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
package org.easybatch.core.filter;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmptyRecordFilterTest {

    @Mock
    private StringRecord stringRecord;

    private RecordFilter<StringRecord> recordFilter;

    @Before
    public void setUp() throws Exception {
        recordFilter = new EmptyRecordFilter();
    }

    @Test
    public void testFilterEmptyRecord() {
        when(stringRecord.getPayload()).thenReturn("");
        assertThat(recordFilter.processRecord(stringRecord)).isNull();
    }

    @Test
    public void testFilterNonEmptyRecord() {
        when(stringRecord.getPayload()).thenReturn("foo");
        assertThat(recordFilter.processRecord(stringRecord)).isEqualTo(stringRecord);
    }

    @Test
    public void integrationTest() throws Exception {
        String dataSource = "foo" + LINE_SEPARATOR + "" + LINE_SEPARATOR + "bar" + LINE_SEPARATOR + "" + LINE_SEPARATOR;

        RecordCollector<String> recordCollector = new RecordCollector<>();
        Job job = aNewJob()
                .reader(new StringRecordReader(dataSource))
                .filter(new EmptyRecordFilter())
                .processor(recordCollector)
                .build();

        JobReport jobReport = newSingleThreadExecutor().submit(job).get();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(4);
        assertThat(jobReport.getMetrics().getFilterCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(2);

        List<Record<String>> records = recordCollector.getRecords();
        assertThat(records).extracting("payload").containsExactly("foo", "bar");
    }
}
