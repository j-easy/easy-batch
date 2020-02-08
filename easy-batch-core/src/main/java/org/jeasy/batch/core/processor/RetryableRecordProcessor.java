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
package org.jeasy.batch.core.processor;

import java.util.concurrent.Callable;

import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.retry.RetryPolicy;
import org.jeasy.batch.core.retry.RetryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decorator that makes a {@link RecordProcessor} retryable whenever an exception
 * is thrown while processing a record. This decorator is useful in case of a 
 * transient error where a retry might succeed.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class RetryableRecordProcessor<I extends Record, O extends Record> implements RecordProcessor<I, O> {

    private RecordProcessor<I, O> delegate;
    private RecordProcessingTemplate recordProcessingTemplate;

    /**
     * Create a new {@link RetryableRecordProcessor}.
     *
     * @param delegate record processor
     * @param retryPolicy to apply
     */
    public RetryableRecordProcessor(RecordProcessor delegate, RetryPolicy retryPolicy) {
        this.delegate = delegate;
        this.recordProcessingTemplate = new RecordProcessingTemplate(retryPolicy);
    }

    @Override
    public Record processRecord(Record record) throws Exception {
        return recordProcessingTemplate.execute(new RecordProcessingCallable(delegate, record));
    }

    private static class RecordProcessingCallable implements Callable<Record> {

        private RecordProcessor recordProcessor;

        private Record record;

        RecordProcessingCallable(RecordProcessor recordProcessor, Record record) {
            this.recordProcessor = recordProcessor;
            this.record = record;
        }

        @Override
        public Record call() throws Exception {
            return recordProcessor.processRecord(record);
        }

    }

    private static class RecordProcessingTemplate extends RetryTemplate {

        private final Logger LOGGER = LoggerFactory.getLogger(RecordProcessingTemplate.class.getName());

        RecordProcessingTemplate(RetryPolicy retryPolicy) {
            super(retryPolicy);
        }

        @Override
        protected void beforeCall() {
            // no op
        }

        @Override
        protected void afterCall(Object result) {
            // no op
        }

        @Override
        protected void onException(Exception e) {
            LOGGER.error("Unable to process record", e);
        }

        @Override
        protected void onMaxAttempts(Exception e) {
            LOGGER.error("Unable to process record after {} attempt(s)", retryPolicy.getMaxAttempts());
        }

        @Override
        protected void beforeWait() {
            LOGGER.debug("Waiting for {} {} before retrying to process record", retryPolicy.getDelay(), retryPolicy.getTimeUnit());
        }

        @Override
        protected void afterWait() {
            // no op
        }

    }
}
