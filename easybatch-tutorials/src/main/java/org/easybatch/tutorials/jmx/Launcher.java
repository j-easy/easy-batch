/*
 * The MIT License
 *
 *   Copyright (c) 2014, Mahmoud Ben Hassine (md.benhassine@gmail.com)
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

package org.easybatch.tutorials.jmx;

import org.easybatch.core.api.EasyBatchReport;
import org.easybatch.core.impl.EasyBatchEngine;
import org.easybatch.core.impl.EasyBatchEngineBuilder;
import org.easybatch.flatfile.FlatFileRecordReader;
import org.easybatch.core.filter.StartsWithStringRecordFilter;
import org.easybatch.flatfile.dsv.DelimitedRecordMapper;
import org.easybatch.tutorials.common.Greeting;
import org.easybatch.validation.BeanValidationRecordValidator;

import java.io.File;

/**
* Main class to run the hello world tutorial with a slow processor.
 *
* @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
*/
public class Launcher {

    public static void main(String[] args) throws Exception {

        // Build an easy batch engine
        EasyBatchEngine easyBatchEngine = new EasyBatchEngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(new File(args[0])))
                .registerRecordFilter(new StartsWithStringRecordFilter("#"))
                .registerRecordMapper(new DelimitedRecordMapper<Greeting>(Greeting.class, new String[]{"id", "name"}))
                .registerRecordValidator(new BeanValidationRecordValidator<Greeting>())
                .registerRecordProcessor(new GreetingSlowProcessor())
                .build();

        // Run easy batch engine
        EasyBatchReport easyBatchReport = easyBatchEngine.call();

        // Print the batch execution report
        System.out.println(easyBatchReport);

    }

}