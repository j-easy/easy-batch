/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import org.easybatch.core.record.StringRecord;

/**
 * A {@link RecordFilter} that filters string records starting with one of the given prefixes.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class StartWithStringRecordFilter implements RecordFilter<StringRecord> {

    /**
     * Prefixes that causes the record to be filtered.
     */
    private String[] prefixes;

    /**
     * Create a {@link StartWithStringRecordFilter} instance.
     *
     * @param prefixes prefixes that cause the record to be filtered.
     */
    public StartWithStringRecordFilter(final String... prefixes) {
        this.prefixes = prefixes;
    }

    public StringRecord processRecord(final StringRecord record) {
        String payload = record.getPayload();
        for (String prefix : prefixes) {
            if (payload.startsWith(prefix)) {
                return null;
            }
        }
        return record;
    }

}
