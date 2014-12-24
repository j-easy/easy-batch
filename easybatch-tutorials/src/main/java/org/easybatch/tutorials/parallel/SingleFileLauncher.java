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

package org.easybatch.tutorials.parallel;

import org.easybatch.core.api.Report;
import org.easybatch.core.filter.RecordNumberGreaterThanFilter;
import org.easybatch.core.filter.RecordNumberLowerThanFilter;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.flatfile.FlatFileRecordReader;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Main class to run the crypto tutorial with a single input file in parallel.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class SingleFileLauncher {

    public static final String COMMENT_SEPARATOR = "####################################";

    public static void main(String[] args) throws Exception {

        System.out.println(COMMENT_SEPARATOR);
        System.out.println("Running a single batch instance");
        System.out.println(COMMENT_SEPARATOR);
        long singleInstanceStartTime = System.nanoTime();
        Engine engine = new EngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(new File(args[0]))) //read data from secret-messages.txt
                .registerRecordProcessor(new MessageEncrypter())
                .build();

        Report report = engine.call();
        System.out.println(report);

        long singleInstanceEndTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - singleInstanceStartTime);

        System.out.println(COMMENT_SEPARATOR);
        System.out.println("Running two batch instances in parallel");
        System.out.println(COMMENT_SEPARATOR);
        long parallelInstancesStartTime = System.nanoTime();

        // To avoid any thread-safety issues,
        // we will create 2 engines with separate instances of record readers and processors

        // Build a batch engine1
        Engine engine1 = new EngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(new File(args[0]))) //read data from secret-messages.txt
                .registerRecordFilter(new RecordNumberGreaterThanFilter(5)) // filter records 6-10
                .registerRecordProcessor(new MessageEncrypter())
                .build();

        // Build a batch engine2
        Engine engine2 = new EngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(new File(args[0]))) //read data from secret-messages.txt
                .registerRecordFilter(new RecordNumberLowerThanFilter(6)) // filter records 1-5
                .registerRecordProcessor(new MessageEncrypter())
                .build();

        //create a 2 threads pool to call batch engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Report> batchReport1 = executorService.submit(engine1);
        Future<Report> batchReport2 = executorService.submit(engine2);

        System.out.println("Batch Report 1 = " + batchReport1.get());
        System.out.println("Batch Report 2 = " + batchReport2.get());

        executorService.shutdown();

        long parallelInstancesEndTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - parallelInstancesStartTime);

        System.out.println(COMMENT_SEPARATOR);
        System.out.println("Processing the input file with two Easy Batch instances in parallel took " + parallelInstancesEndTime + "ms");
        System.out.println("Processing the input file with a single Easy Batch instance took " + singleInstanceEndTime + "ms");
        System.out.println(COMMENT_SEPARATOR);

    }

}