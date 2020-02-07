/*
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
package org.jeasy.batch.core.filter;

import org.jeasy.batch.core.record.StringRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StartsWithStringRecordFilterTest {

	@Mock
	private StringRecord record;

	private StartsWithStringRecordFilter filter = new StartsWithStringRecordFilter("foo", "bar");

	@Test
	public void recordsStartingWithOneOfThePrefixesShouldBeFiltered() {
		when(record.getPayload()).thenReturn("foofoo");
		assertThat(filter.processRecord(record)).isNull();
		when(record.getPayload()).thenReturn("barbar");
		assertThat(filter.processRecord(record)).isNull();
	}

	@Test
	public void recordsNotStartingWithOneOfTheSuffixesShouldNotNotBeFiltered() {
		when(record.getPayload()).thenReturn("fodd");
		assertThat(filter.processRecord(record)).isEqualTo(record);
		when(record.getPayload()).thenReturn("bazr");
		assertThat(filter.processRecord(record)).isEqualTo(record);
	}

}
