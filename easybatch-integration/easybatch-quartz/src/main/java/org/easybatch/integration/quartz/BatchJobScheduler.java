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

import static org.easybatch.core.util.Utils.checkNotNull;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Quartz scheduler wrapper used to setup triggers.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class BatchJobScheduler {

    private static final Logger LOGGER = Logger.getLogger(BatchJobScheduler.class.getName());

    private static final String JOB_NAME_PREFIX = "job-";

    private static final String TRIGGER_NAME_PREFIX = "trigger-for-job-";

    /**
     * Quartz scheduler.
     */
    private Scheduler scheduler;

    /**
     * The scheduler singleton instance.
     */
    private static BatchJobScheduler instance;

    /**
     * Get the scheduler instance.
     *
     * @return The scheduler instance
     * @throws BatchJobSchedulerException thrown if an exception occurs while creating the scheduler
     */
    public static BatchJobScheduler getInstance() throws BatchJobSchedulerException {
        if (instance == null) {
            instance = new BatchJobScheduler();
        }
        return instance;
    }

    BatchJobScheduler() throws BatchJobSchedulerException {
        JobFactory jobFactory = new BatchJobFactory();
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(jobFactory);
        } catch (SchedulerException e) {
            throw new BatchJobSchedulerException("An exception occurred during scheduler setup", e);
        }
    }

    /**
     * Schedule an engine to start at a fixed point of time.
     *
     * @param engine the engine to schedule
     * @param startTime the start time
     */
    public void scheduleAt(final Engine engine, final Date startTime) throws BatchJobSchedulerException {
        checkNotNull(engine, "engine");
        checkNotNull(startTime, "startTime");

        String executionId = engine.getExecutionId();
        String jobName = JOB_NAME_PREFIX + executionId;
        String triggerName = TRIGGER_NAME_PREFIX + executionId;

        Trigger trigger = newTrigger()
                .withIdentity(triggerName)
                .startAt(startTime)
                .forJob(jobName)
                .build();

        JobDetail job = getJobDetail(engine, jobName);

        try {
            LOGGER.log(Level.INFO, "Scheduling engine {0} to start at {1}", new Object[]{engine, startTime});
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new BatchJobSchedulerException("Unable to schedule engine " + engine, e);
        }
    }

    /**
     * Schedule an engine to start at a fixed point of time and repeat with interval period.
     *
     * @param engine the engine to schedule
     * @param startTime the start time
     * @param interval  the repeat interval in minutes
     */
    public void scheduleAtWithInterval(final Engine engine, final Date startTime, final int interval) throws BatchJobSchedulerException {
        checkNotNull(engine, "engine");
        checkNotNull(startTime, "startTime");

        String executionId = engine.getExecutionId();
        String jobName = JOB_NAME_PREFIX + executionId;
        String triggerName = TRIGGER_NAME_PREFIX + executionId;

        ScheduleBuilder scheduleBuilder = simpleSchedule()
                .withIntervalInMinutes(interval)
                .repeatForever();

        Trigger trigger = newTrigger()
                .withIdentity(triggerName)
                .startAt(startTime)
                .withSchedule(scheduleBuilder)
                .forJob(jobName)
                .build();

        JobDetail job = getJobDetail(engine, jobName);

        try {
            LOGGER.log(Level.INFO, "Scheduling engine {0} to start at {1} and every {2} minute(s)", new Object[]{engine, startTime, interval});
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new BatchJobSchedulerException("Unable to schedule engine " + engine, e);
        }
    }

    /**
     * Schedule an engine with a unix-like cron expression.
     *
     * @param engine the engine to schedule
     * @param cronExpression the cron expression to use.
     *                       For a complete tutorial about cron expressions, please refer to
     *                       <a href="http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/crontrigger">quartz reference documentation</a>.
     */
    public void scheduleCron(final Engine engine, final String cronExpression) throws BatchJobSchedulerException {
        checkNotNull(engine, "engine");
        checkNotNull(cronExpression, "cronExpression");

        String executionId = engine.getExecutionId();
        String jobName = JOB_NAME_PREFIX + executionId;
        String triggerName = TRIGGER_NAME_PREFIX + executionId;

        Trigger trigger = newTrigger()
                .withIdentity(triggerName)
                .withSchedule(cronSchedule(cronExpression))
                .forJob(jobName)
                .build();

        JobDetail job = getJobDetail(engine, jobName);

        try {
            LOGGER.log(Level.INFO, "Scheduling engine {0} with cron expression {1}", new Object[]{engine, cronExpression});
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new BatchJobSchedulerException("Unable to schedule engine " + engine, e);
        }
    }

    /**
     * Unschedule the given engine.
     *
     * @param engine the engine to unschedule
     * @throws BatchJobSchedulerException thrown if an exception occurs during engine unscheduling
     */
    public void unschedule(final Engine engine) throws BatchJobSchedulerException {
        LOGGER.log(Level.INFO, "Unscheduling engine {0} ", engine);
        try {
            scheduler.unscheduleJob(TriggerKey.triggerKey(TRIGGER_NAME_PREFIX + engine.getExecutionId()));
        } catch (SchedulerException e) {
            throw new BatchJobSchedulerException("Unable to unschedule engine " + engine, e);
        }
    }

    /**
     * Check if the given engine is scheduled.
     *
     * @param engine the engine to check
     * @return true if the engine is scheduled, false else
     * @throws BatchJobSchedulerException thrown if an exception occurs while checking if the engine is scheduled
     */
    public boolean isScheduled(final Engine engine) throws BatchJobSchedulerException {
        try {
            return scheduler.checkExists(TriggerKey.triggerKey(TRIGGER_NAME_PREFIX + engine.getExecutionId()));
        } catch (SchedulerException e) {
            throw new BatchJobSchedulerException("Unable to check if the engine " + engine + " is scheduled", e);
        }
    }

    /**
     * Start the scheduler.
     *
     * @throws BatchJobSchedulerException thrown if the scheduler cannot be started
     */
    public void start() throws BatchJobSchedulerException {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            throw new BatchJobSchedulerException("An exception occurred during scheduler startup", e);
        }
    }


    /**
     * Stop the scheduler.
     * <p/>
     * <strong>Note: The scheduler cannot be re-started and no more engines can be scheduled.</strong>
     *
     * @throws BatchJobSchedulerException thrown if the scheduler cannot be stopped
     */
    public void stop() throws BatchJobSchedulerException {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            throw new BatchJobSchedulerException("An exception occurred during scheduler shutdown", e);
        }
    }

    /**
     * Check if the scheduler is started.
     *
     * @throws BatchJobSchedulerException thrown if the scheduler status cannot be checked
     */
    public boolean isStarted() throws BatchJobSchedulerException {
        try {
            return scheduler.isStarted();
        } catch (SchedulerException e) {
            throw new BatchJobSchedulerException("An exception occurred during checking if the scheduler is started", e);
        }
    }

    /**
     * Check if the scheduler is stopped.
     *
     * @throws BatchJobSchedulerException thrown if the scheduler status cannot be checked
     */
    public boolean isStopped() throws BatchJobSchedulerException {
        try {
            return scheduler.isShutdown();
        } catch (SchedulerException e) {
            throw new BatchJobSchedulerException("An exception occurred during checking if the scheduler is stopped", e);
        }
    }

    private JobDetail getJobDetail(Engine engine, String jobName) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("engine", engine);
        return newJob(BatchJob.class).withIdentity(jobName).usingJobData(jobDataMap).build();
    }

}
