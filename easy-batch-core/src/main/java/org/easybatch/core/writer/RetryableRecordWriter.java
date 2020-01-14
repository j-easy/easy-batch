/**
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
package org.easybatch.core.writer;

import org.easybatch.core.record.Batch;
import org.easybatch.core.retry.RetryPolicy;
import org.easybatch.core.retry.RetryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Decorator that makes a {@link RecordWriter} retryable whenever the data sink is temporarily unavailable.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class RetryableRecordWriter implements RecordWriter {

    private RecordWriter delegate;
    private RecordWritingTemplate recordWritingTemplate;

    /**
     * Create a new {@link RetryableRecordWriter}.
     *
     * @param delegate record reader
     * @param retryPolicy to apply
     */
    public RetryableRecordWriter(RecordWriter delegate, RetryPolicy retryPolicy) {
        this.delegate = delegate;
        this.recordWritingTemplate = new RecordWritingTemplate(retryPolicy);
    }

    @Override
    public void open() throws Exception {
        delegate.open();
    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        recordWritingTemplate.execute(new RecordWritingCallable(delegate, batch));
    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }

    private static class RecordWritingCallable implements Callable<Void> {

        private RecordWriter recordWriter;

        private Batch batch;

        RecordWritingCallable(RecordWriter recordWriter, Batch batch) {
            this.recordWriter = recordWriter;
            this.batch = batch;
        }

        @Override
        public Void call() throws Exception {
            recordWriter.writeRecords(batch);
            return null;
        }

    }

    private static class RecordWritingTemplate extends RetryTemplate {

        private final Logger LOGGER = LoggerFactory.getLogger(RecordWritingTemplate.class.getName());

        RecordWritingTemplate(RetryPolicy retryPolicy) {
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
            LOGGER.error("Unable to write records", e);
        }

        @Override
        protected void onMaxAttempts(Exception e) {
            LOGGER.error("Unable to write records after {} attempt(s)", retryPolicy.getMaxAttempts());
        }

        @Override
        protected void beforeWait() {
            LOGGER.info("Waiting for {} {} before retrying to write records", retryPolicy.getDelay(), retryPolicy.getTimeUnit());
        }

        @Override
        protected void afterWait() {
            // no op
        }

    }
}
