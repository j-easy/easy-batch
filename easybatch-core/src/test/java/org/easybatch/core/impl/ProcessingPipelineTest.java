/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.core.impl;

import org.easybatch.core.api.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProcessingPipelineTest {

    @Mock
    private Record record;

    @Mock
    private Object preProcessedRecord, processedRecord, secondlyProcessedRecord;

    @Mock
    private Object processingResult;

    @Mock
    private RecordProcessingException exception;

    @Mock
    private Report report;

    @Mock
    private EventManager eventManager;

    @Mock
    private RecordProcessor recordProcessor;

    @Mock
    private ComputationalRecordProcessor computationalRecordProcessor;

    private Pipeline processingPipeline;

    @Before
    public void setUp() throws Exception {
        when(eventManager.fireBeforeRecordProcessing(record)).thenReturn(preProcessedRecord);
        processingPipeline = new Pipeline(asList(recordProcessor, computationalRecordProcessor), report, eventManager);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testProcessWithoutException() throws Exception {
        when(recordProcessor.processRecord(preProcessedRecord)).thenReturn(processedRecord);
        when(computationalRecordProcessor.processRecord(processedRecord)).thenReturn(secondlyProcessedRecord);
        when(computationalRecordProcessor.getComputationResult()).thenReturn(processingResult);

        boolean processingError = processingPipeline.process(record);

        assertThat(processingError).isFalse();

        InOrder inOrder = inOrder(eventManager, recordProcessor, computationalRecordProcessor);

        inOrder.verify(eventManager).fireBeforeRecordProcessing(record);
        inOrder.verify(recordProcessor).processRecord(preProcessedRecord);
        inOrder.verify(computationalRecordProcessor).processRecord(processedRecord);
        inOrder.verify(computationalRecordProcessor).getComputationResult();
        inOrder.verify(eventManager).fireAfterRecordProcessing(secondlyProcessedRecord, processingResult);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testProcessWithException() throws Exception {
        when(recordProcessor.processRecord(preProcessedRecord)).thenThrow(exception);

        boolean processingError = processingPipeline.process(record);

        assertThat(processingError).isTrue();

        InOrder inOrder = inOrder(eventManager, report, recordProcessor, computationalRecordProcessor);

        inOrder.verify(eventManager).fireBeforeRecordProcessing(record);
        inOrder.verify(recordProcessor).processRecord(preProcessedRecord);
        inOrder.verify(report).incrementTotalErrorRecord();
        inOrder.verify(eventManager).fireOnRecordProcessingException(record, exception);

        verifyZeroInteractions(computationalRecordProcessor);
    }

    @Test
    public void testGetLastProcessor() throws Exception {
        assertThat(processingPipeline.getLastProcessor()).isEqualTo(computationalRecordProcessor);
    }
}
