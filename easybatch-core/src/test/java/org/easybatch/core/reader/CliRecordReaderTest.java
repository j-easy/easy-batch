package org.easybatch.core.reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link CliRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class CliRecordReaderTest {

    private static final String EXPECTED_DATA_SOURCE_NAME = "Standard Input";

    private CliRecordReader cliRecordReader;

    @Before
    public void setUp() throws Exception {
        cliRecordReader = new CliRecordReader();
        cliRecordReader.open();
    }

    @Test
    public void testGetTotalRecords() throws Exception {
        assertThat(cliRecordReader.getTotalRecords()).isNull();
    }

    @Test
    public void testGetDataSourceName() throws Exception {
        assertThat(cliRecordReader.getDataSourceName()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        cliRecordReader.close();
    }

}