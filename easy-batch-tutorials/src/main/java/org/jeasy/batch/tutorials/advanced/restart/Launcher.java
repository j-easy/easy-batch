package org.jeasy.batch.tutorials.advanced.restart;

import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobBuilder;
import org.jeasy.batch.core.job.JobExecutor;
import org.jeasy.batch.core.writer.FileRecordWriter;
import org.jeasy.batch.flatfile.FlatFileRecordReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Launcher {

    public static void main(String[] args) throws Exception {

        Path journal = Paths.get("target/checkpoint.oplog");
        createJournal(journal);
        CheckPointListener checkPointListener = new CheckPointListener(journal);

        FlatFileRecordReader recordReader = new FlatFileRecordReader(Paths.get("src/main/resources/data/tweets.csv"));
        FileRecordWriter recordWriter = new FileRecordWriter(Paths.get("target/tweets-out.csv"));
        recordWriter.setAppend(true);

        Job job = new JobBuilder()
                .batchSize(3)
                .reader(recordReader)
                .writer(new BuggyWriter(recordWriter))
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
