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

package org.easybatch.tutorials.advanced.parallel;

import org.easybatch.core.api.Report;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordDispatcher;
import org.easybatch.core.filter.PoisonRecordFilter;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.core.reader.QueueRecordReader;
import org.easybatch.core.dispatcher.RoundRobinRecordDispatcher;
import org.easybatch.core.record.PoisonRecord;
import org.easybatch.flatfile.FlatFileRecordReader;
import org.easybatch.tools.reporting.DefaultReportMerger;
import org.easybatch.tools.reporting.ReportMerger;
import org.easybatch.tutorials.basic.helloworld.TweetProcessor;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.*;

/**
* Main class to run the record dispatching tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
*/
public class ParallelTutorialWithRecordDispatching {

    private static final int THREAD_POOL_SIZE = 2;

    public static void main(String[] args) throws Exception {

        // Input file tweets.csv
        File tweets = new File(args[0]);

        //Create queues
        BlockingQueue<Record> queue1 = new LinkedBlockingQueue<Record>();
        BlockingQueue<Record> queue2 = new LinkedBlockingQueue<Record>();

        // Build easy batch engines
        Engine engine1 = buildBatchEngine(queue1);
        Engine engine2 = buildBatchEngine(queue2);

        //create a 2 threads pool to call engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        //submit workers to executor service
        Future<Report> reportFuture1 = executorService.submit(engine1);
        Future<Report> reportFuture2 = executorService.submit(engine2);

        //create a record dispatcher to dispatch records to previously created queues
        RecordDispatcher recordDispatcher = new RoundRobinRecordDispatcher(Arrays.asList(queue1, queue2));

        //read data source and dispatch records to queues in round-robin fashion
        FlatFileRecordReader flatFileRecordReader = new FlatFileRecordReader(tweets);
        flatFileRecordReader.open();
        while (flatFileRecordReader.hasNextRecord()) {
            Record record = flatFileRecordReader.readNextRecord();
            recordDispatcher.dispatchRecord(record);
        }
        flatFileRecordReader.close();

        //send poison records when all input data has been dispatched to workers
        recordDispatcher.dispatchRecord(new PoisonRecord());

        //wait for easy batch instances termination and get partial reports
        Report report1 = reportFuture1.get();
        Report report2 = reportFuture2.get();

        //merge partial reports into a global one
        ReportMerger reportMerger = new DefaultReportMerger();
        Report finalReport = reportMerger.mergerReports(report1, report2);
        System.out.println(finalReport);

        //shutdown executor service
        executorService.shutdown();

    }

    public static Engine buildBatchEngine(BlockingQueue<Record> queue) {
        return new EngineBuilder()
                .reader(new QueueRecordReader(queue))
                .filter(new PoisonRecordFilter())
                .processor(new TweetProcessor())
                .build();
    }

}