/*
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
package org.jeasy.batch.core.processor;

import java.util.concurrent.TimeUnit;

import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.retry.RetryPolicy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RetryableRecordProcessorTest {

    @Mock
    private Record<String> record;

    private RetryPolicy retryPolicy;
    private RecordProcessor<String, String> recordProcessor;

    @Before
    public void setUp() {
        retryPolicy = new RetryPolicy(3, 500, TimeUnit.MILLISECONDS);
    }

    @Test(expected = Exception.class)
    public void processRecord_whenMaxAttemptsExceeded() throws Exception {
        // Given
        recordProcessor = new RetryableRecordProcessor<>(new UnreliableRecordProcessor(), retryPolicy);

        // When
        recordProcessor.processRecord(record);

        // Then
        // Expecting exception
    }

    @Test
    public void processRecord_whenMaxAttemptsNotExceeded() throws Exception {
        // Given
        BetterUnreliableRecordProcessor delegate = new BetterUnreliableRecordProcessor();
        recordProcessor = new RetryableRecordProcessor<>(delegate, retryPolicy);

        // When
        recordProcessor.processRecord(record);

        // Then
        assertThat(delegate.isExecuted()).isTrue();
    }

    static class UnreliableRecordProcessor implements RecordProcessor<String, String> {

        @Override
        public Record<String> processRecord(Record<String> record) throws Exception {
            throw new Exception("Unable to process record");
        }
    }

    static class BetterUnreliableRecordProcessor implements RecordProcessor<String, String> {
        private int attempts;
        private boolean executed;

        @Override
        public Record<String> processRecord(Record<String> record) throws Exception {
            if (++attempts <= 2) {
                throw new Exception("Unable to process record");
            }
            executed = true;
            return record;
        }

        public boolean isExecuted() {
            return executed;
        }
    }

}
