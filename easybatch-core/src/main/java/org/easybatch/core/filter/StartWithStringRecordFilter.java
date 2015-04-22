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

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;

/**
 * A {@link org.easybatch.core.api.RecordFilter} that filters string records starting with one of the given prefixes.<br/>
 * The parameter negate can be set to true to inverse this behavior :
 * this filter will filter records that do not start with one of the given prefixes.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class StartWithStringRecordFilter implements RecordFilter {

    /**
     * Prefixes that causes the record to be filtered.
     */
    private String[] prefixes;

    /**
     * Parameter to filter a record if it does not start with one of the given prefixes.
     */
    private boolean negate;

    /**
     * @param prefixes prefixes that cause the record to be filtered.
     */
    public StartWithStringRecordFilter(final String... prefixes) {
        this(false, prefixes);
    }

    /**
     * @param negate   true if the filter should filter records that do not start with any of the given prefixes.
     * @param prefixes prefixes that cause the record to be filtered.
     */
    public StartWithStringRecordFilter(final boolean negate, final String... prefixes) {
        this.negate = negate;
        this.prefixes = prefixes;
    }

    /**
     * {@inheritDoc}
     */
    public boolean filterRecord(final Record record) {
        String payload = (String) record.getPayload();
        for (String prefix : prefixes) {
            if (payload.startsWith(prefix)) {
                return !negate;
            }
        }
        return false;
    }

}
