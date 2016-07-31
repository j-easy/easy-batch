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

package org.easybatch.core.reader;

import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

public class StringRecordReaderTest {

    public static final String EXPECTED_DATA_SOURCE_NAME = "In-Memory String";

    private StringRecordReader stringRecordReader;

    private String dataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = "foo" + LINE_SEPARATOR + "bar";
        stringRecordReader = new StringRecordReader(dataSource);
        stringRecordReader.open();
    }

    @Test
    public void whenTheDataSourceIsNotEmpty_ThenThereShouldBeANextRecordToRead() throws Exception {
        assertThat(stringRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void whenTheDataSourceIsEmpty_ThenThereShouldBeNoNextRecordToRead() throws Exception {
        setupEmptyDataSource();
        assertThat(stringRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void whenTheDataSourceIsNotEmpty_ThenTotalRecordsShouldBeEqualToTheNumberOfRecords() throws Exception {
        assertThat(stringRecordReader.getTotalRecords()).isEqualTo(2);
    }

    @Test
    public void whenTheDataSourceIsEmpty_ThenTotalRecordsShouldBeEqualToZero() throws Exception {
        setupEmptyDataSource();
        assertThat(stringRecordReader.getTotalRecords()).isEqualTo(0);
    }

    @Test
    public void whenTheDataSourceIsNotEmpty_ThenTheNextRecordShouldBeReadCorrectly() throws Exception {
        Record record = stringRecordReader.readNextRecord();
        assertThat(record).isInstanceOf(StringRecord.class);
        assertThat(record.getHeader()).isNotNull();
        assertThat(record.getHeader().getNumber()).isEqualTo(1l);
        assertThat(record.getPayload()).isEqualTo("foo");

        record = stringRecordReader.readNextRecord();
        assertThat(record).isInstanceOf(StringRecord.class);
        assertThat(record.getHeader()).isNotNull();
        assertThat(record.getHeader().getNumber()).isEqualTo(2l);
        assertThat(record.getPayload()).isEqualTo("bar");

        assertThat(stringRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void theDataSourceNameShouldBeEqualToInMemoryString() throws Exception {
        assertThat(stringRecordReader.getDataSourceName()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        stringRecordReader.close();
    }

    private void setupEmptyDataSource() throws Exception {
        dataSource = "";
        stringRecordReader = new StringRecordReader(dataSource);
        stringRecordReader.open();
    }

}
