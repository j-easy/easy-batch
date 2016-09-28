package org.easybatch.core.job;

import org.easybatch.core.filter.RecordFilter;
import org.easybatch.core.listener.*;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.validator.RecordValidator;
import org.easybatch.core.writer.RecordWriter;
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

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.JMX_MBEAN_NAME;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BatchJobTest {

    private Job job;

    @Mock
    private Record record1, record2;
    @Mock
    private RecordReader reader;
    @Mock
    private RecordFilter filter;
    @Mock
    private RecordValidator validator;
    @Mock
    private RecordProcessor<Record, Record> firstProcessor, secondProcessor;
    @Mock
    private RecordWriter writer;
    @Mock
    private JobReport jobReport;
    @Mock
    private JobListener jobListener;
    @Mock
    private BatchListener batchListener;
    @Mock
    private RecordReaderListener recordReaderListener;
    @Mock
    private RecordWriterListener recordWriterListener;
    @Mock
    private PipelineListener pipelineListener;
    @Mock
    private Exception exception;

    @Before
    public void setUp() throws Exception {
        when(reader.readRecord()).thenReturn(record1, record2, null);
        when(firstProcessor.processRecord(record1)).thenReturn(record1);
        when(firstProcessor.processRecord(record2)).thenReturn(record2);
        when(secondProcessor.processRecord(record1)).thenReturn(record1);
        when(secondProcessor.processRecord(record2)).thenReturn(record2);
        job = new JobBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .writer(writer)
                .jobListener(jobListener)
                .batchListener(batchListener)
                .readerListener(recordReaderListener)
                .writerListener(recordWriterListener)
                .pipelineListener(pipelineListener)
                .batchSize(2)
                .build();
    }

    /*
     * Core batch job implementation tests
     */

    @Test
    public void allComponentsShouldBeInvokedForEachRecordInOrder() throws Exception {

        new JobExecutor().execute(job);

        InOrder inOrder = Mockito.inOrder(reader, record1, record2, firstProcessor, secondProcessor, writer);

        inOrder.verify(reader).readRecord();
        inOrder.verify(firstProcessor).processRecord(record1);
        inOrder.verify(secondProcessor).processRecord(record1);

        inOrder.verify(reader).readRecord();
        inOrder.verify(firstProcessor).processRecord(record2);
        inOrder.verify(secondProcessor).processRecord(record2);

        inOrder.verify(writer).writeRecords(new Batch(record1, record2));
    }

    @Test
    public void readerShouldBeClosedAtTheEndOfExecution() throws Exception {
        job.call();

        verify(reader).close();
    }

    @Test
    public void writerShouldBeClosedAtTheEndOfExecution() throws Exception {
        job.call();

        verify(writer).close();
    }

    @Test
    public void whenNotAbleToOpenReader_ThenTheJobShouldFail() throws Exception {
        doThrow(exception).when(reader).open();

        JobReport jobReport = job.call();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FAILED);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(0);
        assertThat(jobReport.getLastError()).isEqualTo(exception);
        verify(reader).close();
        verify(writer).close();
    }

    @Test
    public void whenNotAbleToOpenWriter_ThenTheJobShouldFail() throws Exception {
        doThrow(exception).when(writer).open();

        JobReport jobReport = job.call();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FAILED);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(0);
        assertThat(jobReport.getLastError()).isEqualTo(exception);
        verify(reader).close();
        verify(writer).close();
    }

    @Test
    public void whenNotAbleToOpenReader_thenTheJobListenerShouldBeInvoked() throws Exception {
        doThrow(exception).when(reader).open();

        JobReport jobReport = job.call();

        verify(jobListener).afterJobEnd(jobReport);
        verify(reader).close();
        verify(writer).close();
    }

    @Test
    public void whenNotAbleToOpenWriter_thenTheJobListenerShouldBeInvoked() throws Exception {
        doThrow(exception).when(writer).open();

        JobReport jobReport = job.call();

        verify(jobListener).afterJobEnd(jobReport);
        verify(reader).close();
        verify(writer).close();
    }

    @Test
    public void whenNotAbleToReadNextRecord_ThenTheJobShouldFail() throws Exception {
        when(reader.readRecord()).thenThrow(exception);

        JobReport jobReport = job.call();

        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(0);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FAILED);
        assertThat(jobReport.getLastError()).isEqualTo(exception);
        verify(reader).close();
        verify(writer).close();
    }

    @Test
    public void whenNotAbleToWriteRecords_ThenTheJobShouldFail() throws Exception {
        doThrow(exception).when(writer).writeRecords(new Batch(record1, record2));

        JobReport jobReport = job.call();

        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(0);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FAILED);
        assertThat(jobReport.getLastError()).isEqualTo(exception);
        verify(reader).close();
        verify(writer).close();
    }

    @Test
    public void reportShouldBeCorrect() throws Exception {
        JobReport jobReport = job.call();
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(2);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(jobReport.getLastError()).isNull();
    }

    @Test
    public void whenErrorThresholdIsExceeded_ThenTheJobShouldBeAborted() throws Exception {
        when(firstProcessor.processRecord(record1)).thenThrow(exception);
        when(firstProcessor.processRecord(record2)).thenThrow(exception);
        job = new JobBuilder()
                .reader(reader)
                .writer(writer)
                .processor(firstProcessor)
                .errorThreshold(1)
                .build();

        JobReport jobReport = job.call();

        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(0);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FAILED);
        verify(reader).close();
        verify(writer).close();
    }

    @Test
    public void whenARecordProcessorReturnsNull_thenTheRecordShouldBeFiltered() throws Exception {
        when(reader.readRecord()).thenReturn(record1).thenReturn(null);
        when(firstProcessor.processRecord(record1)).thenReturn(null);

        JobReport jobReport = job.call();

        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(0);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.COMPLETED);
    }

    /*
     * JMX tests
     */

    @Test
    public void whenJobNameIsNotSpecified_thenTheJmxMBeanShouldBeRegisteredWithDefaultJobName() throws Exception {
        job = new JobBuilder().enableJmx(true).build();
        job.call();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        assertThat(mbs.isRegistered(new ObjectName(JMX_MBEAN_NAME + "name=" + JobParameters.DEFAULT_JOB_NAME))).isTrue();
    }

    @Test
    public void whenJobNameIsSpecified_thenTheJmxMBeanShouldBeRegisteredWithTheGivenJobName() throws Exception {
        String name = "master";
        job = new JobBuilder().enableJmx(true).named(name).build();
        job.call();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        assertThat(mbs.isRegistered(new ObjectName(JMX_MBEAN_NAME + "name=" + name))).isTrue();
    }

    /*
     * ***************
     * Listeners tests
     * ***************
     */

    /*
     * Job listener
     */
    @Test
    public void jobListenerShouldBeInvoked() throws Exception {
        job = new JobBuilder()
                .reader(reader)
                .jobListener(jobListener)
                .build();

        JobReport report = job.call();

        verify(jobListener).beforeJobStart(any(JobParameters.class));
        verify(jobListener).afterJobEnd(report);
    }

    /*
     * Batch listener
     */
    @Test
    public void batchListenerShouldBeInvokedForEachBatch() throws Exception {
        when(reader.readRecord()).thenReturn(record1, record2, null);
        job = new JobBuilder()
                .reader(reader)
                .writer(writer)
                .batchListener(batchListener)
                .batchSize(1)
                .build();

        job.call();

        Batch batch1 = new Batch(singletonList(record1));
        Batch batch2 = new Batch(singletonList(record2));

        InOrder inOrder = Mockito.inOrder(batchListener);
        inOrder.verify(batchListener).beforeBatchReading();
        inOrder.verify(batchListener).afterBatchProcessing(batch1);
        inOrder.verify(batchListener).afterBatchWriting(batch1);
        inOrder.verify(batchListener).beforeBatchReading();
        inOrder.verify(batchListener).afterBatchProcessing(batch2);
        inOrder.verify(batchListener).afterBatchWriting(batch2);
        inOrder.verify(batchListener).beforeBatchReading();
    }

    @Test
    public void whenWriterThrowsException_thenBatchListenerShouldBeInvoked() throws Exception {
        when(reader.readRecord()).thenReturn(record1, record2, null);
        doThrow(exception).when(writer).writeRecords(new Batch(record1, record2));

        job = new JobBuilder()
                .reader(reader)
                .writer(writer)
                .batchListener(batchListener)
                .batchSize(2)
                .build();

        job.call();

        Batch batch = new Batch(record1, record2);
        verify(batchListener, times(1)).beforeBatchReading();
        verify(batchListener).onBatchWritingException(batch, exception);
    }

    /*
     * Reader listener
     */
    @Test
    public void recordReaderListenerShouldBeInvokedForEachRecord() throws Exception {
        when(reader.readRecord()).thenReturn(record1, record2, null);
        job = new JobBuilder()
                .reader(reader)
                .readerListener(recordReaderListener)
                .build();

        job.call();

        verify(recordReaderListener, times(3)).beforeRecordReading();
        verify(recordReaderListener).afterRecordReading(record1);
        verify(recordReaderListener).afterRecordReading(record2);
    }

    @Test
    public void whenRecordReaderThrowException_thenReaderListenerShouldBeInvoked() throws Exception {
        when(reader.readRecord()).thenThrow(exception);
        job = new JobBuilder()
                .reader(reader)
                .readerListener(recordReaderListener)
                .build();

        job.call();

        verify(recordReaderListener).onRecordReadingException(exception);
    }

    /*
     * Writer listener
     */
    @Test
    public void recordWriterListenerShouldBeInvokedForEachBatch() throws Exception {
        when(reader.readRecord()).thenReturn(record1, record2, null);
        job = new JobBuilder()
                .reader(reader)
                .writer(writer)
                .writerListener(recordWriterListener)
                .batchSize(2)
                .build();

        job.call();

        Batch batch = new Batch(record1, record2);
        verify(recordWriterListener).beforeRecordWriting(batch);
        verify(recordWriterListener).afterRecordWriting(batch);
    }

    @Test
    public void whenRecordWriterThrowException_thenWriterListenerShouldBeInvoked() throws Exception {
        Batch batch = new Batch(record1, record2);
        doThrow(exception).when(writer).writeRecords(batch);
        job = new JobBuilder()
                .reader(reader)
                .writer(writer)
                .writerListener(recordWriterListener)
                .build();

        job.call();

        verify(recordWriterListener).onRecordWritingException(batch, exception);
    }

    /*
     * Pipeline listener
     */
    @Test
    public void pipelineListenerShouldBeInvokedForEachRecord() throws Exception {

        when(pipelineListener.beforeRecordProcessing(record1)).thenReturn(record1);
        when(pipelineListener.beforeRecordProcessing(record2)).thenReturn(record2);

        job = new JobBuilder()
                .reader(reader)
                .pipelineListener(pipelineListener)
                .build();

        job.call();

        verify(pipelineListener).beforeRecordProcessing(record1);
        verify(pipelineListener).afterRecordProcessing(record1, record1);
        verify(pipelineListener).beforeRecordProcessing(record2);
        verify(pipelineListener).afterRecordProcessing(record2, record2);
    }

    @Test
    public void whenProcessorThrowsException_thenPipelineListenerShouldBeInvoked() throws Exception {
        when(firstProcessor.processRecord(record1)).thenThrow(exception);

        job = new JobBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .pipelineListener(pipelineListener)
                .build();

        job.call();

        verify(pipelineListener).onRecordProcessingException(record1, exception);
    }

}