package org.easybatch.integration.opencsv.test;

import org.easybatch.core.util.StringRecord;
import org.easybatch.integration.opencsv.OpenCsvRecordMapper;
import org.junit.Assert;

/**
 * Test class for {@link OpenCsvRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class OpenCsvRecordMapperTest {

    private OpenCsvRecordMapper<Foo> openCsvRecordMapper;

    @org.junit.Before
    public void setUp() throws Exception {
        openCsvRecordMapper = new OpenCsvRecordMapper<Foo>(Foo.class, new String[]{"firstName", "lastName", "age", "married"});
    }

    @org.junit.Test
    public void testOpenCsvMapping() throws Exception {
        StringRecord fooRecord = new StringRecord(1, "foo,bar,15,true");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        Assert.assertNotNull(foo);
        Assert.assertEquals("foo", foo.getFirstName());
        Assert.assertEquals("bar", foo.getLastName());
        Assert.assertEquals(15, foo.getAge());
        Assert.assertEquals(true, foo.isMarried());
    }

    @org.junit.Test
    public void testOpenCsvDelimiter() throws Exception {
        openCsvRecordMapper.setDelimiter(';');
        StringRecord fooRecord = new StringRecord(1, "foo;bar");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        Assert.assertNotNull(foo);
        Assert.assertEquals("foo", foo.getFirstName());
        Assert.assertEquals("bar", foo.getLastName());
    }

    @org.junit.Test
    public void testOpenCsvQualifier() throws Exception {
        openCsvRecordMapper.setQualifier('\'');
        StringRecord fooRecord = new StringRecord(1, "'foo,s','bar'");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        Assert.assertNotNull(foo);
        Assert.assertEquals("foo,s", foo.getFirstName());
        Assert.assertEquals("bar", foo.getLastName());
    }

    @org.junit.Test
    public void testOpenCsvCarriageReturn() throws Exception {
        openCsvRecordMapper.setQualifier('\'');
        StringRecord fooRecord = new StringRecord(1, "'foo\n','bar\n'");
        Foo foo = openCsvRecordMapper.mapRecord(fooRecord);
        Assert.assertNotNull(foo);
        Assert.assertEquals("foo", foo.getFirstName());
        Assert.assertEquals("bar", foo.getLastName());
    }

    @org.junit.After
    public void tearDown() throws Exception {
        openCsvRecordMapper = null;
        System.gc();
    }

}
