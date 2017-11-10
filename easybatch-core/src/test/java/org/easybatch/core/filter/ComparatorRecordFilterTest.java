/**
 * The MIT License
 *
 *   Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComparatorRecordFilterTest {

  @Mock
  private Record<String>               stringRecord;

  private StringComparatorRecordFilter recordFilter;
  private List<String>                 refRecord = new ArrayList<>();

  @Before
  public void setUp() throws Exception {
    refRecord.add("Lorem");
    refRecord.add("ipsum");
    refRecord.add("dolor");
    refRecord.add("sit");
    refRecord.add("amet");

    recordFilter = new StringComparatorRecordFilter(refRecord);
  }

  @Test
  public void testFilterEmptyRecord() {
    when(stringRecord.getPayload()).thenReturn("");
    assertThat(recordFilter.processRecord(stringRecord)).isNull();
  }

  @Test
  public void testFilterNonEmptyExistingRecord() {
    when(stringRecord.getPayload()).thenReturn("ipsum");
    assertThat(recordFilter.processRecord(stringRecord)).isEqualTo(stringRecord);
  }

  @Test
  public void testFilterNonEmptyNotExistingRecord() {
    when(stringRecord.getPayload()).thenReturn("consectetur");
    assertThat(recordFilter.processRecord(stringRecord)).isNull();
  }

  @Test
  public void integrationTest() throws Exception {
    String dataSource = "ipsum" + LINE_SEPARATOR + "" + LINE_SEPARATOR + "consectetur" + LINE_SEPARATOR + "" + LINE_SEPARATOR + "Lorem" + LINE_SEPARATOR;

    RecordCollector<String> recordCollector = new RecordCollector<>();
    Job job = aNewJob().reader(new StringRecordReader(dataSource)).filter(new StringComparatorRecordFilter(refRecord)).processor(recordCollector).build();
    JobReport jobReport = newSingleThreadExecutor().submit(job).get();

    assertThat(jobReport).isNotNull();
    assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(5);
    assertThat(jobReport.getMetrics().getFilterCount()).isEqualTo(3);
    assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(2);

    List<Record<String>> records = recordCollector.getRecords();
    assertThat(records).extracting("payload").containsExactly("ipsum", "Lorem");
  }

  public class StringComparatorRecordFilter extends ComparatorRecordFilter<List<String>, String> {
    public StringComparatorRecordFilter(List<String> pilotData) {
      super(pilotData);
    }

    @Override
    public Record<String> compare(Record<String> record) {
      String payload = record.getPayload();
      return (refRecord.contains(payload)) ? record : null;
    }
  }

}