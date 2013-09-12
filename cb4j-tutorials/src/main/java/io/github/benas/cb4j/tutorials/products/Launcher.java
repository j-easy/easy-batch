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

package io.github.benas.cb4j.tutorials.products;

import io.github.benas.cb4j.core.api.BatchEngine;
import io.github.benas.cb4j.core.api.BatchReport;
import io.github.benas.cb4j.core.config.BatchConfiguration;
import io.github.benas.cb4j.core.config.BatchConfigurationBuilder;
import io.github.benas.cb4j.core.config.BatchConfigurationException;
import io.github.benas.cb4j.core.impl.DefaultBatchEngineImpl;
import io.github.benas.cb4j.core.util.BatchRunner;
import io.github.benas.cb4j.core.util.RecordType;
import io.github.benas.cb4j.core.validator.NumericFieldValidator;

import java.util.Map;

/**
 * Main class to run the products tutorial
 * @author benas (md.benhassine@gmail.com)
 */
public class Launcher {

    public static void main(String[] args) {

        if (args == null || args.length < 3){
            System.err.println("[CB4J] Configuration parameters not specified, usage : ");
            System.err.println("java io.github.benas.cb4j.tutorials.products.Launcher path/to/data/file recordSize recordType fieldsLength");
            System.err.println("Example : java io.github.benas.cb4j.tutorials.products.Launcher /data/cb4j/products.flr 3 FLR 8 3 4");
            System.exit(1);
        }

        /*
         * Create a batch configuration instance
         */
        BatchConfiguration batchConfiguration = new BatchConfigurationBuilder()
                .inputDataFile(args[0])
                .recordSize(Integer.parseInt(args[1]))
                .recordType(RecordType.valueOf(args[2]))
                .fieldsLength(new int[]{Integer.parseInt(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5])})
                .build();

        /*
        * Registering field validators
        */
        NumericFieldValidator numericFieldValidator = new NumericFieldValidator();
        batchConfiguration.registerFieldValidator(0, numericFieldValidator);
        batchConfiguration.registerFieldValidator(1, numericFieldValidator);
        batchConfiguration.registerFieldValidator(2, numericFieldValidator);

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
            BatchEngine batchEngine = new DefaultBatchEngineImpl(batchConfiguration);
            BatchRunner batchRunner = new BatchRunner(batchEngine);
            BatchReport batchReport = batchRunner.run();
            ProductBatchResultHolder batchResultHolder = (ProductBatchResultHolder) batchReport.getBatchResultHolder();

            System.out.println("******* Products Report ******* ");

            //show min prices by category
            System.out.println("Minimum prices by category : ");

            Map<Long, Double> minPrices = batchResultHolder.getMinPricesByCategory();
            for (Map.Entry<Long, Double> entry : minPrices.entrySet()) {
                System.out.println("\tMin price for category " + entry.getKey() + " = " + entry.getValue());
            }

            //show max prices by category
            System.out.println("Maximum prices by category : ");
            Map<Long, Double> maxPrices = batchResultHolder.getMaxPricesByCategory();
            for (Map.Entry<Long, Double> entry : maxPrices.entrySet()) {
                System.out.println("\tMax price for category " + entry.getKey() + " = " + entry.getValue());
            }
        } catch (BatchConfigurationException e) {
            System.err.println(e.getMessage());
        }

    }

}
