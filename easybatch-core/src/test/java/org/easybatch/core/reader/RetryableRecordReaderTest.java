package org.easybatch.core.reader;

import org.easybatch.core.record.Record;
import org.easybatch.core.retry.RetryPolicy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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