package org.easybatch.core.job;

import org.easybatch.core.record.Record;
import org.easybatch.core.retry.RetryPolicy;
import org.easybatch.core.retry.RetryTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.toSeconds;

class RecordReadingTemplate extends RetryTemplate {

    private final Logger LOGGER = Logger.getLogger(Job.class.getName());

    private EventManager eventManager;

    private JobReport jobReport;

    public RecordReadingTemplate(RetryPolicy retryPolicy, EventManager eventManager, JobReport jobReport) {
        super(retryPolicy);
        this.eventManager = eventManager;
        this.jobReport = jobReport;
    }

    @Override
    protected void beforeCall() {
        eventManager.fireBeforeRecordReading();
    }

    @Override
    protected void afterCall(Object result) {
        eventManager.fireAfterRecordReading((Record) result);
    }

    @Override
    protected void onException(Exception e) {
        eventManager.fireOnRecordReadingException(e);
        LOGGER.log(Level.SEVERE, "Unable to read next record", e);
        jobReport.getMetrics().setLastError(e);
    }

    @Override
    protected void onMaxAttempts(Exception e) {
        LOGGER.log(Level.WARNING, "Unable to read next record after {0} attempt(s), aborting job", maxAttempts);
        jobReport.setStatus(JobStatus.ABORTED);
        jobReport.getMetrics().setEndTime(System.currentTimeMillis());
    }

    @Override
    protected void beforeWait() {
        LOGGER.log(Level.INFO, "Waiting for {0}s before retrying to read next record", toSeconds(backOffDelay));
    }

    @Override
    protected void afterWait() {
        // no op
    }

}