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

package org.easybatch.tutorials.pipeline.unixLike;

import org.easybatch.core.api.Report;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.core.util.GrepFilter;
import org.easybatch.core.util.StringRecordReader;

/**
* Main class to run the unix-like processing pipeline tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
*/
public class UnixLikePipelineTutorial {

    public static void main(String[] args) throws Exception {

        /*
         * Example 1: Count the number of lines containing the word java
         * The goal is to implement the equivalent of:
         * cat input.txt | grep java | wc -l
         */

        // Create the String data source (to keep it simple, use in-memory string instead of input.txt file)
        String dataSource1 =
                "Java rocks!\n" +
                "C++ sucks :-)\n" +
                "Javascript rocks!\n" +
                "Scala is fine.\n" +
                "Swift? ...";

        // Build a batch engine
        Engine engine = new EngineBuilder()
                .reader(new StringRecordReader(dataSource1))
                .filter(new GrepFilter("Java"))
                .processor(new LineCountProcessor())
                .build();

        // Run the batch engine
        Report report = engine.call();

        // Print the batch execution report
        System.out.println("Total number of lines containing java = " + report.getBatchResult());

        /*
         * Example 2: Print the name of french peoples
         * The goal is to implement the equivalent of:
         * cat input.txt | grep "france" | cut -d',' -f2 > output.txt
         */

        // Create the String data source (to keep it simple, use in-memory string instead of input.txt file, same for output)
        String dataSource2 =
                "1,jean,france\n" +
                "2,foo,usa\n" +
                "3,toto,belgium\n" +
                "4,jacques,france\n" +
                "5,nikita,japan\n";

        // Build a batch engine
        engine = new EngineBuilder()
                .reader(new StringRecordReader(dataSource2))
                .filter(new GrepFilter("france"))
                .processor(new CutProcessor(",", 1))
                .processor(new OutputProcessor(System.out))
                .build();

        engine.call();

    }

}