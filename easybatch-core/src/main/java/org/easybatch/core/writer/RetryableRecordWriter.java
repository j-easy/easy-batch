package org.easybatch.core.writer;

import org.easybatch.core.record.Batch;
import org.easybatch.core.retry.RetryPolicy;
import org.easybatch.core.retry.RetryTemplate;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private class RecordWritingCallable implements Callable<Void> {

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

    private class RecordWritingTemplate extends RetryTemplate {

        private final Logger LOGGER = Logger.getLogger(RecordWritingTemplate.class.getName());

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
            LOGGER.log(Level.SEVERE, "Unable to write records", e);
        }

        @Override
        protected void onMaxAttempts(Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to write records after {0} attempt(s)", retryPolicy.getMaxAttempts());
        }

        @Override
        protected void beforeWait() {
            LOGGER.log(Level.INFO, "Waiting for {0} {1} before retrying to write records", new Object[]{retryPolicy.getDelay(), retryPolicy.getTimeUnit()});
        }

        @Override
        protected void afterWait() {
            // no op
        }

    }
}
