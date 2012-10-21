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

package net.benas.cb4j.tutorials.products;

import net.benas.cb4j.core.api.BatchEngine;
import net.benas.cb4j.core.api.FieldValidator;
import net.benas.cb4j.core.config.BatchConfiguration;
import net.benas.cb4j.core.config.BatchConfigurationException;
import net.benas.cb4j.core.util.BatchRunner;
import net.benas.cb4j.core.validator.FixedLengthFieldValidator;
import net.benas.cb4j.core.validator.FixedLengthNumericFieldValidator;
import net.benas.cb4j.core.validator.NumericFieldValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Main class to run the products tutorial
 * @author benas (md.benhassine@gmail.com)
 */
public class Launcher {

    public static void main(String[] args) {

        if (args == null || args.length < 3){
            System.err.println("[CB4J] Configuration parameters not specified, usage : ");
            System.err.println("java net.benas.cb4j.tutorials.products.Launcher path/to/data/file recordSize separator");
            System.err.println("Example : java net.benas.cb4j.tutorials.products.Launcher /data/cb4j/products.csv 4 ;");
            System.exit(1);
        }

        Properties configurationProperties = new Properties();
        configurationProperties.setProperty("input.data.path",args[0]);
        configurationProperties.setProperty("input.record.size",args[1]);
        configurationProperties.setProperty("input.field.separator", args[2]);
        configurationProperties.setProperty("input.data.skipHeader", "true");

        BatchConfiguration batchConfiguration = new BatchConfiguration(configurationProperties);

        /*
        * Registering field validators
        */
        NumericFieldValidator numericFieldValidator = new NumericFieldValidator();
        batchConfiguration.registerFieldValidator(0, numericFieldValidator);
        batchConfiguration.registerFieldValidator(1, numericFieldValidator);
        batchConfiguration.registerFieldValidator(3, numericFieldValidator);

        /*
        * Registering record mapper and processor
        */
        batchConfiguration.registerRecordMapper(new ProductMapper());
        batchConfiguration.registerRecordProcessor(new ProductProcessor());

        /*
         * Configure and run the engine
         */
        try {
            batchConfiguration.configure();
            BatchEngine batchEngine = new ProductBatchEngine(batchConfiguration);
            BatchRunner batchRunner = new BatchRunner(batchEngine);
            batchRunner.run();
        } catch (BatchConfigurationException e) {
            System.err.println(e.getMessage());
        }

    }

}
