package org.easybatch.xml;

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

public class MultiXmlFileRecordReaderTest {

    @Test
    public void allResourcesShouldBeReadInOneShot() throws Exception {
        // given
        File dir = new File("src/test/resources");
        File[] files = dir.listFiles(new XmlFileFilter());
        MultiXmlFileRecordReader multiFileRecordReader = new MultiXmlFileRecordReader(Arrays.asList(files), "website");

        RecordCollector recordCollector = new RecordCollector();
        Job job = JobBuilder.aNewJob()
                .reader(multiFileRecordReader)
                .processor(recordCollector)
                .build();

        // when
        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
        jobExecutor.shutdown();

        List<Record> records = recordCollector.getRecords();

        // then
        // there are 4 records in xml files starting with "web" inside "src/test/resources"
        assertThat(records).hasSize(4);

    }

    private class XmlFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.startsWith("web") && name.endsWith("xml");
        }
    }
}