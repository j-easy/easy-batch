package org.easybatch.integration.quartz;

import org.easybatch.core.api.RecordProcessor;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.record.StringRecord;
import org.junit.After;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link BatchScheduler}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class BatchSchedulerTest {

    private BatchScheduler batchScheduler;

    @Mock
    private RecordProcessor<StringRecord, StringRecord> recordProcessor;

    @org.junit.Test
    public void testJobScheduling() throws Exception {

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new StringRecordReader("foo\nbar\n"))
                .processor(recordProcessor)
                .build();

        batchScheduler = new BatchScheduler(engine);
        batchScheduler.scheduleAtWithInterval(new Date(), 1);
        batchScheduler.start();

        assertThat(batchScheduler.isStarted()).isTrue();

        verify(recordProcessor, times(2)).processRecord(any(StringRecord.class));

    }

    @org.junit.Test
    public void testMultipleJobsScheduling() throws Exception {

        Engine engine1 = new EngineBuilder().build();
        Engine engine2 = new EngineBuilder().build();

        BatchScheduler batchScheduler1 = new BatchScheduler(engine1);
        batchScheduler1.scheduleAtWithInterval(new Date(), 1);
        batchScheduler1.start();

        BatchScheduler batchScheduler2 = new BatchScheduler(engine2);
        batchScheduler2.scheduleAtWithInterval(new Date(), 1);
        batchScheduler2.start();

        assertThat(batchScheduler1.isStarted()).isTrue();
        assertThat(batchScheduler2.isStarted()).isTrue();

        batchScheduler1.stop();
        batchScheduler2.stop();

    }

    @After
    public void tearDown() throws Exception {
        if (batchScheduler != null) {
            batchScheduler.stop();
        }
    }

}
