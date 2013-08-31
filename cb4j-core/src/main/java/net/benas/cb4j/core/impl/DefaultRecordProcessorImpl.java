/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.core.impl;

import net.benas.cb4j.core.api.BatchResultHolder;
import net.benas.cb4j.core.api.RecordProcessingException;
import net.benas.cb4j.core.api.RecordProcessor;

/**
 * Abstract dummy implementation class for the {@link RecordProcessor} interface.<br/>
 *
 * This class can be extended to override only the {@link RecordProcessor#processRecord(T)}
 * method when there is no requirement to implement {@link RecordProcessor#preProcessRecord(T)}
 * and {@link RecordProcessor#postProcessRecord(T)} methods.
 *
 * When you need to get a batch result at the end of execution, you should override the
 * {@link RecordProcessor#getBatchResultHolder()} to return a concrete implementation
 * of {@link net.benas.cb4j.core.api.BatchResultHolder}.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public abstract class DefaultRecordProcessorImpl<T> implements RecordProcessor<T> {

    /**
     * Batch execution result holder.
     */
    protected BatchResultHolder<?> batchResultHolder;

    /**
     * No-op implementation.
     * @param typedRecord the record to pre process
     * @throws RecordProcessingException thrown if an exception occurs during record pre-processing
     */
    public void preProcessRecord(T typedRecord) throws RecordProcessingException {
    }

    /**
     * No-op implementation.
     * @param typedRecord the record to process.
     * @throws RecordProcessingException thrown if an exception occurs during record processing
     */
    public void processRecord(T typedRecord) throws RecordProcessingException {
    }

    /**
     * No-op implementation.
     * @param typedRecord the record to post process
     * @throws RecordProcessingException thrown if an exception occurs during record post-processing
     */
    public void postProcessRecord(T typedRecord) throws RecordProcessingException {
    }

    /**
     * Returns batch execution result. This is useful when you need to return some computation results at the end of batch execution.
     * @return batch execution result
     */
    public BatchResultHolder<?> getBatchResultHolder() {
        return batchResultHolder;
    }

}
