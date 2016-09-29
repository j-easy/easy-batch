package org.easybatch.core.listener;

import org.easybatch.core.job.JobReport;
import org.easybatch.core.record.PoisonRecord;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PoisonRecordBroadcasterTest {

    @Mock
    private JobReport jobReport;

    private BlockingQueue<Record> queue1, queue2;

    private PoisonRecordBroadcaster poisonRecordBroadcaster;

    @Before
    public void setUp() throws Exception {
        queue1 = new LinkedBlockingDeque<>();
        queue2 = new LinkedBlockingDeque<>();
        poisonRecordBroadcaster = new PoisonRecordBroadcaster(asList(queue1, queue2));
    }

    @Test
    public void testAfterJobEnd() throws Exception {
        poisonRecordBroadcaster.afterJobEnd(jobReport);

        assertThat(queue1).hasSize(1);
        assertThat(queue2).hasSize(1);
    }
}