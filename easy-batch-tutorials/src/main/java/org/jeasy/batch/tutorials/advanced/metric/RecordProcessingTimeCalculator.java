package org.jeasy.batch.tutorials.advanced.metric;

import org.jeasy.batch.core.job.JobParameters;
import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.core.listener.JobListener;
import org.jeasy.batch.core.listener.PipelineListener;
import org.jeasy.batch.core.record.Record;

public class RecordProcessingTimeCalculator implements PipelineListener, JobListener {

    private long startTime;
    private long nbRecords;
    private long recordProcessingTimesSum;

    @Override
    public void beforeJobStart(JobParameters jobParameters) {

    }

    @Override
    public void afterJobEnd(JobReport jobReport) {
        jobReport.getMetrics().addMetric("Record processing time average (in ms)", (double)recordProcessingTimesSum / (double)nbRecords);
    }

    @Override
    public Record beforeRecordProcessing(Record record) {
        nbRecords++;
        startTime = System.currentTimeMillis();
        return record;
    }

    @Override
    public void afterRecordProcessing(Record input, Record output) {
        recordProcessingTimesSum +=  System.currentTimeMillis() - startTime;
    }

    @Override
    public void onRecordProcessingException(Record record, Throwable throwable) {
        recordProcessingTimesSum += System.currentTimeMillis() - startTime;
    }
}
