package org.easybatch.core.impl;

import org.easybatch.core.api.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ProcessingPipeline}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class ProcessingPipelineTest {

    @Mock
    private Record record;

    @Mock
    private Object typedRecord, processedRecord, secondlyProcessedRecord;

    @Mock
    private Object processingResult;

    @Mock
    private Exception exception;

    @Mock
    private Report report;

    @Mock
    private ErrorRecordHandler errorRecordHandler;

    @Mock
    private EventManager eventManager;

    @Mock
    private RecordProcessor recordProcessor;

    @Mock
    private ComputationalRecordProcessor computationalRecordProcessor;

    private ProcessingPipeline processingPipeline;

    @Test
    @SuppressWarnings("unchecked")
    public void testProcess() throws Exception {
        processingPipeline = new ProcessingPipeline(asList(recordProcessor, computationalRecordProcessor),
                errorRecordHandler, report, eventManager);

        when(recordProcessor.processRecord(typedRecord)).thenReturn(processedRecord);
        when(computationalRecordProcessor.processRecord(processedRecord)).thenReturn(secondlyProcessedRecord);
        when(computationalRecordProcessor.getComputationResult()).thenReturn(processingResult);

        boolean processingError = processingPipeline.process(record, typedRecord);

        assertThat(processingError).isFalse();

        InOrder inOrder = inOrder(eventManager, recordProcessor, computationalRecordProcessor);

        inOrder.verify(eventManager).fireBeforeRecordProcessing(typedRecord);
        inOrder.verify(recordProcessor).processRecord(typedRecord);
        inOrder.verify(computationalRecordProcessor).processRecord(processedRecord);
        inOrder.verify(computationalRecordProcessor).getComputationResult();
        inOrder.verify(eventManager).fireAfterRecordProcessing(secondlyProcessedRecord, processingResult);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testProcessWithException() throws Exception {
        processingPipeline = new ProcessingPipeline(asList(recordProcessor, computationalRecordProcessor),
                errorRecordHandler, report, eventManager);

        when(recordProcessor.processRecord(typedRecord)).thenThrow(exception);

        boolean processingError = processingPipeline.process(record, typedRecord);

        assertThat(processingError).isTrue();

        InOrder inOrder = inOrder(eventManager, report, recordProcessor, errorRecordHandler, computationalRecordProcessor);

        inOrder.verify(eventManager).fireBeforeRecordProcessing(typedRecord);
        inOrder.verify(recordProcessor).processRecord(typedRecord);
        inOrder.verify(report).incrementTotalErrorRecord();
        inOrder.verify(errorRecordHandler).handle(record, exception);
        inOrder.verify(eventManager).fireOnBatchException(exception);
        inOrder.verify(eventManager).fireOnRecordProcessingException(typedRecord, exception);

        Mockito.verifyZeroInteractions(computationalRecordProcessor);
    }

    @Test
    public void testGetLastProcessor() throws Exception {
        processingPipeline = new ProcessingPipeline(asList(recordProcessor, computationalRecordProcessor),
                errorRecordHandler, report, eventManager);

        assertThat(processingPipeline.getLastProcessor()).isEqualTo(computationalRecordProcessor);
    }
}