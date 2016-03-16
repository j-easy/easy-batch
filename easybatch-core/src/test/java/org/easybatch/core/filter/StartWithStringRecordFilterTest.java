/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.filter;

import org.easybatch.core.record.StringRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StartWithStringRecordFilterTest {

    @Mock
    private StringRecord stringRecord;

    private StartWithStringRecordFilter startWithStringRecordFilter;

    @Test
    public void whenTheRecordStartsWithExpectedPrefix_ThenItShouldBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("prefix_content");
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix");
        assertThat(startWithStringRecordFilter.processRecord(stringRecord)).isNull();
    }

    @Test
    public void whenTheRecordDoesNotStartWithExpectedPrefix_ThenItShouldNotBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content");
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix");
        assertThat(startWithStringRecordFilter.processRecord(stringRecord)).isEqualTo(stringRecord);
    }

    @Test
    public void whenTheRecordStartsWithOneOfTheExpectedPrefixes_ThenItShouldBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("prefix1_content");
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix1", "prefix2");
        assertThat(startWithStringRecordFilter.processRecord(stringRecord)).isNull();
    }

    @Test
    public void whenTheRecordDoesNotStartWithOneOfTheExpectedPrefixes_ThenItShouldNotBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content");
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix1", "prefix2");
        assertThat(startWithStringRecordFilter.processRecord(stringRecord)).isEqualTo(stringRecord);
    }

}
