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

package org.easybatch.core.processor;

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Process records in batches.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BatchProcessor implements RecordProcessor<Batch, Batch> {

    private RecordProcessor recordProcessor;

    /**
     * Create a batch processor.
     *
     * @param recordProcessor the delegate {@link RecordProcessor}
     */
    public BatchProcessor(final RecordProcessor recordProcessor) {
        Utils.checkNotNull(recordProcessor, "record processor");
        this.recordProcessor = recordProcessor;
    }

    /**
     * Process a batch of records.
     *
     * @param batch the batch to process.
     * @return the processed batch, may be of another type of the input batch
     * @throws RecordProcessingException thrown if an exception occurs during batch processing
     */
    @Override
    @SuppressWarnings("unchecked")
    public Batch processRecord(Batch batch) throws RecordProcessingException {
        List<Record> processedRecords = new ArrayList<>();
        for (Record record : batch.getPayload()) {
            processedRecords.add(recordProcessor.processRecord(record));
        }
        return new Batch(batch.getHeader(), processedRecords);
    }
}
