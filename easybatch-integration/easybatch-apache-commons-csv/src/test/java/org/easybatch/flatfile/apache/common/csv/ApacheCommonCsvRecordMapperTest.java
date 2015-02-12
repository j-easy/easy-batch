package org.easybatch.flatfile.apache.common.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for {@link ApacheCommonCsvRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ApacheCommonCsvRecordMapperTest {

    private ApacheCommonCsvRecordMapper<Foo> mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ApacheCommonCsvRecordMapper<Foo>(Foo.class);
    }

    @Test
    public void testApacheCommonCsvMapping() throws Exception {
        StringReader stringReader = new StringReader("foo,bar,15,true");
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("firstName", "lastName", "age", "married");
        ApacheCommonCsvRecord record = getApacheCommonCsvRecord(stringReader, csvFormat);

        Foo foo = mapper.mapRecord(record);

        assertFoo(foo);
    }

    @Test
    public void testApacheCommonCsvDelimiter() throws Exception {
        StringReader stringReader = new StringReader("foo;bar;15;true");
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withHeader("firstName", "lastName", "age", "married");
        ApacheCommonCsvRecord record = getApacheCommonCsvRecord(stringReader, csvFormat);

        Foo foo = mapper.mapRecord(record);

        assertFoo(foo);
    }

    @Test
    public void testApacheCommonCsvQualifier() throws Exception {
        StringReader stringReader = new StringReader("'foo,s','bar,n'");
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withQuote('\'')
                .withHeader("firstName", "lastName", "age", "married");
        ApacheCommonCsvRecord record = getApacheCommonCsvRecord(stringReader, csvFormat);

        Foo foo = mapper.mapRecord(record);

        Assert.assertNotNull(foo);
        Assert.assertEquals("foo,s", foo.getFirstName());
        Assert.assertEquals("bar,n", foo.getLastName());
        Assert.assertEquals(0, foo.getAge());
        Assert.assertEquals(false, foo.isMarried());
    }

    @Test
    public void testApacheCommonCsvLineFeed() throws Exception {
        StringReader stringReader = new StringReader("'foo\n','bar\n'");
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withQuote('\'')
                .withHeader("firstName", "lastName", "age", "married");
        ApacheCommonCsvRecord record = getApacheCommonCsvRecord(stringReader, csvFormat);

        Foo foo = mapper.mapRecord(record);

        Assert.assertNotNull(foo);
        Assert.assertEquals("foo\n", foo.getFirstName());
        Assert.assertEquals("bar\n", foo.getLastName());
        Assert.assertEquals(0, foo.getAge());
        Assert.assertEquals(false, foo.isMarried());
    }

    private void assertFoo(Foo foo) {
        Assert.assertNotNull(foo);
        Assert.assertEquals("foo", foo.getFirstName());
        Assert.assertEquals("bar", foo.getLastName());
        Assert.assertEquals(15, foo.getAge());
        Assert.assertEquals(true, foo.isMarried());
    }

    private ApacheCommonCsvRecord getApacheCommonCsvRecord(StringReader stringReader, CSVFormat csvFormat) throws IOException {
        CSVParser parser = new CSVParser(stringReader, csvFormat);
        CSVRecord csvRecord = parser.iterator().next();
        return new ApacheCommonCsvRecord(1, csvRecord);
    }

}
