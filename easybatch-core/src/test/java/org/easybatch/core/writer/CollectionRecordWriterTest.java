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
package org.easybatch.core.writer;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.reader.IterableRecordReader;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link CollectionRecordWriter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class CollectionRecordWriterTest {

    @Mock
    private Record record1, record2;
    @Mock
    private Object payload1, payload2;

    private List<Object> items;

    private CollectionRecordWriter writer;

    @Before
    public void setUp() throws Exception {
        when(record1.getPayload()).thenReturn(payload1);
        when(record2.getPayload()).thenReturn(payload2);
        items = new ArrayList<>();
        writer = new CollectionRecordWriter(items);
    }

    @Test
    public void testWriteRecord() throws Exception {
        writer.writeRecords(new Batch(record1, record2));
        assertThat(items).containsExactly(payload1, payload2);
    }

    @Test
    public void integrationTest() throws Exception {
        List<Object> input = Arrays.asList(payload1, payload2);
        List<Object> output = new ArrayList<>();

        Job job = aNewJob()
                .reader(new IterableRecordReader(input))
                .writer(new CollectionRecordWriter(output))
                .build();

        new JobExecutor().execute(job);

        assertThat(output).containsExactly(payload1, payload2);
    }
}
