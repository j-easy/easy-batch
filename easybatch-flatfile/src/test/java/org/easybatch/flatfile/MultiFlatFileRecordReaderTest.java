package org.easybatch.flatfile;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.reader.MultiFileRecordReader;
import org.easybatch.core.record.Record;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiFlatFileRecordReaderTest {

    @Test
    public void allResourcesShouldBeReadInOneShot() throws Exception {
        MultiFileRecordReader multiFileRecordReader = new MultiFileRecordReader(
                new File("src/test/resources"),
                new TxtFileFilter(),
                FlatFileRecordReader.class);

        RecordCollector recordCollector = new RecordCollector();
        Job job = JobBuilder.aNewJob()
                .reader(multiFileRecordReader)
                .processor(recordCollector)
                .build();

        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
        jobExecutor.shutdown();

        List<Record> records = recordCollector.getRecords();

        // there are 6 records in foo.txt, bar.txt and empty.txt
        // Note how (middle or trailing) empty lines should be read as well
        assertThat(records).hasSize(6);

    }

    private class TxtFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith("txt");
        }
    }
}