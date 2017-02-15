package org.easybatch.json;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.Record;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiJsonFileRecordReaderTest {

    @Test
    public void allResourcesShouldBeReadInOneShot() throws Exception {
        // given
        File dir = new File("src/test/resources");
        File[] files = dir.listFiles(new JsonFileFilter());
        MultiJsonFileRecordReader multiFileRecordReader = new MultiJsonFileRecordReader(Arrays.asList(files));

        RecordCollector recordCollector = new RecordCollector();
        Job job = JobBuilder.aNewJob()
                .reader(multiFileRecordReader)
                .processor(recordCollector)
                .build();

        // when
        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
        jobExecutor.shutdown();

        // then
        List<Record> records = recordCollector.getRecords();

        // there are 12 records in json files inside "src/test/resources"
        assertThat(records).hasSize(12);

    }

    private class JsonFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith("json");
        }
    }
}