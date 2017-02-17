package org.easybatch.xml;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlFileRecordReaderTest {

    private XmlFileRecordReader xmlFileRecordReader;

    @Before
    public void setUp() throws Exception {
        File data = new File("src/test/resources/data.xml");
        xmlFileRecordReader = new XmlFileRecordReader(data, "data");
        xmlFileRecordReader.open();
    }

    @Test
    public void testXmlRecordReading() throws Exception {
        XmlRecord xmlRecord = xmlFileRecordReader.readRecord();
        assertThat(xmlRecord.getHeader().getNumber()).isEqualTo(1L);
        assertThat(xmlRecord.getPayload()).isEqualTo("<data>1</data>");

        xmlRecord = xmlFileRecordReader.readRecord();
        assertThat(xmlRecord.getHeader().getNumber()).isEqualTo(2L);
        assertThat(xmlRecord.getPayload()).isEqualTo("<data>2</data>");

        xmlRecord = xmlFileRecordReader.readRecord();
        assertThat(xmlRecord.getHeader().getNumber()).isEqualTo(3L);
        assertThat(xmlRecord.getPayload()).isEqualTo("<data>3</data>");

        xmlRecord = xmlFileRecordReader.readRecord();
        assertThat(xmlRecord.getHeader().getNumber()).isEqualTo(4L);
        assertThat(xmlRecord.getPayload()).isEqualTo("<data>4</data>");

        xmlRecord = xmlFileRecordReader.readRecord();
        assertThat(xmlRecord.getHeader().getNumber()).isEqualTo(5L);
        assertThat(xmlRecord.getPayload()).isEqualTo("<data>5</data>");

        xmlRecord = xmlFileRecordReader.readRecord();
        assertThat(xmlRecord).isNull();
    }

    @After
    public void tearDown() throws Exception {
        xmlFileRecordReader.close();
    }
}