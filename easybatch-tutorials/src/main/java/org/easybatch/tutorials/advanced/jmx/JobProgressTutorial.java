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

package org.easybatch.tutorials.advanced.jmx;

import org.easybatch.core.jmx.JobMonitorProxy;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.flatfile.FlatFileRecordReader;

import java.io.File;
import java.util.logging.LogManager;

/**
 * Main class to run the job progress tutorial.
 *
 * To run with:
 *  -Dcom.sun.management.jmxremote.port=9999
 *  -Dcom.sun.management.jmxremote.local.only=false
 *  -Dcom.sun.management.jmxremote.authenticate=false
 *  -Dcom.sun.management.jmxremote.ssl=false
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobProgressTutorial {

    public static void main(String[] args) throws Exception {

        File dataSource = new File("src/main/resources/data/tweets.csv");
        ProgressListener progressListener = new ProgressListener(dataSource);

        // Build the batch job
        Job job = new JobBuilder()
                .reader(new FlatFileRecordReader(dataSource))
                .processor(new TweetSlowProcessor())
                .enableJmx()
                .build();

        // Submit the job the job
        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.submit(job);
        jobExecutor.shutdown();

        // Create a job monitor proxy to get notified each time a progress information is sent by the running job
        JobMonitorProxy jobMonitor = new JobMonitorProxy("localhost", 9999, "job");
        jobMonitor.addMonitoringListener(progressListener);
        Thread thread = new Thread(jobMonitor);
        thread.setDaemon(true);
        thread.start();
    }

}
