package org.easybatch.core.filter;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;
import org.easybatch.core.record.StringRecord;

/**
 * Convenient filter that mimics the unix grep utility: it keeps records containing the given pattern
 * instead of filtering them.
 * <p/>
 * Should be used with {@link org.easybatch.core.record.StringRecord} type. Search is case sensitive.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class GrepFilter implements RecordFilter {

    private String pattern;

    private boolean negate;

    public GrepFilter(String pattern) {
        this.pattern = pattern;
    }

    public GrepFilter(String pattern, boolean negate) {
        this.pattern = pattern;
        this.negate = negate;
    }

    @Override
    public boolean filterRecord(Record record) {
        StringRecord stringRecord = (StringRecord) record;
        String payload = stringRecord.getPayload();
        boolean result = doFilterRecord(payload);
        return negate ? !result : result;
    }

    private boolean doFilterRecord(String payload) {
        return !payload.contains(pattern);
    }

}
