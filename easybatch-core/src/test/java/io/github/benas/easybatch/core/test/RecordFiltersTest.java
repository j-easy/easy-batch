package io.github.benas.easybatch.core.test;

import io.github.benas.easybatch.core.filter.*;
import io.github.benas.easybatch.core.util.StringRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for common record filters.
 *
 * @author benas (md.benhassine@gmail.com)
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
