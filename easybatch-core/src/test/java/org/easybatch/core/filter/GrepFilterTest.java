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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrepFilterTest {

    private GrepFilter grepFilter;

    @Mock
    private StringRecord record;

    @Before
    public void setUp() {
        grepFilter = new GrepFilter("java");
    }

    @Test
    public void whenRecordContainsPattern_ThenItShouldNotBeFiltered() {
        when(record.getPayload()).thenReturn("java rocks!");
        assertThat(grepFilter.processRecord(record)).isEqualTo(record);
    }

    @Test
    public void whenRecordDoesNotContainPattern_ThenItShouldBeFiltered() {
        when(record.getPayload()).thenReturn("c++ ..");
        assertThat(grepFilter.processRecord(record)).isNull();
    }

    @Test
    public void patternLookupShouldBeCaseSensitive() {
        when(record.getPayload()).thenReturn("JAVA rocks!");
        assertThat(grepFilter.processRecord(record)).isNull();
    }

}
