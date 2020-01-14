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
package org.easybatch.flatfile;

import org.easybatch.core.record.StringRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class FlatFileRecordReaderTest {

    private FlatFileRecordReader flatFileRecordReader;

    private File dataSource;
    private Path emptyDataSource;
    private String nonExistingDataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new File("src/test/resources/tweets.csv");
        emptyDataSource = Paths.get("src/test/resources/empty-file.txt");
        nonExistingDataSource = "src/test/resources/foo.bar";
    }

    @Test
    public void whenInputFileExistsAndIsNotEmpty_thenReadRecordShouldReturnNextRecords() throws Exception {
        flatFileRecordReader = new FlatFileRecordReader(dataSource);
        flatFileRecordReader.open();
        StringRecord record = flatFileRecordReader.readRecord();
        assertThat(record.getHeader().getNumber()).isEqualTo(1L);
        assertThat(record.getPayload()).isEqualTo("id,user,message");

        record = flatFileRecordReader.readRecord();
        assertThat(record.getHeader().getNumber()).isEqualTo(2L);
        assertThat(record.getPayload()).isEqualTo("1,foo,easy batch rocks! #EasyBatch");

        record = flatFileRecordReader.readRecord();
        assertThat(record.getHeader().getNumber()).isEqualTo(3L);
        assertThat(record.getPayload()).isEqualTo("2,bar,@foo I do confirm :-)");

        record = flatFileRecordReader.readRecord();
        assertThat(record).isNull();
    }

    @Test(expected = FileNotFoundException.class)
    public void whenInputFileDoesNotExist_thenOpeningTheReaderShouldThrowFileNotFoundException() throws Exception {
        flatFileRecordReader = new FlatFileRecordReader(nonExistingDataSource);
        flatFileRecordReader.open();
    }

    @Test
    public void whenInputFileIsEmpty_thenReadRecordShouldReturnNull() throws Exception {
        flatFileRecordReader = new FlatFileRecordReader(emptyDataSource);
        flatFileRecordReader.open();
        assertThat(flatFileRecordReader.readRecord()).isNull();
    }

    @After
    public void tearDown() throws Exception {
        flatFileRecordReader.close();
    }

}
