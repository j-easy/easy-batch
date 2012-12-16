/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.quartz;

import net.benas.cb4j.core.api.BatchEngine;
import net.benas.cb4j.core.config.BatchConfiguration;
import net.benas.cb4j.core.impl.DefaultBatchEngineImpl;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Quartz scheduler wrapper used to setup triggers.
 * @author benas (md.benhassine@gmail.com)
 */
public class BatchScheduler {

    /**
     * The name of CB4J job trigger.
     */
    public static final String CB4J_BATCH_JOB_TRIGGER = "cb4j-batch-job-trigger";

    /**
     * The name of CB4J batch job.
     */
    public static final String CB4J_BATCH_JOB = "cb4j-batch-job";

    /**
     * The trigger used to fire batch execution.
     */
    private Trigger trigger;

    /**
     * Quartz scheduler.
     */
    private Scheduler scheduler;

    public BatchScheduler(BatchConfiguration batchConfiguration, Class<? extends BatchEngine> clazz) throws BatchSchedulerException {
        JobFactory jobFactory = new BatchJobFactory(batchConfiguration,clazz);
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(jobFactory);
        } catch (SchedulerException e) {
            throw new BatchSchedulerException("An exception occurred during scheduler setup, root error = ", e);
        }
    }

    public BatchScheduler(BatchConfiguration batchConfiguration) throws BatchSchedulerException {
        this(batchConfiguration, DefaultBatchEngineImpl.class);
    }

    /**
     * Setup the time the trigger should start at.
     * @param startTime the start time
     */
    public void scheduleAt(Date startTime) {
        trigger = newTrigger()
                .withIdentity(CB4J_BATCH_JOB_TRIGGER)
                .startAt(startTime)
                .forJob(CB4J_BATCH_JOB)
                .build();
    }

    /**
     * Setup a trigger to start now and repeat with interval period.
     * @param interval the repeat interval in minutes
     * @param repeatForever true if the trigger should repeat forever
     */
    public void scheduleNowWithInterval(int interval, boolean repeatForever) {
        SimpleScheduleBuilder simpleScheduleBuilder = simpleSchedule().withIntervalInMinutes(interval);
        if (repeatForever) {
            simpleScheduleBuilder = simpleScheduleBuilder.repeatForever();
        }
        trigger = newTrigger()
                .withIdentity(CB4J_BATCH_JOB_TRIGGER)
                .startNow()
                .withSchedule(simpleScheduleBuilder)
                .forJob(CB4J_BATCH_JOB)
                .build();
    }

    /**
     * Setup a unix cron-like trigger.
     * @param cronExpression the cron expression to use. For a complete tutorial about cron expressions, please refer to <a href="http://quartz-scheduler.org/documentation/quartz-2.1.x/tutorials/crontrigger">quartz reference documentation</a>.
     */
    public void scheduleCron(String cronExpression) {
        trigger = newTrigger()
                .withIdentity(CB4J_BATCH_JOB_TRIGGER)
                .withSchedule(cronSchedule(cronExpression))
                .forJob(CB4J_BATCH_JOB)
                .build();
    }

    /**
     * Start the scheduler.
     * @throws BatchSchedulerException thrown if the scheduler cannot be started
     */
    public void start() throws BatchSchedulerException {
        try {
            JobDetail job = newJob(BatchJob.class).withIdentity(CB4J_BATCH_JOB).build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            try {
                scheduler.shutdown(true);
                throw new BatchSchedulerException("An exception occurred during scheduler startup, root error = ", e);
            } catch (SchedulerException e1) {
                throw new BatchSchedulerException("Unable to shutdown the scheduler, the process may be killed. Root error = ", e1);
            }
        }
    }

}
