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

package org.easybatch.core.writer;

import org.easybatch.core.reader.IterableRecordReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

/**
 * Test class for {@link StringRecordWriter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class StringRecordWriterTest {

    private static final String PAYLOAD = "Foo";

    private StringWriter stringWriter;

    private StringRecordWriter stringRecordWriter;

    @Before
    public void setUp() throws Exception {
        stringWriter = new StringWriter();
        stringRecordWriter = new StringRecordWriter(stringWriter);
    }

    @Test
    public void testWriteRecord() throws Exception {
        stringRecordWriter.writePayload(PAYLOAD);

        assertThat(stringWriter.toString()).isEqualTo(PAYLOAD + LINE_SEPARATOR);
    }

    @Test
    public void integrationTest() throws Exception {
        List<String> dataSource = Arrays.asList("foo", "bar");

        aNewJob()
                .reader(new IterableRecordReader(dataSource))
                .writer(stringRecordWriter)
                .call();

        assertThat(stringWriter.toString()).isEqualTo("foo" + LINE_SEPARATOR + "bar" + LINE_SEPARATOR);
    }
}
