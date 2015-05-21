/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.easybatch.core.record.StringRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link StartWithStringRecordFilter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class StartWithStringRecordFilterTest {

    @Mock
    private StringRecord stringRecord;

    private StartWithStringRecordFilter startWithStringRecordFilter;

    /*
    * Test the regular behavior
    */

    @Test
    public void whenTheRecordStartsWithExpectedPrefix_ThenItShouldBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("prefix_content");
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord)).isTrue();
    }

    @Test
    public void whenTheRecordDoesNotStartWithExpectedPrefix_ThenItShouldNotBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content");
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord)).isFalse();
    }

    @Test
    public void whenTheRecordStartsWithOneOfTheExpectedPrefixes_ThenItShouldBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("prefix1_content");
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix1", "prefix2");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord)).isTrue();

        when(stringRecord.getPayload()).thenReturn("prefix2_content");
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix1", "prefix2");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord)).isTrue();
    }

    @Test
    public void whenTheRecordDoesNotStartWithOneOfTheExpectedPrefixes_ThenItShouldNotBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content");
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix1", "prefix2");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord)).isFalse();
    }

    /*
     * Test the negate behavior
     */

    @Test
    public void whenTheRecordStartsWithExpectedPrefix_ThenItShouldNotBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("prefix_content");
        startWithStringRecordFilter = new StartWithStringRecordFilter(true, "prefix");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord)).isFalse();
    }

    @Test
    public void whenTheRecordDoesNoStartWithExpectedPrefix_ThenItShouldBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content");
        startWithStringRecordFilter = new StartWithStringRecordFilter(true, "prefix");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord)).isTrue();
    }

    @Test
    public void whenTheRecordStartsWithOneOfTheExpectedPrefixes_ThenItShouldNotBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("prefix1_content");
        startWithStringRecordFilter = new StartWithStringRecordFilter(true, "prefix1", "prefix2");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord)).isFalse();

        when(stringRecord.getPayload()).thenReturn("prefix2_content");
        startWithStringRecordFilter = new StartWithStringRecordFilter(true, "prefix1", "prefix2");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord)).isFalse();
    }

    @Test
    public void whenTheRecordDoesNotStartWithOneOfTheExpectedPrefixes_ThenItShouldBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content");
        startWithStringRecordFilter = new StartWithStringRecordFilter(true, "prefix1", "prefix2");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord)).isTrue();
    }

}
