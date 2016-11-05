package org.easybatch.json;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.reader.MultiFileRecordReader;
import org.easybatch.core.record.Record;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiJsonFileRecordReaderTest {

    @Test
    public void allResourcesShouldBeReadInOneShot() throws Exception {
        MultiFileRecordReader multiFileRecordReader = new MultiFileRecordReader(
                new File("src/test/resources"),
                "json",
                JsonFileRecordReader.class);

        RecordCollector recordCollector = new RecordCollector();
        Job job = JobBuilder.aNewJob()
                .reader(multiFileRecordReader)
                .processor(recordCollector)
                .build();

        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
        jobExecutor.shutdown();

        List<Record> records = recordCollector.getRecords();

        // there are 12 records in json files inside "src/test/resources"
        assertThat(records).hasSize(12);

    }

}