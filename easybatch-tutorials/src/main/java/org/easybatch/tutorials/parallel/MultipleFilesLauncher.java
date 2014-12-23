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
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.flatfile.FlatFileRecordReader;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main class to run the crypto tutorial with multiple input files in parallel.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class MultipleFilesLauncher {

    public static void main(String[] args) throws Exception {

        // To avoid any thread-safety issues,
        // we will create 2 engines with separate instances of record readers and processors

        // Build a  batch engine1
        Engine engine1 = new EngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(new File(args[0]))) //read data from secret-messages-part1.txt
                .registerRecordProcessor(new MessageEncrypter())
                .build();

        // Build a batch engine2
        Engine engine2 = new EngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(new File(args[1]))) //read data from secret-messages-part2.txt
                .registerRecordProcessor(new MessageEncrypter())
                .build();

        //create a 2 threads pool to call Easy Batch engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Report> report1 = executorService.submit(engine1);
        Future<Report> report2 = executorService.submit(engine2);

        System.out.println("Batch Report 1 = " + report1.get());
        System.out.println("Batch Report 2 = " + report2.get());

        executorService.shutdown();

    }

}