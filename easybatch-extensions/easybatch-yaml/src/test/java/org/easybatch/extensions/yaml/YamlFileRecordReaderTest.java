package org.easybatch.extensions.yaml;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

public class YamlFileRecordReaderTest {

    private YamlFileRecordReader yamlFileRecordReader;

    @Before
    public void setUp() throws Exception {
        File data = new File("src/test/resources/contacts.yml");
        yamlFileRecordReader = new YamlFileRecordReader(data);
        yamlFileRecordReader.open();
    }

    @Test
    public void testYamlRecordReading() throws Exception {
        YamlRecord yamlRecord = yamlFileRecordReader.readRecord();
        assertThat(yamlRecord.getHeader().getNumber()).isEqualTo(1L);
        assertThat(yamlRecord.getPayload()).isEqualTo("name: Foo" + LINE_SEPARATOR + "age: 28" + LINE_SEPARATOR);


        yamlRecord = yamlFileRecordReader.readRecord();
        assertThat(yamlRecord.getHeader().getNumber()).isEqualTo(2L);
        assertThat(yamlRecord.getPayload()).isEqualTo("name: Bar" + LINE_SEPARATOR + "age: 25" + LINE_SEPARATOR);


        yamlRecord = yamlFileRecordReader.readRecord();
        assertThat(yamlRecord.getHeader().getNumber()).isEqualTo(3L);
        assertThat(yamlRecord.getPayload()).isEqualTo("name: Baz" + LINE_SEPARATOR + "age: 30" + LINE_SEPARATOR);

        yamlRecord = yamlFileRecordReader.readRecord();
        assertThat(yamlRecord).isNull();
    }

    @After
    public void tearDown() throws Exception {
        yamlFileRecordReader.close();
    }
}