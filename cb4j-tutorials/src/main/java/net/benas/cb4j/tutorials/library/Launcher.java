/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.tutorials.library;

import net.benas.cb4j.core.api.BatchEngine;
import net.benas.cb4j.core.api.FieldValidator;
import net.benas.cb4j.core.config.BatchConfiguration;
import net.benas.cb4j.core.config.BatchConfigurationException;
import net.benas.cb4j.core.util.BatchRunner;
import net.benas.cb4j.core.validator.FixedLengthNumericFieldValidator;
import net.benas.cb4j.core.validator.NotEmptyFieldValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Main class to run the library tutorial
 * @author benas (md.benhassine@gmail.com)
 */
public class Launcher {

    public static void main(String[] args) {

        if (args == null || args.length < 2){
            System.err.println("[CB4J] Configuration parameters not specified, usage : ");
            System.err.println("java net.benas.cb4j.tutorials.library.Launcher path/to/data/file recordSize qualifier");
            System.err.println("Example : java net.benas.cb4j.tutorials.library.Launcher /data/cb4j/books.csv 4 ' ");
            System.exit(1);
        }

        /*
         * Start embedded database server
         */
        DatabaseUtil.startEmbeddedDatabase();

        /*
         * Set configuration parameters
         */
        Properties configurationProperties = new Properties();
        configurationProperties.setProperty("input.data.path",args[0]);
        configurationProperties.setProperty("input.record.size",args[1]);
        configurationProperties.setProperty("input.field.qualifier",args[2]);

        BatchConfiguration batchConfiguration = new BatchConfiguration(configurationProperties);

        /*
         * Register field validators
        */
        List<FieldValidator> isbnValidators = new ArrayList<FieldValidator>();
        NotEmptyFieldValidator notEmptyFieldValidator = new NotEmptyFieldValidator();
        isbnValidators.add(notEmptyFieldValidator);
        isbnValidators.add(new FixedLengthNumericFieldValidator(13));

        batchConfiguration.registerFieldValidators(0, isbnValidators);
        batchConfiguration.registerFieldValidator(1, notEmptyFieldValidator);
        batchConfiguration.registerFieldValidator(2, notEmptyFieldValidator);
        batchConfiguration.registerFieldValidator(3, notEmptyFieldValidator);

        /*
         * Register record mapper and processor
        */
        batchConfiguration.registerRecordMapper(new BookMapper());
        batchConfiguration.registerRecordProcessor(new BookProcessor());

        /*
         * Configure and run the engine
         */
        try {
            batchConfiguration.configure();
            BatchEngine batchEngine = new BookLibraryLoaderEngine(batchConfiguration);
            BatchRunner batchRunner = new BatchRunner(batchEngine);
            batchRunner.run();
        } catch (BatchConfigurationException e) {
            System.err.println(e.getMessage());
        }

        /*
         * Dump Book table to checks inserted data
         */
        DatabaseUtil.dumpBookTable();

    }

}
