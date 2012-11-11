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

import net.benas.cb4j.core.config.BatchConfiguration;
import net.benas.cb4j.core.impl.DefaultBatchEngineImpl;

import java.util.Map;

/**
 * Custom engine that overrides the default shutdown method with products prices by category report
 * @author benas (md.benhassine@gmail.com)
 */
public class ProductBatchEngine extends DefaultBatchEngineImpl {

    public ProductBatchEngine(BatchConfiguration batchConfiguration) {
        super(batchConfiguration);
    }

    @Override
    public void shutdown(){

        super.shutdown();// generate default report

        System.out.println("******* Products Report ******* ");

        //show min prices by category
        System.out.println("Minimum prices by category : ");
        Map<Long, Double> minPrices = ((ProductProcessor) recordProcessor).getMinPricesByCategory();
        for (Map.Entry<Long, Double> entry : minPrices.entrySet()) {
            System.out.println("\tMin price for category " + entry.getKey() + " = " + entry.getValue());
        }

        //show max prices by category
        System.out.println("Maximum prices by category : ");
        Map<Long, Double> maxPrices = ((ProductProcessor) recordProcessor).getMaxPricesByCategory();
        for (Map.Entry<Long, Double> entry : maxPrices.entrySet()) {
            System.out.println("\tMax price for category " + entry.getKey() + " = " + entry.getValue());
        }

    }

}
