/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package org.easybatch.json;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonFileRecordReaderTest {

    private File dataSource;
    private JsonFileRecordReader jsonFileRecordReader;

    @Before
    public void setUp() throws Exception {
        dataSource = new File("src/test/resources/persons.json");
        jsonFileRecordReader = new JsonFileRecordReader(dataSource);
        jsonFileRecordReader.open();
    }

    @Test
    public void testJsonRecordReading() throws Exception {
        String expectedDataSourceName = dataSource.getAbsolutePath();

        JsonRecord jsonRecord = jsonFileRecordReader.readRecord();
        assertThat(jsonRecord.getHeader().getNumber()).isEqualTo(1L);
        assertThat(jsonRecord.getHeader().getSource()).isEqualTo(expectedDataSourceName);
        assertThat(jsonRecord.getPayload()).isEqualTo("{\"id\":1,\"name\":\"foo\"}");

        jsonRecord = jsonFileRecordReader.readRecord();
        assertThat(jsonRecord.getHeader().getNumber()).isEqualTo(2L);
        assertThat(jsonRecord.getHeader().getSource()).isEqualTo(expectedDataSourceName);
        assertThat(jsonRecord.getPayload()).isEqualTo("{\"id\":2,\"name\":\"bar\"}");

        jsonRecord = jsonFileRecordReader.readRecord();
        assertThat(jsonRecord.getHeader().getNumber()).isEqualTo(3L);
        assertThat(jsonRecord.getHeader().getSource()).isEqualTo(expectedDataSourceName);
        assertThat(jsonRecord.getPayload()).isEqualTo("{\"id\":3,\"name\":\"toto\"}");

        jsonRecord = jsonFileRecordReader.readRecord();
        assertThat(jsonRecord).isNull();
    }

    @After
    public void tearDown() throws Exception {
        jsonFileRecordReader.close();
    }
}
