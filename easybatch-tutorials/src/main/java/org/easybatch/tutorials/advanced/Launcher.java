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

package org.easybatch.tutorials.advanced;

import org.easybatch.core.api.Report;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.flatfile.FlatFileRecordReader;
import org.easybatch.flatfile.dsv.DelimitedRecordMapper;
import org.easybatch.jdbc.JdbcRecordMapper;
import org.easybatch.jdbc.JdbcRecordReader;
import org.easybatch.xml.XmlRecordMapper;
import org.easybatch.xml.XmlRecordReader;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
* Main class to run the advanced ETL tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
*/
public class Launcher {

    public static void main(String[] args) throws Exception {

        //do not let hsqldb reconfigure java.util.logging used by easy batch
        System.setProperty("hsqldb.reconfig_logging", "false");

        /*
         * Start embedded database server
         */
        DatabaseUtil.startEmbeddedDatabase();
        DatabaseUtil.initializeSessionFactory();

        // Build an easy batch engine to read greetings from csv file
        Engine csvEngine = new EngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(new File(args[0])))
                .registerRecordMapper(new DelimitedRecordMapper<Greeting>(Greeting.class, new String[]{"id","name"}))
                .registerRecordProcessor(new GreetingDataLoader())
                .build();

        // Build an easy batch engine to read greetings from xml file
        Engine xmlEngine = new EngineBuilder()
                .registerRecordReader(new XmlRecordReader("greeting", new File(args[1])))
                .registerRecordMapper(new XmlRecordMapper<Greeting>(Greeting.class))
                .registerRecordProcessor(new GreetingDataLoader())
                .build();

        //create a 2 threads pool to call Easy Batch engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Report> batchReport1 = executorService.submit(csvEngine);
        Future<Report> batchReport2 = executorService.submit(xmlEngine);

        batchReport1.get();
        batchReport2.get();

        executorService.shutdown();

        // Build an easy batch engine to generate JSON products data from the database
        Engine jsonEngine = new EngineBuilder()
                .registerRecordReader(new JdbcRecordReader(DatabaseUtil.getDatabaseConnection(), "select * from greeting"))
                .registerRecordMapper(new JdbcRecordMapper<Greeting>(Greeting.class))
                .registerRecordProcessor(new GreetingJsonGenerator())
                .build();

        jsonEngine.call();

        //close database session factory
        DatabaseUtil.closeSessionFactory();

    }

}