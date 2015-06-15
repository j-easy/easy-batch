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
package org.easybatch.core.impl;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;
import org.easybatch.core.api.event.EventManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link FilterChain}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class FilterChainTest {

    @Mock
    Record record;

    @Mock
    private RecordFilter filter1, filter2;

    @Mock
    private EventManager eventManager;

    private FilterChain filterChain;

    @Before
    public void setUp() throws Exception {
        when(eventManager.fireBeforeRecordFiltering(record)).thenReturn(record);
    }

    @Test
    public void theFilterChainShouldReturnTheSameResultAsTheRecordFilter() throws Exception {

        filterChain = new FilterChain(Collections.singletonList(filter1), eventManager);

        when(filter1.filterRecord(record)).thenReturn(true);
        assertThat(filterChain.filterRecord(record)).isTrue();

        when(filter1.filterRecord(record)).thenReturn(false);
        assertThat(filterChain.filterRecord(record)).isFalse();

    }

    @Test
    public void whenMultipleFilters_thenShouldStopOnFirstFilterThatReturnsTrue() throws Exception {

        filterChain = new FilterChain(Arrays.asList(filter1, filter2), eventManager);

        when(filter1.filterRecord(record)).thenReturn(true);
        when(filter2.filterRecord(record)).thenReturn(false);

        assertThat(filterChain.filterRecord(record)).isTrue();

        verifyZeroInteractions(filter2);
    }

    @Test
    public void whenMultipleFilters_thenShouldCallAllFilters() throws Exception {

        filterChain = new FilterChain(Arrays.asList(filter1, filter2), eventManager);

        when(filter1.filterRecord(record)).thenReturn(false);
        when(filter2.filterRecord(record)).thenReturn(true);

        assertThat(filterChain.filterRecord(record)).isTrue();
    }
}