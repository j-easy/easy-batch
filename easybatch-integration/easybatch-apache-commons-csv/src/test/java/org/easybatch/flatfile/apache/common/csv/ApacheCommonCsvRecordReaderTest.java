package org.easybatch.flatfile.apache.common.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.easybatch.core.api.Record;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

/**
 * Test class for {@link ApacheCommonCsvRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ApacheCommonCsvRecordReaderTest {

    private ApacheCommonCsvRecordReader recordReader;

    @Before
    public void setUp() throws Exception {
        StringReader stringReader = new StringReader("foo,bar,15,true");
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("firstName", "lastName", "age", "married");
        CSVParser parser = new CSVParser(stringReader, csvFormat);
        recordReader = new ApacheCommonCsvRecordReader(parser);
        recordReader.open();
    }

    @Test
    public void testHasNextRecord() throws Exception {
         Assert.assertTrue(recordReader.hasNextRecord());
    }

    @Test
    public void testReadNextRecord() throws Exception {
        Record record = recordReader.readNextRecord();
        Assert.assertTrue(record instanceof ApacheCommonCsvRecord);

        ApacheCommonCsvRecord apacheCommonCsvRecord = (ApacheCommonCsvRecord) record;
        Assert.assertEquals(1, apacheCommonCsvRecord.getNumber());
        Assert.assertEquals("foo", apacheCommonCsvRecord.getPayload().get("firstName"));
        Assert.assertEquals("bar", apacheCommonCsvRecord.getPayload().get("lastName"));
        Assert.assertEquals("15", apacheCommonCsvRecord.getPayload().get("age"));
        Assert.assertEquals("true", apacheCommonCsvRecord.getPayload().get("married"));
    }

    @After
    public void tearDown() throws Exception {
        recordReader.close();
    }

}
