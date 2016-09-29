package org.easybatch.core.listener;

import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecordProcessingTimeListenerTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Record record;

    private RecordProcessingTimeListener recordProcessingTimeListener;

    @Before
    public void setUp() throws Exception {
        when(record.getHeader().getNumber()).thenReturn(1L);
        recordProcessingTimeListener = new RecordProcessingTimeListener();
    }

    @Test
    public void testBeforeRecordProcessing() throws Exception {
        assertThat(recordProcessingTimeListener.beforeRecordProcessing(record)).isSameAs(record);
    }
}