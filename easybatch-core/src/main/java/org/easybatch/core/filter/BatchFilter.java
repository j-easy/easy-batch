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

package org.easybatch.core.filter;

import org.easybatch.core.record.Batch;
import org.easybatch.core.util.Utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.isBatch;
import static org.easybatch.core.util.Utils.isCollection;

/**
 * Filter records from a {@link Batch} using a delegate {@link RecordFilter}.
 * 
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class BatchFilter implements RecordFilter<Object> {

    private static final Logger LOGGER = Logger.getLogger(BatchFilter.class.getName());

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
    @SuppressWarnings("unchecked")
    public Object processRecord(Object batch) {
        Iterator iterator = getIterator(batch);
        filterRecord(iterator);
        return batch;
    }

    private void filterRecord(final Iterator iterator) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                Object record = iterator.next();
                if (delegate.processRecord(record) == null) {
                    iterator.remove();
                }
            }
        }
    }

    private Iterator getIterator(final Object batch) {
        Iterator iterator = null;
        if (isBatch(batch)) {
            iterator = ((Batch) batch).getPayload().iterator();
        } else if (isCollection(batch)) {
            iterator = ((Collection) batch).iterator();
        } else {
            LOGGER.warning("BatchFilter accepts only " + Batch.class.getName() + " or " + Collection.class.getName() + " types");
        }
        return iterator;
    }
}
