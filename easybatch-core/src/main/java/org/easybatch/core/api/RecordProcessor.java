/*
 * The MIT License
 *
 *   Copyright (c) 2014, Mahmoud Ben Hassine (md.benhassine@gmail.com)
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

package org.easybatch.core.api;

/**
 * An interface for record processor.
 * This will be used by easy batch to process each input record already mapped to a domain object type.
 *
 * @param <T> The target domain object type.
 * @param <R> The batch result (if any) type.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public interface RecordProcessor<T, R> {

    /**
     * Process a record.
     * @param record the record to process.
     * @throws Exception thrown if an exception occurs during record processing
     */
    void processRecord(final T record) throws Exception;

    /**
     * Returns batch execution result. This is useful when one need to return some computation results at the end of batch execution.
     * @return batch execution result
     */
    R getEasyBatchResult();

}
