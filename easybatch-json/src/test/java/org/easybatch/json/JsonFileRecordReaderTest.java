package org.easybatch.json;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonFileRecordReaderTest {

    private JsonFileRecordReader jsonFileRecordReader;

    @Before
    public void setUp() throws Exception {
        File tweets = new File("src/test/resources/persons.json");
        jsonFileRecordReader = new JsonFileRecordReader(tweets);
        jsonFileRecordReader.open();
    }

    @Test
    public void testJsonRecordReading() throws Exception {
        JsonRecord jsonRecord = jsonFileRecordReader.readRecord();
        assertThat(jsonRecord.getHeader().getNumber()).isEqualTo(1L);
        assertThat(jsonRecord.getPayload()).isEqualTo("{\"id\":1,\"name\":\"foo\"}");

        jsonRecord = jsonFileRecordReader.readRecord();
        assertThat(jsonRecord.getHeader().getNumber()).isEqualTo(2L);
        assertThat(jsonRecord.getPayload()).isEqualTo("{\"id\":2,\"name\":\"bar\"}");

        jsonRecord = jsonFileRecordReader.readRecord();
        assertThat(jsonRecord.getHeader().getNumber()).isEqualTo(3L);
        assertThat(jsonRecord.getPayload()).isEqualTo("{\"id\":3,\"name\":\"toto\"}");

        jsonRecord = jsonFileRecordReader.readRecord();
        assertThat(jsonRecord).isNull();
    }

    @After
    public void tearDown() throws Exception {
        jsonFileRecordReader.close();
    }
}