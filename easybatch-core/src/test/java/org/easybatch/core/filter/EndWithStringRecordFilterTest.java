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
 * Test class for {@link EndWithStringRecordFilter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class EndWithStringRecordFilterTest {

    @Mock
    private StringRecord stringRecord;
    
    private EndWithStringRecordFilter endWithStringRecordFilter;

    /*
    * Test the regular behavior
    */

    @Test
    public void whenTheRecordEndsWithExpectedSuffix_ThenItShouldBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content_suffix");
        endWithStringRecordFilter = new EndWithStringRecordFilter("suffix");
        assertThat(endWithStringRecordFilter.filterRecord(stringRecord)).isTrue();
    }

    @Test
    public void whenTheRecordDoesNotEndWithExpectedSuffix_ThenItShouldNotBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content");
        endWithStringRecordFilter = new EndWithStringRecordFilter("suffix");
        assertThat(endWithStringRecordFilter.filterRecord(stringRecord)).isFalse();
    }

    @Test
    public void whenTheRecordEndsWithOneOfTheExpectedSuffixes_ThenItShouldBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content_suffix1");
        endWithStringRecordFilter = new EndWithStringRecordFilter("suffix1", "suffix2");
        assertThat(endWithStringRecordFilter.filterRecord(stringRecord)).isTrue();

        when(stringRecord.getPayload()).thenReturn("content_suffix2");
        endWithStringRecordFilter = new EndWithStringRecordFilter("suffix1", "suffix2");
        assertThat(endWithStringRecordFilter.filterRecord(stringRecord)).isTrue();
    }

    @Test
    public void whenTheRecordDoesNotEndWithOneOfTheExpectedSuffixes_ThenItShouldNotBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content");
        endWithStringRecordFilter = new EndWithStringRecordFilter("suffix1", "suffix2");
        assertThat(endWithStringRecordFilter.filterRecord(stringRecord)).isFalse();
    }

    /*
     * Test the negate behavior
     */

    @Test
    public void whenTheRecordEndsWithExpectedSuffix_ThenItShouldNotBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content_suffix");
        endWithStringRecordFilter = new EndWithStringRecordFilter(true, "suffix");
        assertThat(endWithStringRecordFilter.filterRecord(stringRecord)).isFalse();
    }

    @Test
    public void whenTheRecordDoesNoEndWithExpectedSuffix_ThenItShouldBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content");
        endWithStringRecordFilter = new EndWithStringRecordFilter(true, "suffix");
        assertThat(endWithStringRecordFilter.filterRecord(stringRecord)).isTrue();
    }

    @Test
    public void whenTheRecordEndsWithOneOfTheExpectedSuffixes_ThenItShouldNotBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content_suffix1");
        endWithStringRecordFilter = new EndWithStringRecordFilter(true, "suffix1", "suffix2");
        assertThat(endWithStringRecordFilter.filterRecord(stringRecord)).isFalse();

        when(stringRecord.getPayload()).thenReturn("content_suffix2");
        endWithStringRecordFilter = new EndWithStringRecordFilter(true, "suffix1", "suffix2");
        assertThat(endWithStringRecordFilter.filterRecord(stringRecord)).isFalse();
    }

    @Test
    public void whenTheRecordDoesNotEndWithOneOfTheExpectedSuffixes_ThenItShouldBeFiltered() {
        when(stringRecord.getPayload()).thenReturn("content");
        endWithStringRecordFilter = new EndWithStringRecordFilter(true, "suffix1", "suffix2");
        assertThat(endWithStringRecordFilter.filterRecord(stringRecord)).isTrue();
    }

}
