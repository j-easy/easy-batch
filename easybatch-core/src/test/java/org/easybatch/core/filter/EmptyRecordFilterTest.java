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

package org.easybatch.core.filter;

import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

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
    @SuppressWarnings("unchecked")
    public void integrationTest() throws Exception {
        String dataSource = "foo" + LINE_SEPARATOR + "" + LINE_SEPARATOR + "bar" + LINE_SEPARATOR + "" + LINE_SEPARATOR;

        JobReport jobReport = aNewJob()
                .reader(new StringRecordReader(dataSource))
                .filter(new EmptyRecordFilter())
                .processor(new RecordCollector())
                .call();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(4);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(2);

        List<StringRecord> records = (List<StringRecord>) jobReport.getResult();
        assertThat(records).extracting("payload").containsExactly("foo", "bar");
    }
}
