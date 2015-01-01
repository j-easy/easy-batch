package org.easybatch.core.util;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;

/**
 * Convenient filter that mimics the unix grep utility: it keeps records containing the given pattern
 * instead of filtering them.
 *
 * Should be used with {@link StringRecord} type. Search is case sensitive.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class GrepFilter implements RecordFilter {

    private String pattern;

    public GrepFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean filterRecord(Record record) {
        StringRecord stringRecord = (StringRecord) record;
        String payload = stringRecord.getPayload();
        return !payload.contains(pattern);
    }

}
