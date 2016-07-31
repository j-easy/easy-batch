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

package org.easybatch.core.reader;

import org.easybatch.core.processor.RecordProcessingException;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

public class StringBatchReaderTest {

    public static final int BATCH_SIZE = 2;

    @Rule
    public final SystemOutRule systemOut = new SystemOutRule().enableLog();

    @Test
    public void batchProcessingIntegrationTest() throws Exception {
        String dataSource = "foo" + LINE_SEPARATOR +
                "bar" + LINE_SEPARATOR +
                "toto" + LINE_SEPARATOR +
                "titi" + LINE_SEPARATOR +
                "baz";

        aNewJob()
                .reader(new StringBatchReader(dataSource, BATCH_SIZE))
                .processor(new BatchProcessor())
                .call();

        assertThat(systemOut.getLog()).isEqualTo("Batch 1:" + LINE_SEPARATOR +
                        "foo" + LINE_SEPARATOR +
                        "bar" + LINE_SEPARATOR +
                        "Batch 2:" + LINE_SEPARATOR +
                        "toto" + LINE_SEPARATOR +
                        "titi" + LINE_SEPARATOR +
                        "Batch 3:" + LINE_SEPARATOR +
                        "baz" + LINE_SEPARATOR
        );

    }

    private class BatchProcessor implements RecordProcessor<Batch, Batch> {

        @Override
        public Batch processRecord(Batch batch) throws RecordProcessingException {
            System.out.println("Batch " + batch.getHeader().getNumber() + ":");
            for (Record record : batch.getPayload()) {
                System.out.println(record.getPayload());
            }
            return batch;
        }
    }
}
