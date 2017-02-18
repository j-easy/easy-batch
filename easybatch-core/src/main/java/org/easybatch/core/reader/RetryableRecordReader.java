/**
 * The MIT License
 *
 *   Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.core.reader;

import org.easybatch.core.record.Record;
import org.easybatch.core.retry.RetryPolicy;
import org.easybatch.core.retry.RetryTemplate;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Decorator that makes a {@link RecordReader} retryable whenever the data source is temporarily unavailable.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class RetryableRecordReader implements RecordReader {

    private RecordReader delegate;
    private RecordReadingCallable recordReadingCallable;
    private RecordReadingTemplate recordReadingTemplate;

    /**
     * Create a new {@link RetryableRecordReader}.
     *
     * @param delegate record reader
     * @param retryPolicy to apply
     */
    public RetryableRecordReader(RecordReader delegate, RetryPolicy retryPolicy) {
        this.delegate = delegate;
        this.recordReadingCallable = new RecordReadingCallable(delegate);
        this.recordReadingTemplate = new RecordReadingTemplate(retryPolicy);
    }

    @Override
    public void open() throws Exception {
        delegate.open();
    }

    @Override
    public Record readRecord() throws Exception {
        return recordReadingTemplate.execute(recordReadingCallable);
    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }

    private class RecordReadingCallable implements Callable<Record> {

        private RecordReader recordReader;

        RecordReadingCallable(RecordReader recordReader) {
            this.recordReader = recordReader;
        }

        @Override
        public Record call() throws Exception {
            return recordReader.readRecord();
        }

    }

    private class RecordReadingTemplate extends RetryTemplate {

        private final Logger LOGGER = Logger.getLogger(RecordReadingTemplate.class.getName());

        RecordReadingTemplate(RetryPolicy retryPolicy) {
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
            LOGGER.log(Level.SEVERE, "Unable to read next record", e);
        }

        @Override
        protected void onMaxAttempts(Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to read next record after {0} attempt(s)", retryPolicy.getMaxAttempts());
        }

        @Override
        protected void beforeWait() {
            LOGGER.log(Level.INFO, "Waiting for {0} {1} before retrying to read next record", new Object[]{retryPolicy.getDelay(), retryPolicy.getTimeUnit()});
        }

        @Override
        protected void afterWait() {
            // no op
        }

    }
}
