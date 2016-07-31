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

package org.easybatch.core.validator;

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.util.Utils;

/**
 * Validate a batch of records using a delegate {@link RecordValidator}.
 * <p/>
 * <p>A batch of records is valid if all its records are valid.</p>
 * <p>A batch of records is invalid if one of its records is invalid.</p>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BatchValidator implements RecordValidator<Batch> {

    private RecordValidator recordValidator;

    /**
     * Create a batch validator.
     *
     * @param recordValidator the delegate {@link RecordValidator}
     */
    public BatchValidator(final RecordValidator recordValidator) {
        Utils.checkNotNull(recordValidator, "record validator");
        this.recordValidator = recordValidator;
    }

    /**
     * Validate a batch of records.
     *
     * @param batch the batch to validate.
     * @return the batch as is if it should continue in the pipeline
     * @throws RecordValidationException thrown if the batch is not valid and should be rejected
     */
    @Override
    @SuppressWarnings("unchecked")
    public Batch processRecord(final Batch batch) throws RecordValidationException {
        for (Record record : batch.getPayload()) {
            recordValidator.processRecord(record);
        }
        return batch;
    }
}
