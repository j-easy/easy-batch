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

package org.easybatch.tutorials.helloworld.memory;

import org.easybatch.core.api.Report;
import org.easybatch.core.filter.HeaderRecordFilter;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.core.util.StringRecordReader;
import org.easybatch.flatfile.dsv.DelimitedRecordMapper;
import org.easybatch.tutorials.common.Greeting;
import org.easybatch.tutorials.common.GreetingProcessor;
import org.easybatch.tutorials.common.GreetingTransformer;

/**
* Main class to run the hello world in-memory String data source tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
*/
public class StringDataSourceTutorialLauncher {

    public static void main(String[] args) throws Exception {

        // Create the String data source
        String dataSource =
                "id,name\n" +
                "1,foo\n" +
                "2,bar";

        // Build a batch engine
        Engine engine = new EngineBuilder()
                .reader(new StringRecordReader(dataSource))
                .filter(new HeaderRecordFilter())
                .mapper(new DelimitedRecordMapper<Greeting>(Greeting.class, new String[]{"id", "name"}))
                .processor(new GreetingTransformer())
                .processor(new GreetingProcessor())
                .build();

        // Run the batch engine
        Report report = engine.call();

        // Print the batch execution report
        System.out.println(report);

    }

}