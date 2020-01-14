/*
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.tutorials.advanced.quartz;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.writer.StandardOutputRecordWriter;
import org.easybatch.flatfile.FlatFileRecordReader;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Main class to run the Hello World tutorial repeatedly every 10 seconds using Quartz.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Launcher {

    public static void main(String[] args) throws Exception {

        // Build a batch job
        Job job = new JobBuilder()
                .named("my-job")
                .reader(new FlatFileRecordReader(new File("src/main/resources/data/tweets.csv")))
                .writer(new StandardOutputRecordWriter())
                .build();

        // Create a Quartz scheduler
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.setJobFactory(new EasyBatchJobFactory());

        // Schedule the job to start now and run every 10 seconds
        SimpleScheduleBuilder scheduleBuilder = simpleSchedule()
                .withIntervalInSeconds(10)
                .repeatForever();

        Trigger trigger = newTrigger()
                .withIdentity("my-trigger")
                .startAt(new Date())
                .withSchedule(scheduleBuilder)
                .forJob(job.getName())
                .build();

        JobDetail jobDetail = getJobDetail(job);
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();

        System.out.println("Hit enter to stop the application");
        new Scanner(System.in).nextLine();
        scheduler.shutdown();
    }

    private static JobDetail getJobDetail(org.easybatch.core.job.Job job) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(EasyBatchJobFactory.JOB_KEY, job);
        return newJob(EasyBatchJob.class)
                .withIdentity(job.getName())
                .usingJobData(jobDataMap)
                .build();
    }

}
