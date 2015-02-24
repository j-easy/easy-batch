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

import org.easybatch.core.api.RecordFilter;
import org.easybatch.core.api.Report;
import org.easybatch.core.filter.RecordNumberGreaterThanFilter;
import org.easybatch.core.filter.RecordNumberLowerThanFilter;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.flatfile.FlatFileRecordReader;
import org.easybatch.tutorials.basic.helloworld.TweetProcessor;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main class to run the parallel tutorial with data source filtering.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ParallelTutorialWithDataFiltering {

    private static final int THREAD_POOL_SIZE = 2;

    public static void main(String[] args) throws Exception {

        // Input file tweets.csv
        File tweets = new File(ParallelTutorialWithDataFiltering.class
                            .getResource("/org/easybatch/tutorials/advanced/parallel/tweets.csv").toURI());

        // Build worker engines
        // worker engine 1: process data from tweets.csv, filter records 6-10
        Engine engine1 = buildEngine(tweets, new RecordNumberGreaterThanFilter(5));
        // worker engine 2: process data from tweets.csv, filter records 1-5
        Engine engine2 = buildEngine(tweets, new RecordNumberLowerThanFilter(6));

        //create a 2 threads pool to call worker engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        Future<Report> report1 = executorService.submit(engine1);
        Future<Report> report2 = executorService.submit(engine2);

        System.out.println("Report 1 = " + report1.get());
        System.out.println("Report 2 = " + report2.get());

        executorService.shutdown();

    }

    private static Engine buildEngine(File file, RecordFilter recordFilter) throws Exception{
        return new EngineBuilder()
                .reader(new FlatFileRecordReader(file))
                .filter(recordFilter)
                .processor(new TweetProcessor())
                .build();
    }

}