/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.json;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.Report;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.MultiRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.EngineBuilder.aNewEngine;

/**
 * Test class for {@link JsonMultiRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@SuppressWarnings("unchecked")
public class JsonMultiRecordReaderTest {

    private static final int CHUNK_SIZE = 2;
    
    private JsonMultiRecordReader jsonMultiRecordReader;

    @Before
    public void setUp() throws Exception {
        jsonMultiRecordReader = new JsonMultiRecordReader(getDataSource("/tweets.json"), CHUNK_SIZE);
    }

    @Test
    public void testJsonChunkProcessing() throws Exception {
        
        Report report = aNewEngine()
                .reader(jsonMultiRecordReader)
                .processor(new RecordCollector<MultiRecord>())
                .build().call();

        List<MultiRecord> multiRecords = (List<MultiRecord>) report.getJobResult();

        assertThat(multiRecords).isNotNull().hasSize(2);

        MultiRecord chunk1 = multiRecords.get(0);
        List<Record> records = chunk1.getPayload();
        assertThat(records.size()).isEqualTo(2);
        assertThat(records.get(0).getHeader().getNumber()).isEqualTo(1L);
        assertThat(records.get(0).getPayload().toString()).isEqualTo("{\"id\":1,\"user\":\"foo\",\"message\":\"Hello\"}");
        assertThat(records.get(1).getHeader().getNumber()).isEqualTo(2L);
        assertThat(records.get(1).getPayload().toString()).isEqualTo("{\"id\":2,\"user\":\"bar\",\"message\":\"Hi!\"}");

        MultiRecord chunk2 = multiRecords.get(1);
        records = chunk2.getPayload();
        assertThat(records.size()).isEqualTo(1);
        assertThat(records.get(0).getHeader().getNumber()).isEqualTo(3L);
        assertThat(records.get(0).getPayload().toString()).isEqualTo("{\"id\":3,\"user\":\"toto\",\"message\":\"yep ;-)\"}");
    }

    @Test
    public void testEmptyDataSource() throws Exception {
        jsonMultiRecordReader = new JsonMultiRecordReader(getDataSource("/empty.json"), CHUNK_SIZE);
        Report report = aNewEngine()
                .reader(jsonMultiRecordReader)
                .processor(new RecordCollector<MultiRecord>())
                .build().call();

        List<MultiRecord> multiRecords = (List<MultiRecord>) report.getJobResult();

        assertThat(multiRecords).isNotNull().isEmpty();
    }

    private InputStream getDataSource(String name) {
        return this.getClass().getResourceAsStream(name);
    }
}
