/*
 * The MIT License
 *
 *  Copyright (c) 2013, benas (md.benhassine@gmail.com)
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

package io.github.benas.cb4j.integration.beanValidation;

import io.github.benas.cb4j.core.api.BatchEngine;
import io.github.benas.cb4j.core.config.BatchConfiguration;
import io.github.benas.cb4j.core.config.BatchConfigurationBuilder;
import io.github.benas.cb4j.core.config.BatchConfigurationException;
import io.github.benas.cb4j.core.impl.DefaultBatchEngineImpl;
import io.github.benas.cb4j.core.util.BatchRunner;
import io.github.benas.cb4j.core.validator.NumericFieldValidator;
import io.github.benas.cb4j.integration.common.GreetingProcessor;

/**
 * Main class to run the Hello World tutorial with Bean Validation API integration.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class BeanValidationLauncher {

    public static void main(String[] args) {

        if (args == null || args.length == 0 ){
            System.err.println("[CB4J] Configuration parameters not specified, usage : ");
            System.err.println("java io.github.benas.cb4j.integration.beanValidation.BeanValidationLauncher path/to/data/file");
            System.err.println("Example : java io.github.benas.cb4j.integration.beanValidation.BeanValidationLauncher /data/cb4j/persons-jsr303.csv");
            System.exit(1);
        }

        /*
         * Create a batch configuration instance
         */
        BatchConfiguration batchConfiguration = new BatchConfigurationBuilder()
                .inputDataFile(args[0])
                .skipHeader(true)
                .build();

        /*
        * Registering field validators
        */
        batchConfiguration.registerFieldValidator(0, new NumericFieldValidator());

        /*
        * Registering record processor
        */
        batchConfiguration.registerRecordProcessor(new GreetingProcessor());

        /*
         * Configure and run the engine
         */
        try {
            batchConfiguration.configure();
            BatchEngine batchEngine = new DefaultBatchEngineImpl(batchConfiguration);
            BatchRunner batchRunner = new BatchRunner(batchEngine);
            batchRunner.run();
        } catch (BatchConfigurationException e) {
            System.err.println(e.getMessage());
        }

    }
}
