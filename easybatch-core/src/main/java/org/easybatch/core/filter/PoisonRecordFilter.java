package org.easybatch.core.filter;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;
import org.easybatch.core.util.PoisonRecord;

/**
 * Poison records are used as End-Of-Stream signals, usually they have no added value and should be filtered.
 *
 * This filter is used to filter such records.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class PoisonRecordFilter implements RecordFilter {

    @Override
    public boolean filterRecord(Record record) {
        return record instanceof PoisonRecord;
    }

}
