package org.jeasy.batch.tutorials.intermediate.csv2xml;

import org.jeasy.batch.core.filter.HeaderRecordFilter;
import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobExecutor;
import org.jeasy.batch.core.writer.FileRecordWriter;
import org.jeasy.batch.flatfile.DelimitedRecordMapper;
import org.jeasy.batch.flatfile.FlatFileRecordReader;
import org.jeasy.batch.tutorials.common.Tweet;
import org.jeasy.batch.xml.XmlRecordMarshaller;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.jeasy.batch.core.job.JobBuilder.aNewJob;

/**
 * Main class to launch the CSV to XML tutorial.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Launcher {

    public static void main(String[] args) throws Exception {

        Path csvTweets = Paths.get("src/main/resources/data/tweets.csv");
        Path xmlTweets = Paths.get("target/tweets.xml");

        FileRecordWriter recordWriter = new FileRecordWriter(xmlTweets);
        recordWriter.setHeaderCallback(new HeaderWriter());
        recordWriter.setFooterCallback(new FooterWriter());

        Job job = aNewJob()
                .reader(new FlatFileRecordReader(csvTweets))
                .filter(new HeaderRecordFilter())
                .mapper(new DelimitedRecordMapper<>(Tweet.class, "id", "user", "message"))
                .marshaller(new XmlRecordMarshaller<>(Tweet.class))
                .writer(recordWriter)
                .build();

        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
        jobExecutor.shutdown();
    }

}
