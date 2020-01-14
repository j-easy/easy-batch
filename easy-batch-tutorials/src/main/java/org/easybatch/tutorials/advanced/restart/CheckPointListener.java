package org.easybatch.tutorials.advanced.restart;

import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;

import java.io.*;
import java.util.Properties;
import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobStatus;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.listener.RecordWriterListener;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener that saves the number of the last successfully written record in a persistent store ( a file in this example ).
 * If the job fails, restarting the job will skip all records that have been correctly processed and written in the previous run.
 * 
 * This listener should be registered as a {@link RecordWriterListener}, {@link PipelineListener} and {@link JobListener} at the same time.
 * 
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CheckPointListener implements RecordWriterListener, PipelineListener, JobListener {

    public static final String JOB_NAME_KEY = "job.name";
    public static final String JOB_START_KEY = "job.start";
    public static final String JOB_END_KEY = "job.end";
    public static final String JOB_STATUS_KEY = "job.status";
    public static final String WRITE_LAST_KEY = "write.last";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckPointListener.class.getName());

    private long lastSuccessfullyWrittenRecordNumber;
    
    private File journal;

    public CheckPointListener(File journal) {
        this.journal = journal;
        String lastStatus = readPropertyFromJournal(JOB_STATUS_KEY);
        if(lastStatus != null && JobStatus.FAILED.equals(JobStatus.valueOf(lastStatus))) {
            this.lastSuccessfullyWrittenRecordNumber = parseLong(readPropertyFromJournal(WRITE_LAST_KEY));
            LOGGER.info("Last run has failed, records 1.." + lastSuccessfullyWrittenRecordNumber + " will be skipped");
        }
    }

    @Override
    public Record beforeRecordProcessing(Record record) {
        return record.getHeader().getNumber() <= lastSuccessfullyWrittenRecordNumber ? null : record;
    }

    @Override
    public void afterRecordProcessing(Record inputRecord, Record outputRecord) {

    }

    @Override
    public void onRecordProcessingException(Record record, Throwable throwable) {

    }

    @Override
    public void beforeRecordWriting(Batch batch) {

    }

    @Override
    public void afterRecordWriting(Batch batch) {
        Record lastRecord = null;
        for (Record record : batch) {
            lastRecord = record;
        }
        if (lastRecord != null) {
            writePropertyToJournal(WRITE_LAST_KEY, valueOf(lastRecord.getHeader().getNumber()));
        }
    }

    @Override
    public void onRecordWritingException(Batch batch, Throwable throwable) {

    }

    @Override
    public void beforeJobStart(JobParameters jobParameters) {
        
    }

    @Override
    public void afterJobEnd(JobReport jobReport) {
        writePropertyToJournal(JOB_NAME_KEY, jobReport.getJobName());
        writePropertyToJournal(JOB_START_KEY, Utils.formatTime(jobReport.getMetrics().getStartTime()));
        writePropertyToJournal(JOB_END_KEY, Utils.formatTime(jobReport.getMetrics().getEndTime()));
        writePropertyToJournal(JOB_STATUS_KEY, jobReport.getStatus().name());
    }
    
    private void writePropertyToJournal(String key, String value) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(journal));
            properties.setProperty(key, value);
            properties.store(new FileWriter(journal), String.format("setting key '%s' to %s", key, value));
        } catch (IOException e) {
            LOGGER.error("Unable to write property {} in journal file", key, e);
        }
    }
    
    private String readPropertyFromJournal(String key) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(journal));
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read property " + key + " from journal file", e);
        }
    }
}