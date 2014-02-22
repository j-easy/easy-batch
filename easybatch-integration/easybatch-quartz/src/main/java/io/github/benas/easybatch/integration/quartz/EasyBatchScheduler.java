/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package io.github.benas.easybatch.integration.quartz;

import io.github.benas.easybatch.core.impl.EasyBatchEngine;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

import java.util.Date;

/**
 * Quartz scheduler wrapper used to setup triggers.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class EasyBatchScheduler {

    /**
     * The name of easy batch job trigger.
     */
    public static final String EASY_BATCH_JOB_TRIGGER = "easy-batch-job-trigger";

    /**
     * The name of easy batch job.
     */
    public static final String EASY_BATCH_JOB = "easy-batch-job";

    /**
     * The trigger used to fire batch execution.
     */
    private Trigger trigger;

    /**
     * Quartz scheduler.
     */
    private Scheduler scheduler;

    public EasyBatchScheduler(EasyBatchEngine easyBatchEngine) throws EasyBatchSchedulerException {
        JobFactory jobFactory = new EasyBatchJobFactory(easyBatchEngine);
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(jobFactory);
        } catch (SchedulerException e) {
            throw new EasyBatchSchedulerException("An exception occurred during scheduler setup", e);
        }
    }

    /**
     * Setup the time the trigger should start at.
     * @param startTime the start time
     */
    public void scheduleAt(final Date startTime) {
        trigger = TriggerBuilder.newTrigger()
                .withIdentity(EASY_BATCH_JOB_TRIGGER)
                .startAt(startTime)
                .forJob(EASY_BATCH_JOB)
                .build();
    }

    /**
     * Setup a trigger to start at a fixed point of time and repeat with interval period.
     * @param startTime the start time
     * @param interval the repeat interval in minutes
     */
    public void scheduleAtWithInterval(final Date startTime, final int interval) {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(interval);
        simpleScheduleBuilder = simpleScheduleBuilder.repeatForever();
        trigger = TriggerBuilder.newTrigger()
                .withIdentity(EASY_BATCH_JOB_TRIGGER)
                .startAt(startTime)
                .withSchedule(simpleScheduleBuilder)
                .forJob(EASY_BATCH_JOB)
                .build();
    }

    /**
     * Setup a unix cron-like trigger.
     * @param cronExpression the cron expression to use. For a complete tutorial about cron expressions, please refer to <a href="http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/crontrigger">quartz reference documentation</a>.
     */
    public void scheduleCron(final String cronExpression) {
        trigger = TriggerBuilder.newTrigger()
                .withIdentity(EASY_BATCH_JOB_TRIGGER)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .forJob(EASY_BATCH_JOB)
                .build();
    }

    /**
     * Start the scheduler.
     * @throws EasyBatchSchedulerException thrown if the scheduler cannot be started
     */
    public void start() throws EasyBatchSchedulerException {
        try {
            JobDetail job = JobBuilder.newJob(EasyBatchJob.class).withIdentity(EASY_BATCH_JOB).build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            try {
                scheduler.shutdown(true);
                throw new EasyBatchSchedulerException("An exception occurred during scheduler startup", e);
            } catch (SchedulerException e1) {
                throw new EasyBatchSchedulerException("Unable to shutdown the scheduler, the process may be killed", e1);
            }
        }
    }

}
