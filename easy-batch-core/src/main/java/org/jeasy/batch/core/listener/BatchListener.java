/*
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.jeasy.batch.core.listener;

import org.jeasy.batch.core.record.Batch;

/**
 * Allow implementing classes to get notified before/after processing each batch.
 * Exception handling is done by the implementing class.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 * @param <P> type of the record's payload
 */
public interface BatchListener<P> {

    /**
     * Executed before reading each batch.
     */
    default void beforeBatchReading() {
        // no-op
    }

    /**
     * Executed after processing each batch.
     *
     * @param batch the batch of records that has been processed
     */
    default void afterBatchProcessing(final Batch<P> batch) {
        // no-op
    }

    /**
     * Executed after successfully writing each batch.
     *
     * @param batch the batch of records that has been written
     */
    default void afterBatchWriting(final Batch<P> batch) {
        // no-op
    }

    /**
     * Executed when an error occurs during writing each batch.
     *
     * @param batch   the batch attempted to be written
     * @param throwable the error occurred
     */
    default void onBatchWritingException(final Batch<P> batch, Throwable throwable) {
        // no-op
    }

}
