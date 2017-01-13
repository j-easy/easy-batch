package org.easybatch.extensions.yaml;

import org.easybatch.core.record.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

public class YamlRecordReaderTest {

    private YamlRecordReader recordReader;

    @Before
    public void setUp() throws Exception {
        recordReader = new YamlRecordReader(new FileInputStream("src/test/resources/contacts.yml"));
        recordReader.open();
    }

    @Test
    public void readRecord() throws Exception {
        Record record = recordReader.readRecord();

        assertThat(record).isNotNull().isInstanceOf(YamlRecord.class);
        assertThat(record.getPayload()).isEqualTo("name: Foo" + LINE_SEPARATOR + "age: 28" + LINE_SEPARATOR);

        record = recordReader.readRecord();

        assertThat(record).isNotNull().isInstanceOf(YamlRecord.class);
        assertThat(record.getPayload()).isEqualTo("name: Bar" + LINE_SEPARATOR + "age: 25" + LINE_SEPARATOR);

        record = recordReader.readRecord();

        assertThat(record).isNotNull().isInstanceOf(YamlRecord.class);
        assertThat(record.getPayload()).isEqualTo("name: Baz" + LINE_SEPARATOR + "age: 30" + LINE_SEPARATOR);

        record = recordReader.readRecord();

        assertThat(record).isNull();
    }


    @After
    public void tearDown() throws Exception {
        recordReader.close();
    }

}