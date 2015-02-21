package org.easybatch.flatfile.apache.common.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.easybatch.core.api.Record;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(record).isInstanceOf(ApacheCommonCsvRecord.class);

        ApacheCommonCsvRecord apacheCommonCsvRecord = (ApacheCommonCsvRecord) record;
        assertThat(apacheCommonCsvRecord.getHeader().getNumber()).isEqualTo(1);
        assertThat(apacheCommonCsvRecord.getPayload().get("firstName")).isEqualTo("foo");
        assertThat(apacheCommonCsvRecord.getPayload().get("lastName")).isEqualTo("bar");
        assertThat(apacheCommonCsvRecord.getPayload().get("age")).isEqualTo("15");
        assertThat(apacheCommonCsvRecord.getPayload().get("married")).isEqualTo("true");
    }

    @After
    public void tearDown() throws Exception {
        recordReader.close();
    }

}
