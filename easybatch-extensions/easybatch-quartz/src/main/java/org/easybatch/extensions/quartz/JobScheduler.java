/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.extensions.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.checkNotNull;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Quartz scheduler wrapper used to setup triggers.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobScheduler {

    private static final Logger LOGGER = Logger.getLogger(JobScheduler.class.getName());

    private static final String JOB_NAME_PREFIX = "job-";

    private static final String TRIGGER_NAME_PREFIX = "trigger-for-job-";

    private static JobScheduler instance;

    private Scheduler scheduler;

    JobScheduler() throws JobSchedulerException {
        org.quartz.spi.JobFactory jobFactory = new JobFactory();
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(jobFactory);
        } catch (SchedulerException e) {
            throw new JobSchedulerException("An exception occurred during scheduler setup", e);
        }
    }

    /**
     * Get the scheduler instance.
     *
     * @return The scheduler instance
     * @throws JobSchedulerException thrown if an exception occurs while creating the scheduler
     */
    public static JobScheduler getInstance() throws JobSchedulerException {
        if (instance == null) {
            instance = new JobScheduler();
        }
        return instance;
    }

    /**
     * Schedule a job to start at a fixed point of time.
     *
     * @param job       the job to schedule
     * @param startTime the start time
     */
    public void scheduleAt(final org.easybatch.core.job.Job job, final Date startTime) throws JobSchedulerException {
        checkNotNull(job, "job");
        checkNotNull(startTime, "startTime");

        String name = job.getName();
        String jobName = JOB_NAME_PREFIX + name;
        String triggerName = TRIGGER_NAME_PREFIX + name;

        Trigger trigger = newTrigger()
                .withIdentity(triggerName)
                .startAt(startTime)
                .forJob(jobName)
                .build();

        JobDetail jobDetail = getJobDetail(job, jobName);

        try {
            LOGGER.log(Level.INFO, "Scheduling job {0} to start at {1}", new Object[]{job, startTime});
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new JobSchedulerException("Unable to schedule job " + job, e);
        }
    }

    /**
     * Schedule a job to start at a fixed point of time and repeat with interval period.
     *
     * @param job       the job to schedule
     * @param startTime the start time
     * @param interval  the repeat interval in seconds
     */
    public void scheduleAtWithInterval(final org.easybatch.core.job.Job job, final Date startTime, final int interval) throws JobSchedulerException {
        checkNotNull(job, "job");
        checkNotNull(startTime, "startTime");

        String name = job.getName();
        String jobName = JOB_NAME_PREFIX + name;
        String triggerName = TRIGGER_NAME_PREFIX + name;

        ScheduleBuilder scheduleBuilder = simpleSchedule()
                .withIntervalInSeconds(interval)
                .repeatForever();

        Trigger trigger = newTrigger()
                .withIdentity(triggerName)
                .startAt(startTime)
                .withSchedule(scheduleBuilder)
                .forJob(jobName)
                .build();

        JobDetail jobDetail = getJobDetail(job, jobName);

        try {
            LOGGER.log(Level.INFO, "Scheduling job {0} to start at {1} and every {2} second(s)", new Object[]{job, startTime, interval});
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new JobSchedulerException("Unable to schedule job " + job, e);
        }
    }

    /**
     * Schedule a job with a unix-like cron expression.
     *
     * @param job            the job to schedule
     * @param cronExpression the cron expression to use.
     *                       For a complete tutorial about cron expressions, please refer to
     *                       <a href="http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/crontrigger">quartz reference documentation</a>.
     */
    public void scheduleCron(final org.easybatch.core.job.Job job, final String cronExpression) throws JobSchedulerException {
        checkNotNull(job, "job");
        checkNotNull(cronExpression, "cronExpression");

        String name = job.getName();
        String jobName = JOB_NAME_PREFIX + name;
        String triggerName = TRIGGER_NAME_PREFIX + name;

        Trigger trigger = newTrigger()
                .withIdentity(triggerName)
                .withSchedule(cronSchedule(cronExpression))
                .forJob(jobName)
                .build();

        JobDetail jobDetail = getJobDetail(job, jobName);

        try {
            LOGGER.log(Level.INFO, "Scheduling job {0} with cron expression {1}", new Object[]{job, cronExpression});
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new JobSchedulerException("Unable to schedule job " + job, e);
        }
    }

    /**
     * Unschedule the given job.
     *
     * @param job the job to unschedule
     * @throws JobSchedulerException thrown if an exception occurs during job unscheduling
     */
    public void unschedule(final org.easybatch.core.job.Job job) throws JobSchedulerException {
        LOGGER.log(Level.INFO, "Unscheduling job {0} ", job);
        try {
            scheduler.unscheduleJob(TriggerKey.triggerKey(TRIGGER_NAME_PREFIX + job.getName()));
        } catch (SchedulerException e) {
            throw new JobSchedulerException("Unable to unschedule job " + job, e);
        }
    }

    /**
     * Check if the given job is scheduled.
     *
     * @param job the job to check
     * @return true if the job is scheduled, false else
     * @throws JobSchedulerException thrown if an exception occurs while checking if the job is scheduled
     */
    public boolean isScheduled(final org.easybatch.core.job.Job job) throws JobSchedulerException {
        try {
            return scheduler.checkExists(TriggerKey.triggerKey(TRIGGER_NAME_PREFIX + job.getName()));
        } catch (SchedulerException e) {
            throw new JobSchedulerException("Unable to check if the job " + job + " is scheduled", e);
        }
    }

    /**
     * Start the scheduler.
     *
     * @throws JobSchedulerException thrown if the scheduler cannot be started
     */
    public void start() throws JobSchedulerException {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            throw new JobSchedulerException("An exception occurred during scheduler startup", e);
        }
    }


    /**
     * Stop the scheduler.
     * <p/>
     * <strong>Note: The scheduler cannot be re-started and no more jobs can be scheduled.</strong>
     *
     * @throws JobSchedulerException thrown if the scheduler cannot be stopped
     */
    public void stop() throws JobSchedulerException {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            throw new JobSchedulerException("An exception occurred during scheduler shutdown", e);
        }
    }

    /**
     * Check if the scheduler is started.
     *
     * @throws JobSchedulerException thrown if the scheduler status cannot be checked
     */
    public boolean isStarted() throws JobSchedulerException {
        try {
            return scheduler.isStarted();
        } catch (SchedulerException e) {
            throw new JobSchedulerException("An exception occurred during checking if the scheduler is started", e);
        }
    }

    /**
     * Check if the scheduler is stopped.
     *
     * @throws JobSchedulerException thrown if the scheduler status cannot be checked
     */
    public boolean isStopped() throws JobSchedulerException {
        try {
            return scheduler.isShutdown();
        } catch (SchedulerException e) {
            throw new JobSchedulerException("An exception occurred during checking if the scheduler is stopped", e);
        }
    }

    private JobDetail getJobDetail(org.easybatch.core.job.Job job, String jobName) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("job", job);
        return newJob(Job.class).withIdentity(jobName).usingJobData(jobDataMap).build();
    }

}
