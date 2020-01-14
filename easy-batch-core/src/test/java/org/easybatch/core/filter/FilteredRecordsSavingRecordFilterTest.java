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

import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.reader.IterableRecordReader;
import org.easybatch.core.record.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FilteredRecordsSavingRecordFilterTest {

    @Mock
    private Record record;
    @Mock
    private RecordFilter delegate;

    private FilteredRecordsSavingRecordFilter recordFilter;

    @Test
    public void whenFilteringRecords_thenDelegateFilterShouldBeCalled() throws Exception {
        // given
        recordFilter = new FilteredRecordsSavingRecordFilter(delegate);

        // when
        recordFilter.processRecord(record);

        // then
        verify(delegate).processRecord(record);
    }

    @Test
    public void filteredRecordsShouldBeSaved() {
        // given
        List<Integer> dataSource = Arrays.asList(1, 2, 3, 4);
        recordFilter = new FilteredRecordsSavingRecordFilter(new RecordNumberEqualToFilter(2, 4));

        // when
        JobBuilder.aNewJob()
                .reader(new IterableRecordReader(dataSource))
                .filter(recordFilter)
                .build()
                .call();

        // then
        List<Record> filteredRecords = recordFilter.getFilteredRecords();
        assertThat(filteredRecords).hasSize(2);
        assertThat(filteredRecords.get(0).getHeader().getNumber()).isEqualTo(2);
        assertThat(filteredRecords.get(1).getHeader().getNumber()).isEqualTo(4);
    }

}
