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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link org.easybatch.core.filter.RecordNumberEqualsToFilter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class RecordNumberEqualsToFilterTest extends BaseRecordFilterTest {

    private RecordNumberEqualsToFilter recordNumberEqualsToFilter;

    @Test
    public void whenTheRecordNumberIsEqualToExpectedNumber_ThenItShouldBeFiltered() {
        recordNumberEqualsToFilter = new RecordNumberEqualsToFilter(1);
        assertThat(recordNumberEqualsToFilter.filterRecord(stringRecord1)).isTrue();
    }

    @Test
    public void whenTheRecordNumberIsEqualToOneOfTheExpectedNumbers_ThenItShouldBeFiltered() {
        recordNumberEqualsToFilter = new RecordNumberEqualsToFilter(1, 2);
        assertThat(recordNumberEqualsToFilter.filterRecord(stringRecord1)).isTrue();
        assertThat(recordNumberEqualsToFilter.filterRecord(stringRecord2)).isTrue();
    }

    /*
     * Test the negate behavior
     */

    @Test
    public void whenTheRecordNumberIsEqualToExpectedNumber_ThenItShouldNotBeFiltered() {
        recordNumberEqualsToFilter = new RecordNumberEqualsToFilter(true, 1);
        assertThat(recordNumberEqualsToFilter.filterRecord(stringRecord1)).isFalse();
    }

    @Test
    public void whenTheRecordNumberIsEqualToOneOfTheExpectedNumbers_ThenItShouldNotBeFiltered() {
        recordNumberEqualsToFilter = new RecordNumberEqualsToFilter(true, 1, 2);
        assertThat(recordNumberEqualsToFilter.filterRecord(stringRecord1)).isFalse();
        assertThat(recordNumberEqualsToFilter.filterRecord(stringRecord2)).isFalse();
    }

}
