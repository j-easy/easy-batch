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
package org.easybatch.core.reader;

import org.easybatch.core.record.Record;
import org.easybatch.core.retry.RetryPolicy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RetryableRecordReaderTest {

    @Mock
    private Record record;

    private RetryPolicy retryPolicy;
    private RecordReader recordReader;

    @Before
    public void setUp() throws Exception {
        retryPolicy = new RetryPolicy(3, 500, TimeUnit.MILLISECONDS);

    }

    @Test(expected = Exception.class)
    public void readRecord_whenMaxAttemptsExceeded() throws Exception {
        recordReader = new RetryableRecordReader(new UnreliableDataSourceReader(), retryPolicy);
        recordReader.readRecord();
    }

    @Test
    public void readRecord_whenMaxAttemptsNotExceeded() throws Exception {
        recordReader = new RetryableRecordReader(new BetterUnreliableDataSourceReader(), retryPolicy);

        Record actual = recordReader.readRecord();
        assertThat(actual).isEqualTo(record);
    }

    class UnreliableDataSourceReader implements RecordReader {
        @Override
        public void open() throws Exception {
        }

        @Override
        public Record readRecord() throws Exception {
            throw new Exception("Data source temporarily down");
        }

        @Override
        public void close() throws Exception {
        }
    }

    class BetterUnreliableDataSourceReader implements RecordReader {
        private int attempts;

        @Override
        public void open() throws Exception {
        }

        @Override
        public Record readRecord() throws Exception {
            if (++attempts > 2) {
                return record;
            }
            throw new Exception("Data source temporarily down");
        }

        @Override
        public void close() throws Exception {
        }
    }

}
