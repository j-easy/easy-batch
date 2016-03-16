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

package org.easybatch.core.marshaller;

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Marshal a list of objects using a delegate {@link RecordMarshaller}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BatchMarshaller implements RecordMarshaller<Batch, Batch> {

    private RecordMarshaller delegate;

    /**
     * Create a {@link BatchMarshaller}.
     *
     * @param delegate the delegate {@link RecordMarshaller}
     */
    public BatchMarshaller(final RecordMarshaller delegate) {
        Utils.checkNotNull(delegate, "record marshaller");
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Batch processRecord(final Batch batch) throws RecordMarshallingException {
        List<Record> marshalledRecords = new ArrayList<>();
        for (Record record : batch.getPayload()) {
            marshalledRecords.add(delegate.processRecord(record));
        }
        return new Batch(batch.getHeader(), marshalledRecords);
    }
}
