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

package org.easybatch.tutorials.advanced.jms;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.jms.JmsPoisonRecordFilter;
import org.easybatch.jms.JmsQueueRecordReader;

/**
* Main class to run the JMS tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
*/
public class Launcher {

    public static void main(String[] args) throws Exception {

        JMSUtil.startEmbeddedBroker();

        JMSUtil.initJMSFactory();

        // Build a batch job
        Job job = new JobBuilder()
                .reader(new JmsQueueRecordReader(JMSUtil.getQueueConnectionFactory(), JMSUtil.getQueue()))
                .filter(new JmsPoisonRecordFilter())
                .processor(new JmsRecordProcessor())
                .build();

        // Execute the job and get report
        JobExecutor jobExecutor = new JobExecutor();
        JobReport report = jobExecutor.execute(job);

        System.out.println(report);

        jobExecutor.shutdown();
        JMSUtil.stopEmbeddedBroker();
        System.exit(0);

    }

}
