package org.easybatch.flatfile;

import org.easybatch.core.api.Record;
import org.easybatch.core.record.StringRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link FlatFileRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class FlatFileRecordReaderTest {

    private FlatFileRecordReader flatFileRecordReader;

    private File dataSource;

    private File emptyDataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new File(this.getClass().getResource("/tweets.csv").toURI());
        emptyDataSource = new File(this.getClass().getResource("/empty-file.txt").toURI());
        flatFileRecordReader = new FlatFileRecordReader(dataSource);
        flatFileRecordReader.open();
    }

    @Test
    public void testHasNextRecord() throws Exception {
        assertThat(flatFileRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void testReadNextRecord() throws Exception {
        Record record = flatFileRecordReader.readNextRecord();
        StringRecord stringRecord = (StringRecord) record;
        assertThat(stringRecord.getHeader().getNumber()).isEqualTo(1l);
        assertThat(stringRecord.getPayload()).isEqualTo("id,user,message");

        record = flatFileRecordReader.readNextRecord();
        stringRecord = (StringRecord) record;
        assertThat(stringRecord.getHeader().getNumber()).isEqualTo(2l);
        assertThat(stringRecord.getPayload()).isEqualTo("1,foo,easy batch rocks! #EasyBatch");
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
        flatFileRecordReader.open();
        assertThat(flatFileRecordReader.getTotalRecords()).isEqualTo(0l);
    }


    @After
    public void tearDown() throws Exception {
        flatFileRecordReader.close();
    }
}
