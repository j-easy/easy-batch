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

package org.easybatch.tutorials.dispatcher;

import org.easybatch.core.api.EasyBatchReport;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordDispatcher;
import org.easybatch.core.filter.PoisonRecordFilter;
import org.easybatch.core.impl.EasyBatchEngine;
import org.easybatch.core.impl.EasyBatchEngineBuilder;
import org.easybatch.core.util.*;
import org.easybatch.tools.reporting.DefaultEasyBatchReportsAggregator;
import org.easybatch.tools.reporting.EasyBatchReportsAggregator;

import java.util.Arrays;
import java.util.concurrent.*;

/**
* Main class to run the recording dispatching tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
*/
public class Launcher {

    public static void main(String[] args) throws Exception {

        //Create queues
        BlockingQueue<Record> queue1 = new ArrayBlockingQueue<Record>(32);
        BlockingQueue<Record> queue2 = new ArrayBlockingQueue<Record>(32);

        // Build easy batch engines
        EasyBatchEngine easyBatchEngine1 = buildEasyBatchEngine(queue1);
        EasyBatchEngine easyBatchEngine2 = buildEasyBatchEngine(queue2);

        //create a 2 threads pool to call Easy Batch engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        //submit workers to executor service
        Future<EasyBatchReport> batchReportFuture1 = executorService.submit(easyBatchEngine1);
        Future<EasyBatchReport> batchReportFuture2 = executorService.submit(easyBatchEngine2);

        //create a record dispatcher to dispatch records to previously created queues
        RecordDispatcher recordDispatcher = new QueueRecordDispatcher(Arrays.asList(queue1, queue2));

        //read data source and dispatch record to queues in round-robin fashion
        StringRecordReader stringRecordReader = new StringRecordReader("Foo\nBar\nTar\nChar\n");
        stringRecordReader.open();
        while (stringRecordReader.hasNextRecord()) {
            Record record = stringRecordReader.readNextRecord();
            recordDispatcher.dispatchRecord(record);
        }
        stringRecordReader.close();

        //send poison records when all input data has been dispatched to workers
        recordDispatcher.dispatchRecord(new PoisonRecord());//will be dispatched to queue 1
        recordDispatcher.dispatchRecord(new PoisonRecord());//will be dispatched to queue 2

        //wait for easy batch instances termination and get partial reports
        EasyBatchReport easyBatchReport1 = batchReportFuture1.get();
        EasyBatchReport easyBatchReport2 = batchReportFuture2.get();

        //aggregate partial reports into a global one
        EasyBatchReportsAggregator reportsAggregator = new DefaultEasyBatchReportsAggregator();
        EasyBatchReport finalReport = reportsAggregator.aggregateReports(easyBatchReport1, easyBatchReport2);
        System.out.println(finalReport);

        //shutdown executor service
        executorService.shutdown();

    }

    public static EasyBatchEngine buildEasyBatchEngine(BlockingQueue<Record> queue) {
        return new EasyBatchEngineBuilder()
                .readRecordsWith(new QueueRecordReader(queue))
                .filterRecordsWith(new PoisonRecordFilter())
                .build();
    }

}