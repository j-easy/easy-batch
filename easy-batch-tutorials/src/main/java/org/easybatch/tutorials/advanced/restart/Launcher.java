package org.easybatch.tutorials.advanced.restart;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.writer.FileRecordWriter;
import org.easybatch.flatfile.FlatFileRecordReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Launcher {

    public static void main(String[] args) throws Exception {

        File journal = new File("target/checkpoint.oplog");
        createJournal(journal.toPath());
        CheckPointListener checkPointListener = new CheckPointListener(journal);

        Job job = new JobBuilder()
                .batchSize(3)
                .reader(new FlatFileRecordReader(new File("src/main/resources/data/tweets.csv")))
                .writer(new BuggyWriter(new FileRecordWriter(new FileWriter("target/tweets-out.csv", true))))
                .pipelineListener(checkPointListener)
                .writerListener(checkPointListener)
                .jobListener(checkPointListener)
                .build();

        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
        jobExecutor.shutdown();
    }

    private static void createJournal(Path journal) throws IOException {
        if (!Files.exists(journal)) {
            Files.createFile(journal);
        }
    }
}
