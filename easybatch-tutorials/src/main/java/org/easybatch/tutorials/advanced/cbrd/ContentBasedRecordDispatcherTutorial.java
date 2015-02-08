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
import org.easybatch.core.dispatcher.ContentBasedRecordDispatcher;
import org.easybatch.core.dispatcher.ContentBasedRecordDispatcherBuilder;
import org.easybatch.core.filter.PoisonRecordFilter;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.reader.QueueRecordReader;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.util.PoisonRecordBroadcaster;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static org.easybatch.core.impl.EngineBuilder.aNewEngine;

/**
* Main class to run the content based record dispatching tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
*/
public class ContentBasedRecordDispatcherTutorial {

    private static final int THREAD_POOL_SIZE = 4;

    public static void main(String[] args) throws Exception {

        String fruits = "1,apple\n2,orange\n3,banana\n4,apple\n5,pear";

        // Create queues
        BlockingQueue<Record> appleQueue = new LinkedBlockingQueue<Record>();
        BlockingQueue<Record> orangeQueue = new LinkedBlockingQueue<Record>();
        BlockingQueue<Record> defaultQueue = new LinkedBlockingQueue<Record>();

        // Create a content based record dispatcher to dispatch records to according queues based on their content
        ContentBasedRecordDispatcher recordDispatcher = new ContentBasedRecordDispatcherBuilder()
                .when(new AppleRecordPredicate()).dispatchTo(appleQueue)
                .when(new OrangeRecordPredicate()).dispatchTo(orangeQueue)
                .otherwise(defaultQueue)
                .build();

        // Build a master engine that will read records from the data source and dispatch them to worker engines
        Engine masterEngine = aNewEngine()
                .reader(new StringRecordReader(fruits))
                .processor(recordDispatcher)
                .batchProcessEventListener(new PoisonRecordBroadcaster(recordDispatcher))
                .build();

        // Build easy batch engines
        Engine workerEngine1 = buildWorkerEngine(appleQueue);
        Engine workerEngine2 = buildWorkerEngine(orangeQueue);
        Engine workerEngine3 = buildWorkerEngine(defaultQueue);

        // Create a threads pool to call Easy Batch engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Submit master and worker engines to executor service
        executorService.submit(masterEngine);
        executorService.submit(workerEngine1);
        executorService.submit(workerEngine2);
        executorService.submit(workerEngine3);

        // Shutdown executor service
        executorService.shutdown();

    }

    public static Engine buildWorkerEngine(BlockingQueue<Record> queue) {
        return aNewEngine()
                .reader(new QueueRecordReader(queue))
                .filter(new PoisonRecordFilter())
                .build();
    }

}