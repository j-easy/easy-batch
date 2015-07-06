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

package org.easybatch.core.reader;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordProcessingException;
import org.easybatch.core.api.RecordProcessor;
import org.easybatch.core.record.MultiRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.EngineBuilder.aNewEngine;

/**
 * Test class for {@link StringMultiRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class StringMultiRecordReaderTest {

    public static final int CHUNK_SIZE = 2;

    @Rule
    public final SystemOutRule systemOut = new SystemOutRule().enableLog();

    @Test
    public void chunkProcessingIntegrationTest() throws Exception {
        String dataSource = "foo\nbar\ntoto\ntiti\nbaz";

        aNewEngine()
                .reader(new StringMultiRecordReader(CHUNK_SIZE, dataSource))
                .processor(new MultiRecordProcessor())
                .build().call();

        assertThat(systemOut.getLog()).isEqualTo("Chunk 1:\nfoo\nbar\nChunk 2:\ntoto\ntiti\nChunk 3:\nbaz\n");

    }

    private class MultiRecordProcessor implements RecordProcessor<MultiRecord, MultiRecord> {

        @Override
        public MultiRecord processRecord(MultiRecord multiRecord) throws RecordProcessingException {
            System.out.println("Chunk " + multiRecord.getHeader().getNumber() + ":");
            for (Record record : multiRecord.getPayload()) {
                System.out.println(record.getPayload());
            }
            return multiRecord;
        }
    }
}