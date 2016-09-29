package org.easybatch.jms;

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.Message;
import javax.jms.QueueSender;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RandomJmsQueueRecordWriterTest {

    @Mock
    private QueueSender queue1, queue2;
    @Mock
    private Record record;
    @Mock
    private Message message;

    private RandomJmsQueueRecordWriter randomJmsQueueRecordWriter;

    @Before
    public void setUp() throws Exception {
        randomJmsQueueRecordWriter = new RandomJmsQueueRecordWriter(asList(queue1, queue2));
        when(record.getPayload()).thenReturn(message);
    }

    @Test
    public void recordsShouldBeWrittenRandomlyToOneOfTheQueues() throws Exception {
        randomJmsQueueRecordWriter.writeRecords(new Batch(record));

        verify(queue1, atMost(1)).send(message);
        verify(queue2, atMost(1)).send(message);
    }
}