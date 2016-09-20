package org.easybatch.core.writer;

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
public class RetryableRecordWriterTest {

    @Mock
    private Record record;

    private RetryPolicy retryPolicy;
    private RecordWriter recordWriter;

    @Before
    public void setUp() throws Exception {
        retryPolicy = new RetryPolicy(3, 500, TimeUnit.MILLISECONDS);

    }

    @Test(expected = Exception.class)
    public void writeRecords_whenMaxAttemptsExceeded() throws Exception {
        // Given
        recordWriter = new RetryableRecordWriter(new UnreliableDataSinkWriter(), retryPolicy);

        // When
        recordWriter.writeRecord(record);

        // Then
        // Expecting exception
    }

    @Test
    public void writeRecords_whenMaxAttemptsNotExceeded() throws Exception {
        // Given
        BetterUnreliableDataSinkWriter delegate = new BetterUnreliableDataSinkWriter();
        recordWriter = new RetryableRecordWriter(delegate, retryPolicy);

        // When
        recordWriter.writeRecord(record);

        // Then
        assertThat(delegate.isExecuted()).isTrue();
    }

    class UnreliableDataSinkWriter implements RecordWriter {
        @Override
        public void open() throws Exception {
        }

        @Override
        public void writeRecord(Record record) throws Exception {
            throw new Exception("Data source temporarily down");
        }

        @Override
        public void close() throws Exception {
        }
    }

    class BetterUnreliableDataSinkWriter implements RecordWriter {
        private int attempts;
        private boolean executed;

        @Override
        public void open() throws Exception {
        }

        @Override
        public void writeRecord(Record record) throws Exception {
            if (++attempts <= 2) {
                throw new Exception("Data sink temporarily down");
            }
            executed = true;
        }

        @Override
        public void close() throws Exception {
        }

        public boolean isExecuted() {
            return executed;
        }
    }

}