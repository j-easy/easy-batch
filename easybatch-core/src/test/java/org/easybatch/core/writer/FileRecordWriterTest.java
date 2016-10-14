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

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

/**
 * Test class for {@link FileRecordWriter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class FileRecordWriterTest {

    private final Path path = Paths.get("target/test.txt");

    @Mock
    private Header header;

    private Record record1, record2;

    private FileRecordWriter writer;

    @Before
    public void setUp() throws Exception {
        Files.createFile(path);
        record1 = new StringRecord(header, "foo");
        record2 = new StringRecord(header, "bar");
        writer = new FileRecordWriter(path);
        writer.open();
    }

    @Test
    public void testFileWriting() throws Exception {
        writer.writeRecords(new Batch(record1, record2));

        assertThat(path).hasContent("foo" + LINE_SEPARATOR + "bar" + LINE_SEPARATOR);
    }

    @After
    public void tearDown() throws Exception {
        writer.close();
        Files.delete(path);
    }
}
