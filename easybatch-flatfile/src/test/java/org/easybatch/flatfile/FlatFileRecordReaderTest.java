package org.easybatch.flatfile;

import org.easybatch.core.record.StringRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class FlatFileRecordReaderTest {

    private FlatFileRecordReader flatFileRecordReader;

    private File dataSource;
    private Path emptyDataSource;
    private String nonExistingDataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new File("src/test/resources/tweets.csv");
        emptyDataSource = Paths.get("src/test/resources/empty-file.txt");
        nonExistingDataSource = "src/test/resources/foo.bar";
    }

    @Test
    public void whenInputFileExistsAndIsNotEmpty_thenReadRecordShouldReturnNextRecords() throws Exception {
        flatFileRecordReader = new FlatFileRecordReader(dataSource);
        flatFileRecordReader.open();
        StringRecord record = flatFileRecordReader.readRecord();
        assertThat(record.getHeader().getNumber()).isEqualTo(1L);
        assertThat(record.getPayload()).isEqualTo("id,user,message");

        record = flatFileRecordReader.readRecord();
        assertThat(record.getHeader().getNumber()).isEqualTo(2L);
        assertThat(record.getPayload()).isEqualTo("1,foo,easy batch rocks! #EasyBatch");

        record = flatFileRecordReader.readRecord();
        assertThat(record.getHeader().getNumber()).isEqualTo(3L);
        assertThat(record.getPayload()).isEqualTo("2,bar,@foo I do confirm :-)");

        record = flatFileRecordReader.readRecord();
        assertThat(record).isNull();
    }

    @Test(expected = FileNotFoundException.class)
    public void whenInputFileDoesNotExist_thenOpeningTheReaderShouldThrowFileNotFoundException() throws Exception {
        flatFileRecordReader = new FlatFileRecordReader(nonExistingDataSource);
        flatFileRecordReader.open();
    }

    @Test
    public void whenInputFileIsEmpty_thenReadRecordShouldReturnNull() throws Exception {
        flatFileRecordReader = new FlatFileRecordReader(emptyDataSource);
        flatFileRecordReader.open();
        assertThat(flatFileRecordReader.readRecord()).isNull();
    }

    @After
    public void tearDown() throws Exception {
        flatFileRecordReader.close();
    }

}
