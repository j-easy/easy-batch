/*
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.tutorials.intermediate.headerfooter;

import java.io.File;

import javax.sql.DataSource;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.writer.FileRecordWriter;
import org.easybatch.flatfile.DelimitedRecordMarshaller;
import org.easybatch.jdbc.JdbcRecordMapper;
import org.easybatch.jdbc.JdbcRecordReader;
import org.easybatch.tutorials.common.DatabaseUtil;
import org.easybatch.tutorials.common.Tweet;

import static org.easybatch.core.job.JobBuilder.aNewJob;

/**
 * Main class to run the header/footer tutorial.
 *
 * The goal is to read tweets from a relational database and export them to a flat file.
 * The output file needs to have:
 * <ul>
 *     <li>a header with column names</li>
 *     <li>a footer with the total number of records</li>
 * </ul>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Launcher {

    public static void main(String[] args) throws Exception {

        // Output file
        File tweets = new File("target/tweets.csv");

        //Start embedded database server
        DatabaseUtil.startEmbeddedDatabase();

        // get a connection to the database
        DataSource dataSource = DatabaseUtil.getDataSource();

        // Build a batch job

        FileRecordWriter recordWriter = new FileRecordWriter(tweets);
        HeaderWriter headerCallback = new HeaderWriter();
        FooterWriter footerCallback = new FooterWriter();
        recordWriter.setHeaderCallback(headerCallback);
        recordWriter.setFooterCallback(footerCallback);

        final String[] fields = {"id", "user", "message"};
        Job job = aNewJob()
                .reader(new JdbcRecordReader(dataSource, "select * from tweet"))
                .mapper(new JdbcRecordMapper<>(Tweet.class, fields))
                .marshaller(new DelimitedRecordMarshaller<>(Tweet.class, fields))
                .writer(recordWriter)
                .batchListener(footerCallback)
                .build();
        
        // Execute the job
        JobExecutor jobExecutor = new JobExecutor();
        JobReport jobReport = jobExecutor.execute(job);
        jobExecutor.shutdown();

        System.out.println(jobReport);

        // Shutdown embedded database server and delete temporary files
        DatabaseUtil.cleanUpWorkingDirectory();

    }

}
