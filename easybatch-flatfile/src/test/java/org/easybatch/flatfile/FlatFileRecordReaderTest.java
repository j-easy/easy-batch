package org.easybatch.flatfile;

import org.easybatch.core.record.StringRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class FlatFileRecordReaderTest {

    private FlatFileRecordReader flatFileRecordReader;

    private File dataSource, emptyDataSource, nonExistingDataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new File(getFileUri("/tweets.csv"));
        emptyDataSource = new File(getFileUri("/empty-file.txt"));
        nonExistingDataSource = new File("./foo.bar");
        flatFileRecordReader = new FlatFileRecordReader(dataSource);
        flatFileRecordReader.open();
    }

    @Test
    public void testReadNextRecord() throws Exception {
        StringRecord record = flatFileRecordReader.readRecord();
        assertThat(record.getHeader().getNumber()).isEqualTo(1L);
        assertThat(record.getPayload()).isEqualTo("id,user,message");

        record = flatFileRecordReader.readRecord();
        assertThat(record.getHeader().getNumber()).isEqualTo(2L);
        assertThat(record.getPayload()).isEqualTo("1,foo,easy batch rocks! #EasyBatch");
    }

    /*
     * Empty file tests
     */

    @Test
    public void testReadRecordForEmptyFile() throws Exception {
        flatFileRecordReader.close();
        flatFileRecordReader = new FlatFileRecordReader(emptyDataSource);
        flatFileRecordReader.open();
        assertThat(flatFileRecordReader.readRecord()).isNull();
    }

    @After
    public void tearDown() throws Exception {
        flatFileRecordReader.close();
    }

    private URI getFileUri(String fileName) throws URISyntaxException {
        return this.getClass().getResource(fileName).toURI();
    }

}
