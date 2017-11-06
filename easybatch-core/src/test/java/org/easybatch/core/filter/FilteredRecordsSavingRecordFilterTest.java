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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;

import org.easybatch.core.record.FileRecord;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FilteredRecordsSavingRecordFilterTest {

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private Record                            record;

  @Mock
  private StringRecord                      stringRecord;

  @Mock
  private File                              file;

  @Mock
  private FileRecord                        fileRecord;

  private FilteredRecordsSavingRecordFilter recordFilter;

  @Test
  public void filterNumberEqualTo() {
    recordFilter = new FilteredRecordsSavingRecordFilter(new RecordNumberEqualToFilter(2, 4));

    when(record.getHeader().getNumber()).thenReturn(1l);
    assertThat(recordFilter.processRecord(record)).isNotNull();
    commonAssertFirst();

    when(record.getHeader().getNumber()).thenReturn(2l);
    assertThat(recordFilter.processRecord(record)).isNull();
    commonAssertGeneric(1);

    when(record.getHeader().getNumber()).thenReturn(3l);
    assertThat(recordFilter.processRecord(record)).isNotNull();
    commonAssertGeneric(1);

    when(record.getHeader().getNumber()).thenReturn(4l);
    assertThat(recordFilter.processRecord(record)).isNull();
    commonAssertGeneric(2);
  }

  @Test
  public void filterStringStartWith() {
    recordFilter = new FilteredRecordsSavingRecordFilter(new StartWithStringRecordFilter("prefix"));

    when(stringRecord.getPayload()).thenReturn("Lorem");
    assertThat(recordFilter.processRecord(stringRecord)).isNotNull();
    commonAssertFirst();

    when(stringRecord.getPayload()).thenReturn("prefix_ipsum");
    assertThat(recordFilter.processRecord(stringRecord)).isNull();
    commonAssertGeneric(1);

    when(stringRecord.getPayload()).thenReturn("dolor");
    assertThat(recordFilter.processRecord(stringRecord)).isNotNull();
    commonAssertGeneric(1);

    when(stringRecord.getPayload()).thenReturn("prefix_sit");
    assertThat(recordFilter.processRecord(stringRecord)).isNull();
    commonAssertGeneric(2);
  }

  @Test
  public void filterFileExtension() {
    recordFilter = new FilteredRecordsSavingRecordFilter(new FileExtensionFilter(".txt", ".xml"));
    when(fileRecord.getPayload()).thenReturn(file);

    when(file.getName()).thenReturn("test.jpg");
    assertThat(recordFilter.processRecord(fileRecord)).isNotNull();
    commonAssertFirst();

    when(file.getName()).thenReturn("test.txt");
    assertThat(recordFilter.processRecord(fileRecord)).isNull();
    commonAssertGeneric(1);

    when(file.getName()).thenReturn("test.gif");
    assertThat(recordFilter.processRecord(fileRecord)).isNotNull();
    commonAssertGeneric(1);

    when(file.getName()).thenReturn("test.xml");
    assertThat(recordFilter.processRecord(fileRecord)).isNull();
    commonAssertGeneric(2);
  }

  private void commonAssertFirst() {
    assertThat(recordFilter.getFilteredRecords()).isNotNull();
    assertThat(recordFilter.getFilteredRecords()).isEmpty();
  }

  private void commonAssertGeneric(int expected) {
    assertThat(recordFilter.getFilteredRecords()).isNotNull();
    assertThat(recordFilter.getFilteredRecords()).isNotEmpty();
    assertThat(recordFilter.getFilteredRecordNumber()).isEqualTo(expected);
  }

}