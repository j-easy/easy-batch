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

import org.easybatch.core.record.Record;

/**
 * A {@link RecordFilter} that filters records based on their number.
 * <p/>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class RecordNumberEqualToFilter implements RecordFilter<Record> {

    /**
     * Record numbers that causes the record to be filtered.
     */
    private long[] numbers;

    /**
     * Create a {@link RecordNumberEqualToFilter} instance.
     *
     * @param numbers record numbers that cause the record to be filtered.
     */
    public RecordNumberEqualToFilter(final long... numbers) {
        this.numbers = numbers;
    }

    @Override
    public Record processRecord(final Record record) {
        for (long number : numbers) {
            if (record.getHeader().getNumber() == number) {
                return null;
            }
        }
        return record;

    }

}
