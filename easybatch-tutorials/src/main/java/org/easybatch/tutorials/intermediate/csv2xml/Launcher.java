package org.easybatch.tutorials.intermediate.csv2xml;

import org.easybatch.core.filter.HeaderRecordFilter;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.writer.FileRecordWriter;
import org.easybatch.flatfile.DelimitedRecordMapper;
import org.easybatch.flatfile.FlatFileRecordReader;
import org.easybatch.tutorials.common.Tweet;
import org.easybatch.xml.XmlRecordMarshaller;
import org.easybatch.xml.XmlWrapperTagWriter;

import java.io.File;
import java.io.FileWriter;

import static org.easybatch.core.job.JobBuilder.aNewJob;

/**
 * Main class to launch the CSV to XML tutorial.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Launcher {

    public static void main(String[] args) throws Exception {

        File csvTweets = new File("src/main/resources/data/tweets.csv");
        File xmlTweets = new File("target/tweets.xml");

        Job job = aNewJob()
                .reader(new FlatFileRecordReader(csvTweets))
                .filter(new HeaderRecordFilter())
                .mapper(new DelimitedRecordMapper<>(Tweet.class, "id", "user", "message"))
                .marshaller(new XmlRecordMarshaller<>(Tweet.class))
                .writer(new FileRecordWriter(new FileWriter(xmlTweets, true)))
                .jobListener(new XmlWrapperTagWriter(xmlTweets, "tweets"))
                .build();

        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
        jobExecutor.shutdown();
    }

}
