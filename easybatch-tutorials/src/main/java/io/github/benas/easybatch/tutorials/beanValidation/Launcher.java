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

package io.github.benas.easybatch.tutorials.beanValidation;

import io.github.benas.easybatch.core.api.EasyBatchReport;
import io.github.benas.easybatch.core.filter.StartsWithStringRecordFilter;
import io.github.benas.easybatch.core.impl.EasyBatchEngine;
import io.github.benas.easybatch.core.impl.EasyBatchEngineBuilder;
import io.github.benas.easybatch.flatfile.FlatFileRecordReader;
import io.github.benas.easybatch.flatfile.dsv.DsvRecordMapper;
import io.github.benas.easybatch.validation.BeanValidationRecordValidator;

/**
* Main class to run the bean validation tutorial.
 *
* @author benas (md.benhassine@gmail.com)
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

        // Configure the product record mapper
        DsvRecordMapper<Product> productMapper = new DsvRecordMapper<Product>(
                Product.class,
                new String[]{"productId","name", "description", "price","published", "lastUpdate" });
        productMapper.setDelimiter("|");
        productMapper.setQualifier("\"");

        // Build an easy batch engine
        EasyBatchEngine easyBatchEngine = new EasyBatchEngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(args[0])) //read data from products-jsr303.csv
                .registerRecordFilter(new StartsWithStringRecordFilter("#"))
                .registerRecordMapper(productMapper)
                .registerRecordValidator(new BeanValidationRecordValidator<Product>())
                .registerRecordProcessor(new ProductProcessor())
                .build();

        // Run easy batch engine
        EasyBatchReport easyBatchReport = easyBatchEngine.call();
        System.out.println("easyBatchReport = " + easyBatchReport);

        /*
         * Dump product table to checks inserted data
         */
        DatabaseUtil.closeSessionFactory();
        DatabaseUtil.dumpProductTable();

    }

}