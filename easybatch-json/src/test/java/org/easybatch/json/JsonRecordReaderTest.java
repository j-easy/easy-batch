/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link JsonRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JsonRecordReaderTest {

    private JsonRecordReader jsonRecordReader;

    @Before
    public void setUp() throws Exception {
        jsonRecordReader = new JsonRecordReader(this.getClass().getResourceAsStream("/tweets.json"));
        jsonRecordReader.open();
    }

    @Test
    public void parsedJsonRecordPayloadShouldBeTheSameAsInInputFile() throws Exception {

        assertThat(jsonRecordReader.hasNextRecord()).isTrue();

        String expectedJson1 = "{\"id\":1,\"user\":\"foo\",\"message\":\"Hello\"}";
        JsonRecord jsonRecord1 = jsonRecordReader.readNextRecord();
        assertThat(jsonRecord1).isNotNull();
        assertThat(jsonRecord1.getHeader().getNumber()).isEqualTo(1);
        assertThat(jsonRecord1.getPayload()).isEqualTo(expectedJson1);

        assertThat(jsonRecordReader.hasNextRecord()).isTrue();

        String expectedJson2 = "{\"id\":2,\"user\":\"bar\",\"message\":\"Hi!\"}";
        JsonRecord jsonRecord2 = jsonRecordReader.readNextRecord();
        assertThat(jsonRecord2).isNotNull();
        assertThat(jsonRecord2.getHeader().getNumber()).isEqualTo(2);
        assertThat(jsonRecord2.getPayload()).isEqualTo(expectedJson2);

        assertThat(jsonRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void whenTheInputFileIsNotEmpty_ThenTheJsonRecordReaderShouldHaveNextRecord() throws Exception {
        assertThat(jsonRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void whenTheInputFileIsNotEmpty_ThenTheTotalRecordsCountShouldBeEqualToTheNumberOfRecordsInTheInputFile() throws Exception {
        Long totalRecords = jsonRecordReader.getTotalRecords();
        assertThat(totalRecords).isNotNull();
        assertThat(totalRecords).isEqualTo(2);
    }

    /*
     * Empty file tests
     */

    @Test
    public void whenTheInputFileIsEmpty_ThenTheJsonRecordReaderShouldHaveNoNextRecord() throws Exception {
        jsonRecordReader.close();
        jsonRecordReader = new JsonRecordReader(this.getClass().getResourceAsStream("/empty.json"));
        jsonRecordReader.open();
        assertThat(jsonRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void whenTheInputFileIsEmpty_ThenTheTotalRecordsCountShouldBeEqualToZero() throws Exception {
        jsonRecordReader.close();
        jsonRecordReader = new JsonRecordReader(this.getClass().getResourceAsStream("/empty.json"));
        jsonRecordReader.open();
        Long totalRecords = jsonRecordReader.getTotalRecords();
        assertThat(totalRecords).isNotNull();
        assertThat(totalRecords).isEqualTo(0);
    }

    @After
    public void tearDown() throws Exception {
        jsonRecordReader.close();
    }

}
