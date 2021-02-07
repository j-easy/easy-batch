/*
 * The MIT License
 *
 *   Copyright (c) 2021, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.jeasy.batch.core.filter;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilteredRecordsCollectorTest {

	@Mock
	private RecordFilter<String> delegate;
	@Mock
	private StringRecord record1, record2, record3, record4;

	private FilteredRecordsCollector<String> collector;

	@Before
	public void setUp() throws Exception {
		collector = new FilteredRecordsCollector<>(delegate);
	}

	@Test
	public void testFilteredRecordsCollecting() {
		when(delegate.processRecord(record1)).thenReturn(record1);
		when(delegate.processRecord(record2)).thenReturn(null);
		when(delegate.processRecord(record3)).thenReturn(record3);
		when(delegate.processRecord(record4)).thenReturn(null);

		collector.processRecord(record1);
		collector.processRecord(record2);
		collector.processRecord(record3);
		collector.processRecord(record4);

		List<Record<String>> filteredRecords = collector.getFilteredRecords();
		Assertions.assertThat(filteredRecords).containsExactly(record2, record4);
	}

}
