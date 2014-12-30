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

package org.easybatch.tutorials.products;

import org.easybatch.core.api.Report;
import org.easybatch.core.filter.HeaderRecordFilter;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.flatfile.FlatFileRecordReader;
import org.easybatch.flatfile.dsv.DelimitedRecordMapper;

import java.io.File;

/**
* Main class to run the products statistics tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
*/
public class Launcher {

    public static void main(String[] args) throws Exception {

        // Configure the product record mapper
        DelimitedRecordMapper<Product> productMapper = new DelimitedRecordMapper<Product>(Product.class, new String[]{"id", "name", "description", "price", "published", "lastUpdate", "origin"});
        productMapper.setDelimiter("|");
        productMapper.setQualifier("\"");
        productMapper.registerTypeConverter(Origin.class, new OriginTypeConverter());

        // Build a batch engine
        Engine engine = new EngineBuilder()
                .reader(new FlatFileRecordReader(new File(args[0]))) //read data from products.csv
                .filter(new HeaderRecordFilter())
                .mapper(productMapper)
                .processor(new ProductProcessor())
                .build();

        // Run the batch engine and get execution report
        Report report = engine.call();

        // Print batch report
        System.out.println(report);

        // Get the batch computation result
        Double maxProductPrice = (Double) report.getBatchResult();

        // Print the maximum price
        System.out.println("The maximum product price for national published products is : " + maxProductPrice);

    }

}