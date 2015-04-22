package org.easybatch.core.reader;

import org.easybatch.core.api.Record;
import org.easybatch.core.record.PoisonRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link QueueRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class QueueRecordReaderTest {

    private QueueRecordReader queueRecordReader;

    private BlockingQueue<Record> queue;

    @Mock
    private Record record;

    @Mock
    private PoisonRecord poisonRecord;

    @Before
    public void setUp() throws Exception {
        queue = new ArrayBlockingQueue<Record>(10);
        queue.put(record);
        queue.put(poisonRecord);

        queueRecordReader = new QueueRecordReader(queue);
        queueRecordReader.open();
    }

    @Test
    public void testTotalRecordsIsNull() throws Exception {
        assertThat(queueRecordReader.getTotalRecords()).isNull();
    }

    @Test
    public void testHasNextRecord() throws Exception {
        assertThat(queueRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void testReadNextRecord() throws Exception {
        assertThat(queueRecordReader.readNextRecord()).isEqualTo(record);
        assertThat(queueRecordReader.hasNextRecord()).isTrue();
        assertThat(queueRecordReader.readNextRecord()).isEqualTo(poisonRecord);
        assertThat(queueRecordReader.hasNextRecord()).isFalse();
        assertThat(queue).isEmpty();
    }

    @After
    public void tearDown() throws Exception {
        queueRecordReader.close();
    }
}