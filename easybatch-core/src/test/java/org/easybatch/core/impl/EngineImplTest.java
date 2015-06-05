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
import org.easybatch.core.api.event.batch.BatchProcessEventListener;
import org.easybatch.core.api.event.step.*;
import org.easybatch.core.exception.RecordMappingException;
import org.easybatch.core.exception.RecordProcessingException;
import org.easybatch.core.exception.RecordReaderOpeningException;
import org.easybatch.core.exception.RecordReadingException;
import org.easybatch.core.record.StringRecord;
import org.easybatch.core.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link EngineImpl}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class EngineImplTest {

    private Engine engine;

    @Mock
    private Header header1, header2;
    @Mock
    private StringRecord record1, record2;
    @Mock
    private RecordReader reader;
    @Mock
    private RecordFilter firstFilter, secondFilter;
    @Mock
    private RecordMapper mapper;
    @Mock
    private RecordValidator firstValidator, secondValidator;
    @Mock
    private RecordProcessor firstProcessor, secondProcessor;
    @Mock
    private ComputationalRecordProcessor computationalRecordProcessor;
    @Mock
    private BatchProcessEventListener batchProcessEventListener;
    @Mock
    private RecordReaderEventListener recordReaderEventListener;
    @Mock
    private RecordFilterEventListener recordFilterEventListener;
    @Mock
    private RecordMapperEventListener recordMapperEventListener;
    @Mock
    private RecordValidatorEventListener recordValidatorEventListener;
    @Mock
    private RecordProcessorEventListener recordProcessorEventListener;
    @Mock
    private FilteredRecordHandler filteredRecordHandler;
    @Mock
    private IgnoredRecordHandler ignoredRecordHandler;
    @Mock
    private RejectedRecordHandler rejectedRecordHandler;
    @Mock
    private ErrorRecordHandler errorRecordHandler;
    @Mock
    private RecordMappingException recordMappingException;
    @Mock
    private RecordReadingException recordReadingException;
    @Mock
    private RecordReaderOpeningException recordReaderOpeningException;
    @Mock
    private RecordProcessingException recordProcessingException;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        when(header1.getNumber()).thenReturn(1l);
        when(header2.getNumber()).thenReturn(2l);
        when(record1.getHeader()).thenReturn(header1);
        when(record2.getHeader()).thenReturn(header2);
        when(record1.getPayload()).thenReturn("test1");
        when(record2.getPayload()).thenReturn("test2");
        when(reader.hasNextRecord()).thenReturn(true, true, false);
        when(reader.readNextRecord()).thenReturn(record1, record2);
        when(firstFilter.filterRecord(record1)).thenReturn(false);
        when(secondFilter.filterRecord(record1)).thenReturn(false);
        when(firstFilter.filterRecord(record2)).thenReturn(false);
        when(secondFilter.filterRecord(record2)).thenReturn(false);
        when(mapper.mapRecord(record1)).thenReturn(record1);
        when(mapper.mapRecord(record2)).thenReturn(record2);
        when(firstValidator.validateRecord(record1)).thenReturn(Collections.emptySet());
        when(secondValidator.validateRecord(record1)).thenReturn(Collections.emptySet());
        when(firstValidator.validateRecord(record2)).thenReturn(Collections.emptySet());
        when(secondValidator.validateRecord(record2)).thenReturn(Collections.emptySet());
        when(firstProcessor.processRecord(record1)).thenReturn(record1);
        when(firstProcessor.processRecord(record2)).thenReturn(record2);
        when(secondProcessor.processRecord(record1)).thenReturn(record1);
        when(secondProcessor.processRecord(record2)).thenReturn(record2);
        engine = new EngineBuilder()
                .reader(reader)
                .filter(firstFilter)
                .filter(secondFilter)
                .mapper(mapper)
                .validator(firstValidator)
                .validator(secondValidator)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .build();
    }

    /*
     * Core engine implementation tests
     */

    @Test
    @SuppressWarnings("unchecked")
    public void allComponentsShouldBeInvokedForEachRecordInOrder() throws Exception {

        engine.call();

        InOrder inOrder = Mockito.inOrder(record1, record2, firstFilter, secondFilter, mapper, firstValidator, secondValidator, firstProcessor, secondProcessor);

        inOrder.verify(firstFilter).filterRecord(record1);
        inOrder.verify(secondFilter).filterRecord(record1);
        inOrder.verify(mapper).mapRecord(record1);
        inOrder.verify(firstValidator).validateRecord(record1);
        inOrder.verify(secondValidator).validateRecord(record1);
        inOrder.verify(firstProcessor).processRecord(record1);
        inOrder.verify(secondProcessor).processRecord(record1);

        inOrder.verify(firstFilter).filterRecord(record2);
        inOrder.verify(secondFilter).filterRecord(record2);
        inOrder.verify(mapper).mapRecord(record2);
        inOrder.verify(firstValidator).validateRecord(record2);
        inOrder.verify(secondValidator).validateRecord(record2);
        inOrder.verify(firstProcessor).processRecord(record2);
        inOrder.verify(secondProcessor).processRecord(record2);

    }

    @Test
    public void recordReaderShouldBeClosedAtTheEndOfExecution() throws Exception {
        when(reader.hasNextRecord()).thenReturn(true, false);
        when(reader.readNextRecord()).thenReturn(record1);
        engine = new EngineBuilder().reader(reader).build();

        engine.call();

        verify(reader).close();
    }

    @Test
    public void whenNotAbleToOpenReader_ThenTheEngineShouldAbortExecution() throws Exception {
        doThrow(recordReaderOpeningException).when(reader).open();
        engine = new EngineBuilder()
                .reader(reader)
                .build();
        Report report = engine.call();
        assertThat(report).isNotNull();
        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(0);
        assertThat(report.getTotalRecords()).isNull();
        assertThat(report.getStatus()).isEqualTo(Status.ABORTED);
    }

    @Test
    public void whenNotAbleToReadNextRecord_ThenTheEngineShouldAbortExecution() throws Exception {
        when(reader.hasNextRecord()).thenReturn(true);
        when(reader.readNextRecord()).thenThrow(recordReadingException);
        when(reader.getTotalRecords()).thenReturn(null);

        engine = new EngineBuilder()
                .reader(reader)
                .build();
        Report report = engine.call();

        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(0);
        assertThat(report.getTotalRecords()).isNull();
        assertThat(report.getStatus()).isEqualTo(Status.ABORTED);
    }

    @Test
    public void batchResultShouldBeReturnedFromTheLastProcessorInThePipeline() throws Exception {
        when(computationalRecordProcessor.getComputationResult()).thenReturn(5);

        engine = new EngineBuilder()
                .reader(reader)
                .processor(computationalRecordProcessor)
                .build();
        Report report = engine.call();

        assertThat(report.getBatchResult()).isEqualTo(computationalRecordProcessor.getComputationResult());
    }

    @Test
    public void reportShouldBeCorrect() throws Exception {
        Report report = engine.call();
        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(2);
        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getStatus()).isEqualTo(Status.FINISHED);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenStrictModeIsEnabled_ThenTheEngineShouldAbortOnFirstProcessingExceptionIfAny() throws Exception {
        when(firstProcessor.processRecord(record1)).thenReturn(record1);
        when(secondProcessor.processRecord(record1)).thenThrow(recordProcessingException);
        engine = new EngineBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .strictMode(true)
                .build();
        Report report = engine.call();
        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getErrorRecordsCount()).isEqualTo(1);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(0);
        assertThat(report.getTotalRecords()).isEqualTo(1);
        assertThat(report.getStatus()).isEqualTo(Status.ABORTED);
        verify(firstProcessor).processRecord(record1);
        verify(secondProcessor).processRecord(record1);
        verifyNoMoreInteractions(firstProcessor);
        verifyNoMoreInteractions(secondProcessor);
    }

    @Test
    public void whenStrictModeIsEnabled_ThenTheEngineShouldAbortOnFirstMappingExceptionIfAny() throws Exception {
        when(mapper.mapRecord(record1)).thenThrow(recordMappingException);
        engine = new EngineBuilder()
                .reader(reader)
                .mapper(mapper)
                .strictMode(true)
                .build();
        Report report = engine.call();
        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(1);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(0);
        assertThat(report.getTotalRecords()).isEqualTo(1);
        assertThat(report.getStatus()).isEqualTo(Status.ABORTED);
        verify(mapper).mapRecord(record1);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    public void whenEngineNameIsNotSpecified_thenTheJmxMBeanShouldBeRegisteredWithDefaultEngineName() throws Exception {
        engine = new EngineBuilder().enableJMX(true).build();
        engine.call();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        assertThat(mbs.isRegistered(new ObjectName(Utils.JMX_MBEAN_NAME + "name=" + Utils.DEFAULT_ENGINE_NAME + ",id=" + engine.getExecutionId()))).isTrue();
    }

    @Test
    public void whenEngineNameIsSpecified_thenTheJmxMBeanShouldBeRegisteredWithEngineName() throws Exception {
        String name = "master-engine";
        engine = new EngineBuilder().enableJMX(true).named(name).build();
        engine.call();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        assertThat(mbs.isRegistered(new ObjectName(Utils.JMX_MBEAN_NAME + "name=" + name + ",id=" + engine.getExecutionId()))).isTrue();
    }

    /*
     * Batch/Step event listeners tests
     */

    @Test
    @SuppressWarnings("unchecked")
    public void batchProcessEventListenerShouldBeInvokedForEachEvent() throws Exception {
        when(firstProcessor.processRecord(record1)).thenThrow(recordProcessingException);
        engine = new EngineBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .batchProcessEventListener(batchProcessEventListener)
                .build();
        engine.call();

        verify(batchProcessEventListener).beforeBatchStart();
        verify(batchProcessEventListener).onBatchException(recordProcessingException);
        verify(batchProcessEventListener).afterBatchEnd();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stepEventListenersShouldBeInvokedForEachEvent() throws Exception {
        engine = new EngineBuilder()
                .reader(reader)
                .recordReaderEventListener(recordReaderEventListener)
                .recordFilterEventListener(recordFilterEventListener)
                .recordMapperEventListener(recordMapperEventListener)
                .recordValidatorEventListener(recordValidatorEventListener)
                .recordProcessorEventListener(recordProcessorEventListener)
                .build();
        engine.call();

        verify(recordReaderEventListener).beforeReaderOpening();
        verify(recordReaderEventListener).afterReaderOpening();
        verify(recordReaderEventListener, times(2)).beforeRecordReading();
        verify(recordReaderEventListener).afterRecordReading(record1);
        verify(recordReaderEventListener).afterRecordReading(record2);
        verify(recordReaderEventListener).beforeReaderClosing();
        verify(recordReaderEventListener).afterReaderClosing();

        verify(recordFilterEventListener).beforeRecordFiltering(record1);
        verify(recordFilterEventListener).beforeRecordFiltering(record2);
        verify(recordFilterEventListener).afterRecordFiltering(record1, false);
        verify(recordFilterEventListener).afterRecordFiltering(record2, false);

        verify(recordMapperEventListener).beforeRecordMapping(record1);
        verify(recordMapperEventListener).beforeRecordMapping(record2);
        verify(recordMapperEventListener).afterRecordMapping(record1, record1);
        verify(recordMapperEventListener).afterRecordMapping(record2, record2);

        verify(recordValidatorEventListener).beforeRecordValidation(record1);
        verify(recordValidatorEventListener).beforeRecordValidation(record2);
        verify(recordValidatorEventListener).afterRecordValidation(record1, Collections.<ValidationError>emptySet());
        verify(recordValidatorEventListener).afterRecordValidation(record2, Collections.<ValidationError>emptySet());

        verify(recordProcessorEventListener).beforeRecordProcessing(record1);
        verify(recordProcessorEventListener).afterRecordProcessing(record1, null);
        verify(recordProcessorEventListener).beforeRecordProcessing(record2);
        verify(recordProcessorEventListener).afterRecordProcessing(record2, null);
    }

    /*
     * Custom reporting handlers tests
     */

    @Test
    public void whenARecordIsFiltered_thenTheCustomFilteredRecordHandlerShouldBeInvoked() throws Exception {
        when(firstFilter.filterRecord(record1)).thenReturn(true);
        engine = new EngineBuilder()
                .reader(reader)
                .filter(firstFilter)
                .filteredRecordHandler(filteredRecordHandler)
                .build();
        engine.call();

        verify(filteredRecordHandler).handle(record1);
    }

    @Test
    public void whenARecordIsIgnored_thenTheCustomIgnoredRecordHandlerShouldBeInvoked() throws Exception {
        when(mapper.mapRecord(record1)).thenThrow(recordMappingException);
        engine = new EngineBuilder()
                .reader(reader)
                .mapper(mapper)
                .ignoredRecordHandler(ignoredRecordHandler)
                .build();
        engine.call();

        verify(ignoredRecordHandler).handle(record1, recordMappingException);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenARecordIsRejected_thenTheCustomRejectedRecordHandlerShouldBeInvoked() throws Exception {
        Set<ValidationError> validationErrors = new HashSet<ValidationError>();
        validationErrors.add(new ValidationError("error"));
        when(firstValidator.validateRecord(record1)).thenReturn(validationErrors);
        engine = new EngineBuilder()
                .reader(reader)
                .validator(firstValidator)
                .rejectedRecordHandler(rejectedRecordHandler)
                .build();
        engine.call();

        verify(rejectedRecordHandler).handle(record1, validationErrors);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenARecordIsInError_thenTheCustomErrorRecordHandlerShouldBeInvoked() throws Exception {
        when(firstProcessor.processRecord(record1)).thenThrow(recordProcessingException);
        engine = new EngineBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .errorRecordHandler(errorRecordHandler)
                .build();
        engine.call();

        verify(errorRecordHandler).handle(record1, recordProcessingException);
    }

}
