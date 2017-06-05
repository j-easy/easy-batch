/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.tutorials.basic.wordcount;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.reader.StringRecordReader;

import java.util.Map;

import static org.easybatch.core.job.JobBuilder.aNewJob;

/**
* Main class to run the word count tutorial.
 *
* @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
*/
public class Launcher {

    public static void main(String[] args) {

        // Create a data source
        String dataSource =
                "big data is often\n" +
                "not so big";

        // Build a batch job
        WordCounter wordCounter = new WordCounter();
        Job job = aNewJob()
                .reader(new StringRecordReader(dataSource))
                .mapper(new LineTokenizer())
                .processor(wordCounter)
                .build();

        // Execute the job
        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
        jobExecutor.shutdown();
        
        // Get result
        Map<String, Integer> words = wordCounter.getCount();

        System.out.println(words);

    }

}
