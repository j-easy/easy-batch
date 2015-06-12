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
 * A {@link org.easybatch.core.api.RecordFilter} that filters flat file records based on their number.<br/>
 * The parameter negate can be set to true to inverse this behavior :
 * this filter will filter records which number is not equal to any of the given numbers.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class RecordNumberEqualsToFilter implements RecordFilter {

    /**
     * Record numbers that causes the record to be filtered.
     */
    private long[] numbers;

    /**
     * Parameter to filter a record if its number is not equal to one of the given numbers.
     */
    private boolean negate;

    /**
     * @param numbers record numbers that cause the record to be filtered.
     */
    public RecordNumberEqualsToFilter(final long... numbers) {
        this(false, numbers);
    }

    /**
     * @param negate  true if the filter should filter records which number is not equal to any of the given numbers.
     * @param numbers record numbers that cause the record to be filtered.
     */
    public RecordNumberEqualsToFilter(final boolean negate, final long... numbers) {
        this.negate = negate;
        this.numbers = numbers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean filterRecord(final Record record) {
        for (long number : numbers) {
            if (record.getHeader().getNumber() == number) {
                return !negate;
            }
        }
        return false;
    }

}
