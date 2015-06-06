/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.integration.quartz;

import org.easybatch.core.api.Engine;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Quartz scheduler wrapper used to setup triggers.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class BatchScheduler {

    private static final Logger LOGGER = Logger.getLogger(BatchScheduler.class.getName());

    /**
     * The name of easy batch job trigger.
     */
    private String triggerName;

    /**
     * The name of easy batch job.
     */
    private String jobName;

    /**
     * The trigger used to fire batch execution.
     */
    private Trigger trigger;

    /**
     * Quartz scheduler.
     */
    private Scheduler scheduler;

    public BatchScheduler(Engine engine) throws BatchSchedulerException {
        JobFactory jobFactory = new BatchJobFactory(engine);
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(jobFactory);
            jobName = "batch-job-" + engine.getExecutionId();
            triggerName = "trigger-for-" + jobName;
        } catch (SchedulerException e) {
            throw new BatchSchedulerException("An exception occurred during scheduler setup", e);
        }
    }

    /**
     * Setup the time the trigger should start at.
     *
     * @param startTime the start time
     */
    public void scheduleAt(final Date startTime) {
        trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName)
                .startAt(startTime)
                .forJob(jobName)
                .build();
        LOGGER.log(Level.INFO, "Building a scheduler for job {0} to start at {1}", new Object[]{jobName, startTime});
    }

    /**
     * Setup a trigger to start at a fixed point of time and repeat with interval period.
     *
     * @param startTime the start time
     * @param interval  the repeat interval in minutes
     */
    public void scheduleAtWithInterval(final Date startTime, final int interval) {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(interval);
        simpleScheduleBuilder = simpleScheduleBuilder.repeatForever();
        trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName)
                .startAt(startTime)
                .withSchedule(simpleScheduleBuilder)
                .forJob(jobName)
                .build();
        LOGGER.log(Level.INFO, "Building a scheduler for job {0} to start at {1} and every {2} minute(s)", new Object[]{jobName, startTime, interval});
    }

    /**
     * Setup a unix cron-like trigger.
     *
     * @param cronExpression the cron expression to use.
     *                       For a complete tutorial about cron expressions, please refer to
     *                       <a href="http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/crontrigger">quartz reference documentation</a>.
     */
    public void scheduleCron(final String cronExpression) {
        trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .forJob(jobName)
                .build();
        LOGGER.log(Level.INFO, "Building a scheduler for job {0} with cron expression {1}", new Object[]{jobName, cronExpression});
    }

    /**
     * Start the scheduler.
     *
     * @throws BatchSchedulerException thrown if the scheduler cannot be started
     */
    public void start() throws BatchSchedulerException {
        LOGGER.log(Level.INFO, "Starting the scheduler for job {0}", jobName);
        try {
            JobDetail job = JobBuilder.newJob(BatchJob.class).withIdentity(jobName).build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            throw new BatchSchedulerException("An exception occurred during scheduler startup", e);
        }
    }


    /**
     * Stop the scheduler.
     * <p/>
     * Note: The scheduler cannot be re-started.
     *
     * @throws BatchSchedulerException thrown if the scheduler cannot be stopped
     */
    public void stop() throws BatchSchedulerException {
        LOGGER.log(Level.INFO, "Stopping the scheduler for job {0}", jobName);
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            throw new BatchSchedulerException("An exception occurred during scheduler shutdown", e);
        }
    }

    /**
     * Check if the scheduler is started.
     *
     * @throws BatchSchedulerException thrown if the scheduler status cannot be checked
     */
    public boolean isStarted() throws BatchSchedulerException {
        try {
            return scheduler.isStarted();
        } catch (SchedulerException e) {
            throw new BatchSchedulerException("An exception occurred during checking if the scheduler is started", e);
        }
    }

    /**
     * Check if the scheduler is stopped.
     *
     * @throws BatchSchedulerException thrown if the scheduler status cannot be checked
     */
    public boolean isStopped() throws BatchSchedulerException {
        try {
            return scheduler.isShutdown();
        } catch (SchedulerException e) {
            throw new BatchSchedulerException("An exception occurred during checking if the scheduler is stopped", e);
        }
    }

}
