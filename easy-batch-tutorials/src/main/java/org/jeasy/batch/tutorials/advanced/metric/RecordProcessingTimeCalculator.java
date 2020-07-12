package org.jeasy.batch.tutorials.advanced.metric;

import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.core.listener.JobListener;
import org.jeasy.batch.core.listener.PipelineListener;
import org.jeasy.batch.core.record.Record;

public class RecordProcessingTimeCalculator implements PipelineListener, JobListener {

    private long startTime;
    private long nbRecords;
    private long recordProcessingTimesSum;

    @Override
    public void afterJob(JobReport jobReport) {
        jobReport.getMetrics().addMetric("Record processing time average (in ms)", (double)recordProcessingTimesSum / (double)nbRecords);
    }

    @Override
    public <P> Record<P> beforeRecordProcessing(Record<P> record) {
        nbRecords++;
        startTime = System.currentTimeMillis();
        return record;
    }

    @Override
    public <P> void afterRecordProcessing(Record<P> input, Record<P> output) {
        recordProcessingTimesSum +=  System.currentTimeMillis() - startTime;
    }

    @Override
    public <P> void onRecordProcessingException(Record<P> record, Throwable throwable) {
        recordProcessingTimesSum += System.currentTimeMillis() - startTime;
    }
}
