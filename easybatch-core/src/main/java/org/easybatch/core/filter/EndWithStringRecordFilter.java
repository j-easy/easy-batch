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

import org.easybatch.core.api.RecordFilter;
import org.easybatch.core.api.RecordFilteringException;
import org.easybatch.core.record.StringRecord;

/**
 * A {@link org.easybatch.core.api.RecordFilter} that filters string records ending with one of the given suffixes.
 * <p/>
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class EndWithStringRecordFilter implements RecordFilter<StringRecord> {

    /**
     * Suffixes that causes the record to be filtered.
     */
    private String[] suffixes;

    /**
     * @param suffixes suffixes that cause the record to be filtered.
     */
    public EndWithStringRecordFilter(final String... suffixes) {
        this.suffixes = suffixes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringRecord processRecord(final StringRecord record) throws RecordFilteringException {
        String payload = record.getPayload();
        for (String prefix : suffixes) {
            if (payload.endsWith(prefix)) {
                throw new RecordFilteringException();
            }
        }
        return record;
    }

}
