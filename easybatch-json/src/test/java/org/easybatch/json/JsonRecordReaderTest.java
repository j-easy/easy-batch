/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.json;

import org.easybatch.core.reader.RecordReadingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonRecordReaderTest {

    private JsonRecordReader jsonRecordReader;

    @Before
    public void setUp() throws Exception {
        jsonRecordReader = new JsonRecordReader(getDataSource("/tweets.json"));
        jsonRecordReader.open();
    }

    @Test
    public void whenTheDataSourceIsNotEmpty_ThenTheJsonRecordReaderShouldHaveNextRecord() throws Exception {
        assertThat(jsonRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void parsedJsonRecordPayloadShouldBeTheSameAsInTheDataSource() throws Exception {

        assertThat(jsonRecordReader.hasNextRecord()).isTrue();

        String expectedJson = "{\"id\":1,\"user\":\"foo\",\"message\":\"Hello\"}";
        JsonRecord jsonRecord = jsonRecordReader.readNextRecord();
        assertThat(jsonRecord).isNotNull();
        assertThat(jsonRecord.getHeader().getNumber()).isEqualTo(1);
        assertThat(jsonRecord.getPayload()).isEqualTo(expectedJson);

        assertThat(jsonRecordReader.hasNextRecord()).isTrue();

        expectedJson = "{\"id\":2,\"user\":\"bar\",\"message\":\"Hi!\"}";
        jsonRecord = jsonRecordReader.readNextRecord();
        assertThat(jsonRecord).isNotNull();
        assertThat(jsonRecord.getHeader().getNumber()).isEqualTo(2);
        assertThat(jsonRecord.getPayload()).isEqualTo(expectedJson);

        assertThat(jsonRecordReader.hasNextRecord()).isTrue();

        expectedJson = "{\"id\":3,\"user\":\"toto\",\"message\":\"yep ;-)\"}";
        jsonRecord = jsonRecordReader.readNextRecord();
        assertThat(jsonRecord).isNotNull();
        assertThat(jsonRecord.getHeader().getNumber()).isEqualTo(3);
        assertThat(jsonRecord.getPayload()).isEqualTo(expectedJson);

        assertThat(jsonRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void testEmbeddedObjectParsing() throws Exception {
        String dataSource = "[{\"name\":\"foo\",\"address\":{\"zipcode\":1000,\"city\":\"brussels\"}}]";
        jsonRecordReader.close();
        jsonRecordReader = new JsonRecordReader(new ByteArrayInputStream(dataSource.getBytes()));
        jsonRecordReader.open();
        assertThat(jsonRecordReader.hasNextRecord()).isTrue();
        JsonRecord record = jsonRecordReader.readNextRecord();
        assertThat(record.getPayload()).isEqualTo("{\"name\":\"foo\",\"address\":{\"zipcode\":1000,\"city\":\"brussels\"}}");
    }

    @Test
    public void testEmbeddedArrayParsing() throws Exception {
        String dataSource = "[{\"friends\":[\"foo\",\"bar\"]}]";
        jsonRecordReader.close();
        jsonRecordReader = new JsonRecordReader(new ByteArrayInputStream(dataSource.getBytes()));
        jsonRecordReader.open();
        assertThat(jsonRecordReader.hasNextRecord()).isTrue();
        JsonRecord record = jsonRecordReader.readNextRecord();
        assertThat(record.getPayload()).isEqualTo("{\"friends\":[\"foo\",\"bar\"]}");
    }

    /*
     * Empty file tests
     */

    @Test
    public void whenTheDataSourceIsEmpty_ThenTheJsonRecordReaderShouldHaveNoNextRecord() throws Exception {
        jsonRecordReader.close();
        jsonRecordReader = new JsonRecordReader(getDataSource("/empty.json"));
        jsonRecordReader.open();
        assertThat(jsonRecordReader.hasNextRecord()).isFalse();
    }

    @Test(expected = RecordReadingException.class)
    public void whenJsonStreamIsIllformed_thenTheJsonRecordReaderShouldThrowAnException() throws Exception {
        String dataSource = "[{\"name\":\"foo\",}]";// illegal trailing comma
        jsonRecordReader.close();
        jsonRecordReader = new JsonRecordReader(new ByteArrayInputStream(dataSource.getBytes()));
        jsonRecordReader.open();
        assertThat(jsonRecordReader.hasNextRecord()).isTrue();
        jsonRecordReader.readNextRecord();

    }

    @After
    public void tearDown() throws Exception {
        jsonRecordReader.close();
    }

    private InputStream getDataSource(String fileName) {
        return this.getClass().getResourceAsStream(fileName);
    }

}
