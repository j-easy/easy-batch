/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.extensions.apache.common.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easybatch.core.record.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

public class ApacheCommonCsvRecordReaderTest {

    private ApacheCommonCsvRecordReader recordReader;

    @Before
    public void setUp() throws Exception {
        StringReader stringReader = new StringReader("foo,bar,15,true");
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("firstName", "lastName", "age", "married");
        CSVParser parser = new CSVParser(stringReader, csvFormat);
        recordReader = new ApacheCommonCsvRecordReader(parser);
        recordReader.open();
    }

    @Test
    public void testHasNextRecord() throws Exception {
      assertThat(recordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void testReadNextRecord() throws Exception {
        recordReader.hasNextRecord();
        Record record = recordReader.readNextRecord();
        assertThat(record).isInstanceOf(ApacheCommonCsvRecord.class);

        ApacheCommonCsvRecord apacheCommonCsvRecord = (ApacheCommonCsvRecord) record;
        assertThat(apacheCommonCsvRecord.getHeader()).isNotNull();
        assertThat(apacheCommonCsvRecord.getHeader().getNumber()).isEqualTo(1);

        CSVRecord csvRecord = apacheCommonCsvRecord.getPayload();
        assertThat(csvRecord.get("firstName")).isEqualTo("foo");
        assertThat(csvRecord.get("lastName")).isEqualTo("bar");
        assertThat(csvRecord.get("age")).isEqualTo("15");
        assertThat(csvRecord.get("married")).isEqualTo("true");
    }

    @After
    public void tearDown() throws Exception {
        recordReader.close();
    }

}
