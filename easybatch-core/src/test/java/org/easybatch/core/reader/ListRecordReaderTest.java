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

package org.easybatch.core.reader;

import org.easybatch.core.api.Record;
import org.easybatch.core.record.GenericRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link org.easybatch.core.reader.ListRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ListRecordReaderTest {

    public static final String EXPECTED_DATA_SOURCE_NAME = "In-Memory List";

    public static final String PAYLOAD = "foo";

    private ListRecordReader<String> listRecordReader;

    List<String> dataSource = new ArrayList<String>();

    @Before
    public void setUp() throws Exception {
        List<String> dataSource = new ArrayList<String>();
        dataSource.add(PAYLOAD);
        listRecordReader = new ListRecordReader<String>(dataSource);
    }

    @Test
    public void whenTheDataSourceIsNotEmpty_ThenThereShouldBeANextRecordToRead() throws Exception {
        assertThat(listRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void whenTheDataSourceIsEmpty_ThenThereShouldBeNoNextRecordToRead() throws Exception {
        dataSource.clear();
        listRecordReader = new ListRecordReader<String>(dataSource);
        assertThat(listRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void whenTheDataSourceIsNotEmpty_ThenTotalRecordsShouldBeEqualToTheListSize() throws Exception {
        assertThat(listRecordReader.getTotalRecords()).isEqualTo(1);
    }

    @Test
    public void whenTheDataSourceIsEmpty_ThenTotalRecordsShouldBeEqualToZero() throws Exception {
        dataSource.clear();
        listRecordReader = new ListRecordReader<String>(dataSource);
        assertThat(listRecordReader.getTotalRecords()).isEqualTo(0);
    }

    @Test
    public void whenTheDataSourceIsNotEmpty_ThenTheNextRecordShouldBeReadFromTheList() throws Exception {
        Record<String> record = listRecordReader.readNextRecord();
        assertThat(record).isInstanceOf(GenericRecord.class);
        assertThat(record.getHeader().getNumber()).isEqualTo(1l);
        assertThat(record.getPayload()).isEqualTo(PAYLOAD);
    }

    @Test
    public void theDataSourceNameShouldBeEqualToInMemoryList() throws Exception {
        assertThat(listRecordReader.getDataSourceName()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);
    }

}
