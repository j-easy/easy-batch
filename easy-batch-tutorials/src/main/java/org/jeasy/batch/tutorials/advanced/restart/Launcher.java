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

        Path dataSource = Paths.get(args.length != 0 ? args[0] : "easy-batch-tutorials/src/main/resources/data/tweets.csv");
        Path dataSink = Paths.get(args.length != 0 ? args[1] : "easy-batch-tutorials/target/tweets-out.csv");
        String journalFile = args.length != 0 ? args[2] : "easy-batch-tutorials/target/checkpoint.oplog";

        Path journal = Paths.get(journalFile);
        createJournal(journal);
        CheckPointListener checkPointListener = new CheckPointListener(journal);

        FlatFileRecordReader recordReader = new FlatFileRecordReader(dataSource);
        FileRecordWriter recordWriter = new FileRecordWriter(dataSink);
        recordWriter.setAppend(true);

        Job job = new JobBuilder<String, String>()
                .batchSize(3)
                .reader(recordReader)
                .writer(new BuggyWriter<>(recordWriter))
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
