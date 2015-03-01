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

package org.easybatch.tutorials.advanced.cbrd.files;

import org.easybatch.core.api.Record;
import org.easybatch.core.dispatcher.ContentBasedRecordDispatcher;
import org.easybatch.core.dispatcher.ContentBasedRecordDispatcherBuilder;
import org.easybatch.core.dispatcher.PoisonRecordBroadcaster;
import org.easybatch.core.filter.FileExtensionFilter;
import org.easybatch.core.filter.PoisonRecordFilter;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.reader.FileRecordReader;
import org.easybatch.core.reader.QueueRecordReader;

import java.io.File;
import java.util.Arrays;
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
public class FilesParallelProcessingTutorial {

    private static final int THREAD_POOL_SIZE = 3;

    public static void main(String[] args) throws Exception {

        File directory = new File(args[0]);

        // Create queues
        BlockingQueue<Record> csvQueue = new LinkedBlockingQueue<Record>();
        BlockingQueue<Record> xmlQueue = new LinkedBlockingQueue<Record>();

        // Create a content based record dispatcher to dispatch records based on their content
        ContentBasedRecordDispatcher recordDispatcher = new ContentBasedRecordDispatcherBuilder()
                .when(new CsvFilePredicate()).dispatchTo(csvQueue)
                .when(new XmlFilePredicate()).dispatchTo(xmlQueue)
                .build();

        // Build a master engine that will read files from the directory and dispatch them to worker engines
        Engine masterEngine = aNewEngine()
                .reader(new FileRecordReader(directory))
                .filter(new FileExtensionFilter(Arrays.asList(".log", ".tmp")))
                .processor(recordDispatcher)
                .batchProcessEventListener(new PoisonRecordBroadcaster(recordDispatcher))
                .build();

        // Build easy batch engines
        Engine workerEngine1 = buildWorkerEngine(csvQueue);
        Engine workerEngine2 = buildWorkerEngine(xmlQueue);

        // Create a threads pool to call Easy Batch engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Submit master and worker engines to executor service
        executorService.submit(masterEngine);
        executorService.submit(workerEngine1);
        executorService.submit(workerEngine2);

        // Shutdown executor service
        executorService.shutdown();

    }

    public static Engine buildWorkerEngine(BlockingQueue<Record> queue) {
        return aNewEngine()
                .reader(new QueueRecordReader(queue))
                .filter(new PoisonRecordFilter())
                .processor(new DummyFileProcessor())
                .build();
    }

}