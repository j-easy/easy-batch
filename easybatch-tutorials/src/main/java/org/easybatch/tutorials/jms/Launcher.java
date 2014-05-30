/*
 * The MIT License
 *
 *   Copyright (c) 2014, Mahmoud Ben Hassine (md.benhassine@gmail.com)
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

package org.easybatch.tutorials.jms;

import org.easybatch.core.api.EasyBatchReport;
import org.easybatch.core.impl.EasyBatchEngine;
import org.easybatch.core.impl.EasyBatchEngineBuilder;
import org.easybatch.flatfile.dsv.DelimitedRecordMapper;
import org.easybatch.tools.reporting.DefaultEasyBatchReportsAggregator;
import org.easybatch.tools.reporting.EasyBatchReportsAggregator;
import org.easybatch.tutorials.common.Greeting;
import org.easybatch.tutorials.jmx.GreetingSlowProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
* Main class to run the JMS tutorial.
 *
* @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
*/
public class Launcher {

    public static void main(String[] args) throws Exception {

        //start embedded JMS broker
        JMSUtil.startBroker();

        // Build easy batch engines
        EasyBatchEngine easyBatchEngine1 = buildEasyBatchEngine(1);
        EasyBatchEngine easyBatchEngine2 = buildEasyBatchEngine(2);

        //create a 2 threads pool to call Easy Batch engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<EasyBatchReport> batchReportFuture1 = executorService.submit(easyBatchEngine1);
        Future<EasyBatchReport> batchReportFuture2 = executorService.submit(easyBatchEngine2);

        //wait for easy batch instances termination and get partial reports
        EasyBatchReport easyBatchReport1 = batchReportFuture1.get();
        EasyBatchReport easyBatchReport2 = batchReportFuture2.get();

        //aggregate partial reports into a global one
        EasyBatchReportsAggregator reportsAggregator = new DefaultEasyBatchReportsAggregator();
        EasyBatchReport finalReport = reportsAggregator.aggregateReports(easyBatchReport1, easyBatchReport2);
        System.out.println(finalReport);

        //shutdown executor service
        executorService.shutdown();

        //stop embedded JMS broker
        JMSUtil.stopBroker();

    }

    public static EasyBatchEngine buildEasyBatchEngine(int id) {
        return new EasyBatchEngineBuilder()
                .registerRecordReader(new GreetingJmsReader(id))
                .registerRecordMapper(new DelimitedRecordMapper<Greeting>(Greeting.class, new String[]{"id","name"}))
                .registerRecordProcessor(new GreetingSlowProcessor())
                .build();
    }

}