/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.core.impl;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;
import org.easybatch.core.api.event.EventManager;

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
        eventManager.fireBeforeRecordFiltering(currentRecord);
        boolean filtered = false;
        for (RecordFilter recordFilter : filters) {
            if (recordFilter.filterRecord(currentRecord)) {
                filtered = true;
                break;
            }
        }
        eventManager.fireAfterRecordFiltering(currentRecord, filtered);
        return filtered;
    }
}
