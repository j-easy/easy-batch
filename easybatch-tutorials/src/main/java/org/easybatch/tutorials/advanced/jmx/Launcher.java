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

package org.easybatch.tutorials.advanced.jmx;

import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.core.reader.StringRecordReader;

/**
* Main class to run the JMX tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
*/
public class Launcher {

    public static void main(String[] args) throws Exception {

        // Create the String data source
        String dataSource =
                "1,foo,easy batch rocks! #EasyBatch\n" +
                "2,foo,easy batch rocks! #EasyBatch\n" +
                "3,foo,easy batch rocks! #EasyBatch\n" +
                "4,foo,easy batch rocks! #EasyBatch\n" +
                "5,foo,easy batch rocks! #EasyBatch\n" +
                "6,foo,easy batch rocks! #EasyBatch\n" +
                "7,foo,easy batch rocks! #EasyBatch\n" +
                "8,bar,@foo I do confirm :-)";

        // Build a batch engine
        Engine engine = new EngineBuilder()
                .reader(new StringRecordReader(dataSource))
                .processor(new TweetSlowProcessor())
                .enableJMX(true)
                .build();

        // Run the batch engine
        engine.call();

    }

}