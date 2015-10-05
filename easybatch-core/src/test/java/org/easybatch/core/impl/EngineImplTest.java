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
import org.easybatch.core.api.event.JobEventListener;
import org.easybatch.core.api.event.PipelineEventListener;
import org.easybatch.core.api.event.RecordReaderEventListener;
import org.easybatch.core.api.handler.ErrorRecordHandler;
import org.easybatch.core.api.handler.FilteredRecordHandler;
import org.easybatch.core.api.handler.RejectedRecordHandler;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.reader.IterableRecordReader;
import org.easybatch.core.record.GenericRecord;
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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.EngineBuilder.aNewEngine;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class EngineImplTest {

    private Engine engine;

    @Mock
    private Header header1, header2;
    @Mock
    private Record record1, record2;
    @Mock
    private RecordReader reader;
    @Mock
    private RecordFilter filter;
    @Mock
    private RecordValidator validator;
    @Mock
    private RecordProcessor firstProcessor, secondProcessor;
    @Mock
    private ComputationalRecordProcessor computationalRecordProcessor;
    @Mock
    private Object jobResult;
    @Mock
    private JobEventListener jobEventListener;
    @Mock
    private RecordReaderEventListener recordReaderEventListener;
    @Mock
    private PipelineEventListener pipelineEventListener;
    @Mock
    private FilteredRecordHandler filteredRecordHandler;
    @Mock
    private RejectedRecordHandler rejectedRecordHandler;
    @Mock
    private ErrorRecordHandler errorRecordHandler;
    @Mock
    private RecordReadingException recordReadingException;
    @Mock
    private RecordReaderOpeningException recordReaderOpeningException;
    @Mock
    private RecordProcessingException recordProcessingException;
    @Mock
    private RecordFilteringException recordFilteringException;
    @Mock
    private RecordValidationException recordValidationException;
    @Mock
    private RuntimeException runtimeException;

    @Before
    public void setUp() throws Exception {
        when(record1.getHeader()).thenReturn(header1);
        when(record2.getHeader()).thenReturn(header2);
        when(reader.hasNextRecord()).thenReturn(true, true, false);
        when(reader.readNextRecord()).thenReturn(record1, record2);
        when(firstProcessor.processRecord(record1)).thenReturn(record1);
        when(firstProcessor.processRecord(record2)).thenReturn(record2);
        when(secondProcessor.processRecord(record1)).thenReturn(record1);
        when(secondProcessor.processRecord(record2)).thenReturn(record2);
        engine = new EngineBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .build();
    }

    /*
     * Core engine implementation tests
     */

    @Test
    public void allComponentsShouldBeInvokedForEachRecordInOrder() throws Exception {

        engine.call();

        InOrder inOrder = Mockito.inOrder(reader, record1, record2, firstProcessor, secondProcessor);

        inOrder.verify(reader).readNextRecord();
        inOrder.verify(firstProcessor).processRecord(record1);
        inOrder.verify(secondProcessor).processRecord(record1);

        inOrder.verify(reader).readNextRecord();
        inOrder.verify(firstProcessor).processRecord(record2);
        inOrder.verify(secondProcessor).processRecord(record2);

    }

    @Test
    public void recordReaderShouldBeClosedAtTheEndOfExecution() throws Exception {
        when(reader.hasNextRecord()).thenReturn(true, false);
        when(reader.readNextRecord()).thenReturn(record1);
        engine = new EngineBuilder().reader(reader).build();

        engine.call();

        verify(reader, times(2)).hasNextRecord();
        verify(reader).readNextRecord();
        verify(reader).close();
    }

    @Test
    public void whenKeepAliveIsActivated_thenTheRecordReaderShouldNotBeClosedAtTheEndOfExecution() throws Exception {
        when(reader.hasNextRecord()).thenReturn(true, false);
        when(reader.readNextRecord()).thenReturn(record1);
        engine = new EngineBuilder().reader(reader, true).build();

        engine.call();

        verify(reader, times(0)).close();
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

        engine = new EngineBuilder()
                .reader(reader)
                .build();

        Report report = engine.call();

        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(0);
        assertThat(report.getTotalRecords()).isNull();
        assertThat(report.getStatus()).isEqualTo(Status.ABORTED);
    }

    @Test
    public void jobResultShouldBeReturnedFromTheLastProcessorInThePipeline() throws Exception {
        when(computationalRecordProcessor.getComputationResult()).thenReturn(jobResult);

        engine = new EngineBuilder()
                .reader(reader)
                .processor(computationalRecordProcessor)
                .build();
        Report report = engine.call();

        assertThat(report.getJobResult()).isEqualTo(jobResult);
    }


    @Test
    public void whenRecordFilterThrowsARecordFilteringException_thenShouldFilterRecord() throws Exception {
        when(filter.processRecord(record1)).thenThrow(recordFilteringException);
        aNewEngine()
                .reader(reader)
                .filter(filter)
                .filteredRecordHandler(filteredRecordHandler)
                .build().call();

        verify(filteredRecordHandler).handle(record1, recordFilteringException);
    }

    @Test
    public void whenRecordValidatorThrowsARecordValidationException_thenShouldRejectRecord() throws Exception {
        when(validator.processRecord(record1)).thenThrow(recordValidationException);
        aNewEngine()
                .reader(reader)
                .validator(validator)
                .rejectedRecordHandler(rejectedRecordHandler)
                .build().call();

        verify(rejectedRecordHandler).handle(record1, recordValidationException);
    }

    @Test
    public void whenRecordProcessorThrowsARecordProcessingException_thenRecordShouldBeInError() throws Exception {
        when(firstProcessor.processRecord(record1)).thenThrow(recordProcessingException);
        aNewEngine()
                .reader(reader)
                .processor(firstProcessor)
                .errorRecordHandler(errorRecordHandler)
                .build().call();

        verify(errorRecordHandler).handle(record1, recordProcessingException);
    }

    @Test
    public void reportShouldBeCorrect() throws Exception {
        Report report = engine.call();
        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(2);
        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getStatus()).isEqualTo(Status.FINISHED);
    }

    @Test
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
    public void whenStrictModeIsEnabled_ThenTheEngineShouldAbortOnFirstRecordProcessingExceptionIfAny() throws Exception {
        when(firstProcessor.processRecord(record1)).thenThrow(recordProcessingException);

        engine = new EngineBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .strictMode(true)
                .build();
        Report report = engine.call();

        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getErrorRecordsCount()).isEqualTo(1);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(0);
        assertThat(report.getTotalRecords()).isEqualTo(1);
        assertThat(report.getStatus()).isEqualTo(Status.ABORTED);
        Mockito.verifyZeroInteractions(secondProcessor);
    }

    /*
     * JMX tests
     */

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
     * Skip & limit parameters tests
     */

    @Test
    public void testRecordSkipping() throws Exception {
        List<String> dataSource = Arrays.asList("foo", "bar");

        Engine engine = aNewEngine()
                .reader(new IterableRecordReader(dataSource))
                .skip(1)
                .processor(new RecordCollector<StringRecord>())
                .build();

        Report report = engine.call();

        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getSkippedRecordsCount()).isEqualTo(1);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(1);

        List<GenericRecord> records = (List<GenericRecord>) report.getJobResult();

        assertThat(records).isNotNull().hasSize(1);
        assertThat(records.get(0).getPayload()).isNotNull().isEqualTo("bar");

    }

    @Test
    public void testRecordLimit() throws Exception {
        List<String> dataSource = Arrays.asList("foo", "bar", "baz");

        Engine engine = aNewEngine()
                .reader(new IterableRecordReader(dataSource))
                .limit(2)
                .processor(new RecordCollector<StringRecord>())
                .build();

        Report report = engine.call();

        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(2);

        List<GenericRecord> records = (List<GenericRecord>) report.getJobResult();

        assertThat(records).isNotNull().hasSize(2);
        assertThat(records.get(0).getPayload()).isNotNull().isEqualTo("foo");
        assertThat(records.get(1).getPayload()).isNotNull().isEqualTo("bar");

    }

    /*
     * Job/Step event listeners tests
     */

    @Test
    public void recordReaderEventListenerShouldBeInvokedForEachEvent() throws Exception {
        when(reader.hasNextRecord()).thenReturn(true, false);
        when(reader.readNextRecord()).thenReturn(record1);
        engine = new EngineBuilder()
                .reader(reader)
                .recordReaderEventListener(recordReaderEventListener)
                .build();
        engine.call();

        verify(recordReaderEventListener).beforeRecordReading();
        verify(recordReaderEventListener).afterRecordReading(record1);
    }

    @Test
    public void pipelineEventListenersShouldBeInvokedForEachEvent() throws Exception {

        when(pipelineEventListener.beforeRecordProcessing(record1)).thenReturn(record1);
        when(pipelineEventListener.beforeRecordProcessing(record2)).thenReturn(record2);

        engine = new EngineBuilder()
                .reader(reader)
                .recordReaderEventListener(recordReaderEventListener)
                .pipelineEventListener(pipelineEventListener)
                .build();
        engine.call();

        verify(recordReaderEventListener, times(2)).beforeRecordReading();
        verify(recordReaderEventListener).afterRecordReading(record1);
        verify(recordReaderEventListener).afterRecordReading(record2);

        verify(pipelineEventListener).beforeRecordProcessing(record1);
        verify(pipelineEventListener).afterRecordProcessing(record1, null);
        verify(pipelineEventListener).beforeRecordProcessing(record2);
        verify(pipelineEventListener).afterRecordProcessing(record2, null);
    }

    /*
     * Custom reporting handlers tests
     */

    @Test
    public void exceptionsThrownByCustomRecordProcessingListenersShouldBeHandledProperly() throws Exception {
        when(pipelineEventListener.beforeRecordProcessing(record1)).thenThrow(runtimeException);

        engine = new EngineBuilder()
                .reader(reader)
                .errorRecordHandler(errorRecordHandler)
                .pipelineEventListener(pipelineEventListener)
                .build();

        engine.call();

        verify(errorRecordHandler).handle(record1, runtimeException);
    }

}
