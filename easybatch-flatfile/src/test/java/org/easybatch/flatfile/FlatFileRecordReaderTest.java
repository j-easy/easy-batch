package org.easybatch.flatfile;

import org.easybatch.core.api.Record;
import org.easybatch.core.record.StringRecord;
import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

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
        Assert.assertTrue(flatFileRecordReader.hasNextRecord());
    }

    @Test
    public void testReadNextRecord() throws Exception {
        Record record = flatFileRecordReader.readNextRecord();
        StringRecord stringRecord = (StringRecord) record;
        Assertions.assertThat(stringRecord.getHeader().getNumber()).isEqualTo(1l);
        Assert.assertEquals("id,user,message", stringRecord.getPayload());

        record = flatFileRecordReader.readNextRecord();
        stringRecord = (StringRecord) record;
        Assertions.assertThat(stringRecord.getHeader().getNumber()).isEqualTo(2l);
        Assert.assertEquals("1,foo,easy batch rocks! #EasyBatch", stringRecord.getPayload());
    }

    @Test
    public void testTotalRecords() throws Exception {
        Assert.assertEquals(new Long(3), flatFileRecordReader.getTotalRecords());
    }

    @Test
    public void testGetDataSourceName() throws Exception {
        Assert.assertEquals(dataSource.getAbsolutePath(), flatFileRecordReader.getDataSourceName());
    }

    /*
     * Empty file tests
     */

    @Test
    public void testHasNextRecordForEmptyFile() throws Exception {
        flatFileRecordReader.close();
        flatFileRecordReader = new FlatFileRecordReader(emptyDataSource);
        flatFileRecordReader.open();
        Assert.assertFalse(flatFileRecordReader.hasNextRecord());
    }

    @Test
    public void testTotalRecordsForEmptyFile() throws Exception {
        flatFileRecordReader.close();
        flatFileRecordReader = new FlatFileRecordReader(emptyDataSource);
        flatFileRecordReader.open();
        Assert.assertEquals(new Long(0), flatFileRecordReader.getTotalRecords());
    }


    @After
    public void tearDown() throws Exception {
        flatFileRecordReader.close();
    }
}
