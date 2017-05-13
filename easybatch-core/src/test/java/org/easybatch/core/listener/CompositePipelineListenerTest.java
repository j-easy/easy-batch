package org.easybatch.core.listener;

import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompositePipelineListenerTest {

    @Mock
    private Record inputRecord, processedRecord, outputRecord;
    @Mock
    private Throwable exception;
    @Mock
    private PipelineListener pipelineListener1, pipelineListener2;

    private CompositePipelineListener compositePipelineListener;

    @Before
    public void setUp() throws Exception {
        when(pipelineListener1.beforeRecordProcessing(inputRecord)).thenReturn(processedRecord);
        when(pipelineListener2.beforeRecordProcessing(processedRecord)).thenReturn(outputRecord);
        compositePipelineListener = new CompositePipelineListener(asList(pipelineListener1, pipelineListener2));
    }

    @Test
    public void testBeforeRecordProcessing() throws Exception {
        compositePipelineListener.beforeRecordProcessing(inputRecord);

        InOrder inOrder = inOrder(pipelineListener1, pipelineListener2);
        inOrder.verify(pipelineListener1).beforeRecordProcessing(inputRecord);
        inOrder.verify(pipelineListener2).beforeRecordProcessing(processedRecord);
    }

    @Test
    public void testAfterRecordProcessing() throws Exception {
        compositePipelineListener.afterRecordProcessing(inputRecord, outputRecord);

        InOrder inOrder = inOrder(pipelineListener1, pipelineListener2);
        inOrder.verify(pipelineListener2).afterRecordProcessing(inputRecord, outputRecord);
        inOrder.verify(pipelineListener1).afterRecordProcessing(inputRecord, outputRecord);
    }

    @Test
    public void testOnRecordProcessingException() throws Exception {
        compositePipelineListener.onRecordProcessingException(inputRecord, exception);

        InOrder inOrder = inOrder(pipelineListener1, pipelineListener2);
        inOrder.verify(pipelineListener2).onRecordProcessingException(inputRecord, exception);
        inOrder.verify(pipelineListener1).onRecordProcessingException(inputRecord, exception);
    }

}