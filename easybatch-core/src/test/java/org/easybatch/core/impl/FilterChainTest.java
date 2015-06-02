package org.easybatch.core.impl;

import org.easybatch.core.api.EventManager;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;
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