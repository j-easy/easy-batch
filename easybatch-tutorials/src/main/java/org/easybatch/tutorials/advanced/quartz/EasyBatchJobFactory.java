package org.easybatch.tutorials.advanced.quartz;

import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;

/**
 * Quartz Job factory implementation used to create job instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class EasyBatchJobFactory implements org.quartz.spi.JobFactory {

    public static final String JOB_KEY = "job";

    @Override
    public org.quartz.Job newJob(final TriggerFiredBundle bundle, final Scheduler scheduler) {
        return new EasyBatchJob((org.easybatch.core.job.Job) bundle.getJobDetail().getJobDataMap().get(JOB_KEY));
    }

    @Override
    public String toString() {
        return "EasyBatchJobFactory";
    }
}
