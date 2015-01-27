package org.easybatch.core.impl;

import org.easybatch.core.api.*;
import org.easybatch.core.api.event.global.BatchProcessEventListener;
import org.easybatch.core.api.event.record.*;
import org.easybatch.core.record.StringRecord;
import org.easybatch.core.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link Engine}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class EngineTest {

    private Engine engine;

    @Mock
    private StringRecord record1, record2;
    @Mock
    private RecordReader reader;
    @Mock
    private RecordFilter filter;
    @Mock
    private RecordMapper mapper;
    @Mock
    private RecordValidator validator;
    @Mock
    private RecordProcessor firstProcessor;
    @Mock
    private RecordProcessor secondProcessor;
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

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        when(record1.getNumber()).thenReturn(1);
        when(record1.getPayload()).thenReturn("test1");
        when(record2.getNumber()).thenReturn(2);
        when(record2.getPayload()).thenReturn("test2");
        when(reader.hasNextRecord()).thenReturn(true, true, false);
        when(reader.readNextRecord()).thenReturn(record1, record2);
        when(filter.filterRecord(record1)).thenReturn(false);
        when(filter.filterRecord(record2)).thenReturn(false);
        when(mapper.mapRecord(record1)).thenReturn(record1);
        when(mapper.mapRecord(record2)).thenReturn(record2);
        when(validator.validateRecord(record1)).thenReturn(Collections.emptySet());
        when(validator.validateRecord(record2)).thenReturn(Collections.emptySet());
        when(firstProcessor.processRecord(record1)).thenReturn(record1);
        when(firstProcessor.processRecord(record2)).thenReturn(record2);
        when(secondProcessor.processRecord(record1)).thenReturn(record1);
        when(secondProcessor.processRecord(record2)).thenReturn(record2);
        engine = new EngineBuilder()
                .reader(reader)
                .filter(filter)
                .mapper(mapper)
                .validator(validator)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .build();
    }

    /*
     * Core engine implementation tests
     */

    @Test
    @SuppressWarnings("unchecked")
    public void allComponentsShouldBeInvokedForEachRecord() throws Exception {

        engine.call();

        verify(filter).filterRecord(record1);
        verify(filter).filterRecord(record2);
        verify(mapper).mapRecord(record1);
        verify(mapper).mapRecord(record2);
        verify(validator).validateRecord(record1);
        verify(validator).validateRecord(record2);
        verify(firstProcessor).processRecord(record1);
        verify(secondProcessor).processRecord(record1);
        verify(firstProcessor).processRecord(record2);
        verify(secondProcessor).processRecord(record2);

    }

    @Test
    public void recordReaderShouldBeClosedAtTheEndOfExecution() throws Exception {
        when(record1.getNumber()).thenReturn(1);
        when(record1.getPayload()).thenReturn("test1");
        when(reader.hasNextRecord()).thenReturn(true, false);
        when(reader.readNextRecord()).thenReturn(record1);
        engine = new EngineBuilder().reader(reader).build();

        engine.call();

        verify(reader).close();
    }

    @Test
    public void whenNotAbleToOpenReader_ThenTheEngineShouldAbortExecution() throws Exception {
        doThrow(new Exception("error while opening record reader!")).when(reader).open();
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
    public void whenNotAbleToReadNextRecord_ThenTheEngineShouldAbortExecution() throws Exception {
        when(reader.hasNextRecord()).thenReturn(true);
        when(reader.readNextRecord()).thenThrow(new Exception());
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
    public void batchResultShouldBeReturnedFromTheLastProcessorInThePipeline() {
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
        when(secondProcessor.processRecord(record1)).thenThrow(new Exception());
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
    }

    @Test
    public void whenStrictModeIsEnabled_ThenTheEngineShouldAbortOnFirstMappingExceptionIfAny() throws Exception {
        when(mapper.mapRecord(record1)).thenThrow(new Exception());
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
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenStrictModeIsEnabled_ThenTheEngineShouldAbortOnFirstValidationExceptionIfAny() throws Exception {
        when(validator.validateRecord(record1)).thenThrow(new RuntimeException());
        engine = new EngineBuilder()
                .reader(reader)
                .validator(validator)
                .strictMode(true)
                .build();
        Report report = engine.call();
        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(1);
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(0);
        assertThat(report.getTotalRecords()).isEqualTo(1);
        assertThat(report.getStatus()).isEqualTo(Status.ABORTED);
    }

    @Test
    public void jmxMBeanShouldBeRegisteredAtEngineCreation() throws MalformedObjectNameException {
        engine = new EngineBuilder().enableJMX(true).build();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        assertThat(mbs.isRegistered(new ObjectName(Utils.JMX_MBEAN_NAME))).isTrue();
    }

    /*
     * Batch/Step event listeners tests
     */

    @Test
    @SuppressWarnings("unchecked")
    public void batchProcessEventListenerShouldBeInvokedForEachEvent() throws Exception {
        Exception exception = new Exception();
        when(firstProcessor.processRecord(record1)).thenThrow(exception);
        engine = new EngineBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .batchProcessEventListener(batchProcessEventListener)
                .build();
        engine.call();

        verify(batchProcessEventListener).beforeBatchStart();
        verify(batchProcessEventListener).onException(exception);
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

        verify(recordReaderEventListener).beforeReaderOpen();
        verify(recordReaderEventListener).afterReaderOpen();
        verify(recordReaderEventListener, times(2)).beforeRecordRead();
        verify(recordReaderEventListener).afterRecordRead(record1);
        verify(recordReaderEventListener).afterRecordRead(record2);
        verify(recordReaderEventListener).beforeReaderClose();
        verify(recordReaderEventListener).afterReaderClose();

        verify(recordFilterEventListener).beforeFilterRecord(record1);
        verify(recordFilterEventListener).beforeFilterRecord(record2);
        verify(recordFilterEventListener).afterFilterRecord(record1, false);
        verify(recordFilterEventListener).afterFilterRecord(record2, false);

        verify(recordMapperEventListener).beforeMapRecord(record1);
        verify(recordMapperEventListener).beforeMapRecord(record2);
        verify(recordMapperEventListener).afterMapRecord(record1, record1);
        verify(recordMapperEventListener).afterMapRecord(record2, record2);

        verify(recordValidatorEventListener).beforeValidateRecord(record1);
        verify(recordValidatorEventListener).beforeValidateRecord(record2);
        verify(recordValidatorEventListener).afterValidateRecord(record1, Collections.<ValidationError>emptySet());
        verify(recordValidatorEventListener).afterValidateRecord(record2, Collections.<ValidationError>emptySet());

        verify(recordProcessorEventListener).beforeProcessingRecord(record1);
        verify(recordProcessorEventListener).afterProcessingRecord(record1, null);
        verify(recordProcessorEventListener).beforeProcessingRecord(record2);
        verify(recordProcessorEventListener).afterProcessingRecord(record2, null);
    }

    /*
     * Custom reporting handlers tests
     */

    @Test
    public void whenARecordIsFiltered_thenTheCustomFilteredRecordHandlerShouldBeInvoked() {
        when(filter.filterRecord(record1)).thenReturn(true);
        engine = new EngineBuilder()
                .reader(reader)
                .filter(filter)
                .filteredRecordHandler(filteredRecordHandler)
                .build();
        engine.call();

        verify(filteredRecordHandler).handle(record1);
    }

    @Test
    public void whenARecordIsIgnored_thenTheCustomIgnoredRecordHandlerShouldBeInvoked() throws Exception {
        Exception exception = new Exception();
        when(mapper.mapRecord(record1)).thenThrow(exception);
        engine = new EngineBuilder()
                .reader(reader)
                .mapper(mapper)
                .ignoredRecordHandler(ignoredRecordHandler)
                .build();
        engine.call();

        verify(ignoredRecordHandler).handle(record1, exception);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenARecordIsRejected_thenTheCustomRejectedRecordHandlerShouldBeInvoked() throws Exception {
        Set<ValidationError> validationErrors = new HashSet<ValidationError>();
        validationErrors.add(new ValidationError("error"));
        when(validator.validateRecord(record1)).thenReturn(validationErrors);
        engine = new EngineBuilder()
                .reader(reader)
                .validator(validator)
                .rejectedRecordHandler(rejectedRecordHandler)
                .build();
        engine.call();

        verify(rejectedRecordHandler).handle(record1, validationErrors);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenARecordIsInError_thenTheCustomErrorRecordHandlerShouldBeInvoked() throws Exception {
        Exception exception = new Exception();
        when(firstProcessor.processRecord(record1)).thenThrow(exception);
        engine = new EngineBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .errorRecordHandler(errorRecordHandler)
                .build();
        engine.call();

        verify(errorRecordHandler).handle(record1, exception);
    }

}
