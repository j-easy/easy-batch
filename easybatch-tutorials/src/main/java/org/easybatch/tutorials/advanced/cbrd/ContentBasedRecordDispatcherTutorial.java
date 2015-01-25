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

package org.easybatch.tutorials.advanced.cbrd;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordDispatcher;
import org.easybatch.core.api.Report;
import org.easybatch.core.filter.PoisonRecordFilter;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.core.reader.QueueRecordReader;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.dispatcher.ContentBasedRecordDispatcherBuilder;
import org.easybatch.core.record.PoisonRecord;
import org.easybatch.tools.reporting.DefaultReportMerger;
import org.easybatch.tools.reporting.ReportMerger;

import java.util.concurrent.*;

/**
* Main class to run the content based record dispatching tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
*/
public class ContentBasedRecordDispatcherTutorial {

    public static void main(String[] args) throws Exception {

        //Create queues
        BlockingQueue<Record> appleQueue = new LinkedBlockingQueue<Record>();
        BlockingQueue<Record> orangeQueue = new LinkedBlockingQueue<Record>();
        BlockingQueue<Record> defaultQueue = new LinkedBlockingQueue<Record>();

        // Build easy batch engines
        Engine engine1 = buildBatchEngine(appleQueue);
        Engine engine2 = buildBatchEngine(orangeQueue);
        Engine engine3 = buildBatchEngine(defaultQueue);

        //create a 3 threads pool to call Easy Batch engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //submit workers to executor service
        Future<Report> reportFuture1 = executorService.submit(engine1);
        Future<Report> reportFuture2 = executorService.submit(engine2);
        Future<Report> reportFuture3 = executorService.submit(engine3);

        //create a content based record dispatcher to dispatch records to previously created queues
        RecordDispatcher recordDispatcher = new ContentBasedRecordDispatcherBuilder()
                .when(new AppleRecordPredicate()).dispatchTo(appleQueue)
                .when(new OrangeRecordPredicate()).dispatchTo(orangeQueue)
                .otherwise(defaultQueue)
                .build();

        //read data source and dispatch record to queues based on their content
        StringRecordReader stringRecordReader = new StringRecordReader("1,apple\n2,orange\n3,banana\n4,apple\n5,pear");
        stringRecordReader.open();
        while (stringRecordReader.hasNextRecord()) {
            Record record = stringRecordReader.readNextRecord();
            recordDispatcher.dispatchRecord(record);
        }
        stringRecordReader.close();

        //send poison records when all input data has been dispatched to workers
        recordDispatcher.dispatchRecord(new PoisonRecord());

        //wait for easy batch instances termination and get partial reports
        Report report1 = reportFuture1.get();
        Report report2 = reportFuture2.get();
        Report report3 = reportFuture3.get();

        //merge partial reports into a global one
        ReportMerger reportMerger = new DefaultReportMerger();
        Report finalReport = reportMerger.mergerReports(report1, report2, report3);
        System.out.println(finalReport);

        //shutdown executor service
        executorService.shutdown();

    }

    public static Engine buildBatchEngine(BlockingQueue<Record> queue) {
        return new EngineBuilder()
                .reader(new QueueRecordReader(queue))
                .filter(new PoisonRecordFilter())
                .build();
    }

}