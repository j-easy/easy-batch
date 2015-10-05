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
    public void testHasNextRecord() throws Exception {
        assertThat(flatFileRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void testReadNextRecord() throws Exception {
        StringRecord record = flatFileRecordReader.readNextRecord();
        assertThat(record.getHeader().getNumber()).isEqualTo(1l);
        assertThat(record.getPayload()).isEqualTo("id,user,message");

        record = flatFileRecordReader.readNextRecord();
        assertThat(record.getHeader().getNumber()).isEqualTo(2l);
        assertThat(record.getPayload()).isEqualTo("1,foo,easy batch rocks! #EasyBatch");
    }

    @Test
    public void testTotalRecords() throws Exception {
        assertThat(flatFileRecordReader.getTotalRecords()).isEqualTo(3l);
    }

    @Test
    public void testGetDataSourceName() throws Exception {
        assertThat(flatFileRecordReader.getDataSourceName()).isEqualTo(dataSource.getAbsolutePath());
    }

    /*
     * Empty file tests
     */

    @Test
    public void testHasNextRecordForEmptyFile() throws Exception {
        flatFileRecordReader.close();
        flatFileRecordReader = new FlatFileRecordReader(emptyDataSource);
        flatFileRecordReader.open();
        assertThat(flatFileRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void testTotalRecordsForEmptyFile() throws Exception {
        flatFileRecordReader.close();
        flatFileRecordReader = new FlatFileRecordReader(emptyDataSource);
        assertThat(flatFileRecordReader.getTotalRecords()).isEqualTo(0l);
    }

    @Test
    public void testTotalRecordsForNonExistingFile() throws Exception {
        flatFileRecordReader.close();
        flatFileRecordReader = new FlatFileRecordReader(nonExistingDataSource);
        assertThat(flatFileRecordReader.getTotalRecords()).isNull();
    }


    @After
    public void tearDown() throws Exception {
        flatFileRecordReader.close();
    }

    private URI getFileUri(String fileName) throws URISyntaxException {
        return this.getClass().getResource(fileName).toURI();
    }

}
