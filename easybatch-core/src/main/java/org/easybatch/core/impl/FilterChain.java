package org.easybatch.core.impl;

import org.easybatch.core.api.EventManager;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;

import java.util.List;

/**
 * The list of filters to apply for each record.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
class FilterChain {

    private List<RecordFilter> filters;

    private EventManager eventManager;

    FilterChain(List<RecordFilter> filters, EventManager eventManager) {
        this.filters = filters;
        this.eventManager = eventManager;
    }

    public void addRecordFilter(final RecordFilter recordFilter) {
        filters.add(recordFilter);
    }

    public boolean filterRecord(final Record currentRecord) {
        eventManager.fireBeforeFilterRecord(currentRecord);
        boolean filtered = false;
        for (RecordFilter recordFilter : filters) {
            if (recordFilter.filterRecord(currentRecord)) {
                filtered = true;
                break;
            }
        }
        eventManager.fireAfterFilterRecord(currentRecord, filtered);
        return filtered;
    }
}
