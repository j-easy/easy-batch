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

package org.easybatch.core.test;

import org.easybatch.core.filter.*;
import org.easybatch.core.util.StringRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for common record filters.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class RecordFiltersTest {

    private StartsWithStringRecordFilter startsWithStringRecordFilter;
    private EndsWithStringRecordFilter endsWithStringRecordFilter;
    private RecordNumberEqualsToRecordFilter recordNumberEqualsToRecordFilter;
    private RecordNumberGreaterThanRecordFilter recordNumberGreaterThanRecordFilter;
    private RecordNumberLowerThanRecordFilter recordNumberLowerThanRecordFilter;
    private RecordNumberInsideRangeRecordFilter recordNumberInsideRangeRecordFilter;
    private RecordNumberOutsideRangeRecordFilter recordNumberOutsideRangeRecordFilter;


    private StringRecord stringRecord;

    @Before
    public void setUp() throws Exception {
        startsWithStringRecordFilter = new StartsWithStringRecordFilter("prefix");
        endsWithStringRecordFilter = new EndsWithStringRecordFilter("suffix");
        recordNumberEqualsToRecordFilter = new RecordNumberEqualsToRecordFilter(1);
        recordNumberGreaterThanRecordFilter = new RecordNumberGreaterThanRecordFilter(0);
        recordNumberLowerThanRecordFilter = new RecordNumberLowerThanRecordFilter(2);
        recordNumberInsideRangeRecordFilter = new RecordNumberInsideRangeRecordFilter(0, 2);
        recordNumberOutsideRangeRecordFilter = new RecordNumberOutsideRangeRecordFilter(0, 2);
        stringRecord = new StringRecord(1,"prefix_content_suffix");
    }

    @Test
    public void testRecordFiltering() {
        Assert.assertEquals(true, recordNumberEqualsToRecordFilter.filterRecord(stringRecord));
        Assert.assertEquals(true, recordNumberGreaterThanRecordFilter.filterRecord(stringRecord));
        Assert.assertEquals(true, recordNumberLowerThanRecordFilter.filterRecord(stringRecord));
        Assert.assertEquals(true, recordNumberInsideRangeRecordFilter.filterRecord(stringRecord));
        Assert.assertEquals(false, recordNumberOutsideRangeRecordFilter.filterRecord(stringRecord));
        Assert.assertEquals(true, startsWithStringRecordFilter.filterRecord(stringRecord));
        Assert.assertEquals(true, endsWithStringRecordFilter.filterRecord(stringRecord));
    }

    @Test
    public void testNegateRecordFiltering() {

        startsWithStringRecordFilter = new StartsWithStringRecordFilter(true, "prefix");
        endsWithStringRecordFilter = new EndsWithStringRecordFilter(true, "suffix");
        recordNumberEqualsToRecordFilter = new RecordNumberEqualsToRecordFilter(true, 1);

        Assert.assertEquals(false, recordNumberEqualsToRecordFilter.filterRecord(stringRecord));
        Assert.assertEquals(false, startsWithStringRecordFilter.filterRecord(stringRecord));
        Assert.assertEquals(false, endsWithStringRecordFilter.filterRecord(stringRecord));
    }

}
