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

package org.easybatch.core.mapper;

import java.util.ArrayList;
import java.util.List;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.util.Utils;

/**
 * Map a {@link Batch} of records to a list of domain objects using a delegate {@link RecordMapper}.
 *
 * @param <T> The target domain object type.
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class BatchMapper<T> implements RecordMapper<Batch, List<T>> {

    private RecordMapper delegate;

    /**
     * Create a {@link BatchMapper}.
     *
     * @param delegate the delegate {@link RecordMapper}
     */
    public BatchMapper(final RecordMapper delegate) {
        Utils.checkNotNull(delegate, "record mapper");
        this.delegate = delegate;
    }

    @Override
    public List<T> processRecord(final Batch batch) throws RecordMappingException {
        List<T> records = new ArrayList<>();
        for (Record record : batch.getPayload()) {
            records.add((T) delegate.processRecord(record));
        }
        return records;
    }
}
