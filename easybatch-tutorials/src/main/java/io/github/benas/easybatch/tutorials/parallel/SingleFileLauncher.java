/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.tutorials.parallel;

import io.github.benas.easybatch.core.api.EasyBatchReport;
import io.github.benas.easybatch.core.filter.RecordNumberGreaterThanRecordFilter;
import io.github.benas.easybatch.core.filter.RecordNumberLowerThanRecordFilter;
import io.github.benas.easybatch.core.impl.EasyBatchEngine;
import io.github.benas.easybatch.core.impl.EasyBatchEngineBuilder;
import io.github.benas.easybatch.flatfile.FlatFileRecordReader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main class to run the crypto tutorial with a single input file in parallel.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class SingleFileLauncher {

    public static final String COMMENT_SEPARATOR = "####################################";

    public static void main(String[] args) throws Exception {

        System.out.println(COMMENT_SEPARATOR);
        System.out.println("Running a single Easy Batch instance");
        System.out.println(COMMENT_SEPARATOR);
        long singleInstanceStartTime = System.currentTimeMillis();
        EasyBatchEngine easyBatchEngine = new EasyBatchEngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(args[0])) //read data from secret-messages.txt
                .registerRecordProcessor(new MessageEncrypter())
                .build();

        EasyBatchReport easyBatchReport = easyBatchEngine.call();
        System.out.println("easyBatchReport = " + easyBatchReport);

        long singleInstanceEndTime = System.currentTimeMillis() - singleInstanceStartTime;

        System.out.println(COMMENT_SEPARATOR);
        System.out.println("Running two Easy Batch instances in parallel");
        System.out.println(COMMENT_SEPARATOR);
        long parallelInstancesStartTime = System.currentTimeMillis();

        // To avoid any thread-safety issues,
        // we will create 2 engines with separate instances of record readers and processors

        // Build an easy batch engine1
        EasyBatchEngine easyBatchEngine1 = new EasyBatchEngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(args[0])) //read data from secret-messages.txt
                .registerRecordFilter(new RecordNumberGreaterThanRecordFilter(5)) // filter records 6-10
                .registerRecordProcessor(new MessageEncrypter())
                .build();

        // Build an easy batch engine2
        EasyBatchEngine easyBatchEngine2 = new EasyBatchEngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(args[0])) //read data from secret-messages.txt
                .registerRecordFilter(new RecordNumberLowerThanRecordFilter(6)) // filter records 1-5
                .registerRecordProcessor(new MessageEncrypter())
                .build();

        //create a 2 threads pool to call Easy Batch engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<EasyBatchReport> easyBatchReport1 = executorService.submit(easyBatchEngine1);
        Future<EasyBatchReport> easyBatchReport2 = executorService.submit(easyBatchEngine2);

        System.out.println("Easy Batch Report 1 = " + easyBatchReport1.get());
        System.out.println("Easy Batch Report 2 = " + easyBatchReport2.get());

        executorService.shutdown();

        long parallelInstancesEndTime = System.currentTimeMillis() - parallelInstancesStartTime;

        System.out.println(COMMENT_SEPARATOR);
        System.out.println("Processing the input file with two Easy Batch instances in parallel took " + parallelInstancesEndTime + "ms");
        System.out.println("Processing the input file with a single Easy Batch instance took " + singleInstanceEndTime + "ms");
        System.out.println(COMMENT_SEPARATOR);

    }

}