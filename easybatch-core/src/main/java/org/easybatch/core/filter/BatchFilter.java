/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core.filter;

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.util.Utils;

import java.util.Iterator;

/**
 * Filter records from a {@link Batch} using a delegate {@link RecordFilter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BatchFilter implements RecordFilter<Batch> {

    private RecordFilter delegate;

    /**
     * Create a {@link BatchFilter}.
     *
     * @param delegate the delegate {@link RecordFilter}
     */
    public BatchFilter(final RecordFilter delegate) {
        Utils.checkNotNull(delegate, "record filter");
        this.delegate = delegate;
    }

    @Override
    public Batch processRecord(Batch batch) {
        Iterator<Record> iterator = batch.getPayload().iterator();
        filterRecord(iterator);
        return batch;
    }

    @SuppressWarnings("unchecked")
    private void filterRecord(final Iterator<Record> iterator) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                Record record = iterator.next();
                if (delegate.processRecord(record) == null) {
                    iterator.remove();
                }
            }
        }
    }

}
