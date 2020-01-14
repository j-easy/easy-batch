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
