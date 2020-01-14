package org.easybatch.tutorials.advanced.metric;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.IterableRecordReader;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.StandardOutputRecordWriter;

import java.util.Random;

import static java.util.Arrays.asList;

/**
 * Main class to run the custom metric tutorial.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Launcher {

    public static void main(String[] args) {

        RecordProcessingTimeCalculator recordProcessingTimeCalculator = new RecordProcessingTimeCalculator();

        // Create a job
        Job job = new JobBuilder()
                .reader(new IterableRecordReader(asList("foo", "bar")))
                .processor(new RandomTimeProcessor())
                .writer(new StandardOutputRecordWriter())
                .batchSize(1)
                .pipelineListener(recordProcessingTimeCalculator)
                .jobListener(recordProcessingTimeCalculator)
                .build();

        // Execute the job
        JobExecutor jobExecutor = new JobExecutor();
        JobReport report = jobExecutor.execute(job);
        jobExecutor.shutdown();

        // Print the job execution report
        System.out.println(report);
    }

    private static class RandomTimeProcessor implements RecordProcessor {
        private final Random random = new Random();
        @Override
        public Record processRecord(Record record) throws Exception {
            Thread.sleep(random.nextInt(1000));
            return record;
        }
    }
}
