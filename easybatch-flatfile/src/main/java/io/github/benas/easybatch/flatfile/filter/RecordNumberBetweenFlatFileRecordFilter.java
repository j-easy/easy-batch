/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.flatfile.filter;

import io.github.benas.easybatch.core.api.Record;
import io.github.benas.easybatch.core.api.RecordFilter;

/**
 * A {@link io.github.benas.easybatch.core.api.RecordFilter} that filters flat file records
 * if their number is outside a given range.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class RecordNumberBetweenFlatFileRecordFilter implements RecordFilter {

    /**
     * Record number range lower bound.
     */
    private long lowerBound;

    /**
     * Record number range higher bound.
     */
    private long higherBound;

    /**
     * @param lowerBound Record number range lower bound.
     * @param higherBound Record number range higher bound.
     */
    public RecordNumberBetweenFlatFileRecordFilter(final long lowerBound, final long higherBound) {
        this.lowerBound = lowerBound;
        this.higherBound = higherBound;
    }

    /**
     * {@inheritDoc}
     */
    public boolean filterRecord(final Record record) {
        return record.getNumber() > lowerBound && record.getNumber() < higherBound;
    }

}
