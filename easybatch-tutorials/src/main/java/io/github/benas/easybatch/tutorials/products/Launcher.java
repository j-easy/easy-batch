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

package io.github.benas.easybatch.tutorials.products;

import io.github.benas.easybatch.core.api.EasyBatchReport;
import io.github.benas.easybatch.core.impl.EasyBatchEngine;
import io.github.benas.easybatch.core.impl.EasyBatchEngineBuilder;
import io.github.benas.easybatch.flatfile.FlatFileRecordReader;
import io.github.benas.easybatch.flatfile.flr.FixedLengthRecordMapper;

/**
* Main class to run the products statistics tutorial.
 *
* @author benas (md.benhassine@gmail.com)
*/
public class Launcher {

    public static void main(String[] args) throws Exception {

        // Build an easy batch engine
        EasyBatchEngine easyBatchEngine = new EasyBatchEngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(args[0]))
                .registerRecordMapper(new FixedLengthRecordMapper<Product>(Product.class, new int[]{8, 4}, new String[]{"productId", "price"}))
                .registerRecordProcessor(new ProductProcessor())
                .build();

        // Run easy batch engine
        EasyBatchReport easyBatchReport = easyBatchEngine.call();

        // Get the batch computation result
        Double batchResult = (Double) easyBatchReport.getEasyBatchResult();

        // Print the maximum price
        System.out.println("The maximum product price is : " + batchResult);

    }

}