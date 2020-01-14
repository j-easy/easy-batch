package org.easybatch.tutorials.advanced.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Quartz Job implementation to schedule Easy Batch jobs.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class EasyBatchJob implements org.quartz.Job {

    /**
     * The job instance.
     */
    private org.easybatch.core.job.Job easyBatchJob;

    /**
     * Create a quartz job to schedule a Easy Batch job.
     *
     * @param easyBatchJob the Easy Batch job to schedule
     */
    public EasyBatchJob(final org.easybatch.core.job.Job easyBatchJob) {
        this.easyBatchJob = easyBatchJob;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        try {
            easyBatchJob.call();
        } catch (Exception e) {
            throw new JobExecutionException("An exception occurred during the execution of job " + easyBatchJob.getName(), e);
        }
    }

}
