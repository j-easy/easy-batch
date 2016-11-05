package org.easybatch.xml;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.reader.MultiFileRecordReader;
import org.easybatch.core.record.Record;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiXmlFileRecordReaderTest {

    @Ignore("Fails because XmlFileRecordWriter should be created with root element name parameter in addition to the xml file")
    @Test
    public void allResourcesShouldBeReadInOneShot() throws Exception {
        MultiFileRecordReader multiFileRecordReader = new MultiFileRecordReader(
                new File("src/test/resources"),
                new XmlFileFilter(),
                XmlFileRecordReader.class);

        RecordCollector recordCollector = new RecordCollector();
        Job job = JobBuilder.aNewJob()
                .reader(multiFileRecordReader)
                .processor(recordCollector)
                .build();

        JobExecutor jobExecutor = new JobExecutor();
        jobExecutor.execute(job);
        jobExecutor.shutdown();

        List<Record> records = recordCollector.getRecords();

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