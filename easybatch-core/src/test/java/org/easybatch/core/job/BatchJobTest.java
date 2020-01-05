/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.core.job;

import org.easybatch.core.filter.RecordFilter;
import org.easybatch.core.listener.*;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.IterableRecordReader;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.validator.RecordValidator;
import org.easybatch.core.writer.RecordWriter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.JMX_MBEAN_NAME;
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
    private JobListener jobListener1;
    @Mock
    private JobListener jobListener2;
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
        when(pipelineListener.beforeRecordProcessing(record1)).thenReturn(record1);
        when(pipelineListener.beforeRecordProcessing(record2)).thenReturn(record2);
        job = new JobBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .writer(writer)
                .jobListener(jobListener1)
                .jobListener(jobListener2)
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
        assertThat(jobReport.getMetrics().getFilterCount()).isEqualTo(0);
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
        assertThat(jobReport.getMetrics().getFilterCount()).isEqualTo(0);
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

        JobReport jobReportInternal = job.call();
        
        InOrder inOrder = inOrder(jobListener1, jobListener2, reader, writer);
        inOrder.verify(jobListener2).afterJobEnd(jobReportInternal);
        inOrder.verify(jobListener1).afterJobEnd(jobReportInternal);
        inOrder.verify(reader).close();
        inOrder.verify(writer).close();

    }

    @Test
    public void whenNotAbleToOpenWriter_thenTheJobListenerShouldBeInvoked() throws Exception {
        doThrow(exception).when(writer).open();

        JobReport jobReport = job.call();

        verify(jobListener1).afterJobEnd(jobReport);
        verify(reader).close();
        verify(writer).close();
    }

    @Test
    public void whenNotAbleToReadNextRecord_ThenTheJobShouldFail() throws Exception {
        when(reader.readRecord()).thenThrow(exception);

        JobReport jobReport = job.call();

        assertThat(jobReport.getMetrics().getFilterCount()).isEqualTo(0);
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
        when(pipelineListener.beforeRecordProcessing(record1)).thenReturn(record1);
        when(pipelineListener.beforeRecordProcessing(record2)).thenReturn(record2);
        doThrow(exception).when(writer).writeRecords(new Batch(record1, record2));

        JobReport jobReport = job.call();

        assertThat(jobReport.getMetrics().getFilterCount()).isEqualTo(0);
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
        when(pipelineListener.beforeRecordProcessing(record1)).thenReturn(record1);
        when(pipelineListener.beforeRecordProcessing(record2)).thenReturn(record2);

        JobReport jobReport = job.call();
        assertThat(jobReport.getMetrics().getFilterCount()).isEqualTo(0);
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

        assertThat(jobReport.getMetrics().getFilterCount()).isEqualTo(0);
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

        assertThat(jobReport.getMetrics().getFilterCount()).isEqualTo(1);
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
                .jobListener(jobListener1)
                .build();

        JobReport report = job.call();

        verify(jobListener1).beforeJobStart(any(JobParameters.class));
        verify(jobListener1).afterJobEnd(report);
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
    public void multipleBatchListenerShouldBeInvokedForEachBatchInOrder() throws Exception {
        BatchListener batchListener1 = mock(BatchListener.class);
        BatchListener batchListener2 = mock(BatchListener.class);
        when(reader.readRecord()).thenReturn(record1, record2, null);
        job = new JobBuilder()
                .reader(reader)
                .writer(writer)
                .batchListener(batchListener1)
                .batchListener(batchListener2)
                .batchSize(1)
                .build();

        job.call();

        Batch batch1 = new Batch(singletonList(record1));
        Batch batch2 = new Batch(singletonList(record2));

        InOrder inOrder = Mockito.inOrder(batchListener1, batchListener2);
        inOrder.verify(batchListener1).beforeBatchReading();
        inOrder.verify(batchListener2).beforeBatchReading();
        inOrder.verify(batchListener2).afterBatchProcessing(batch1);
        inOrder.verify(batchListener1).afterBatchProcessing(batch1);
        inOrder.verify(batchListener2).afterBatchWriting(batch1);
        inOrder.verify(batchListener1).afterBatchWriting(batch1);
        //--
        inOrder.verify(batchListener1).beforeBatchReading();
        inOrder.verify(batchListener2).beforeBatchReading();
        inOrder.verify(batchListener2).afterBatchProcessing(batch2);
        inOrder.verify(batchListener1).afterBatchProcessing(batch2);
        inOrder.verify(batchListener2).afterBatchWriting(batch2);
        inOrder.verify(batchListener1).afterBatchWriting(batch2);
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
        when(pipelineListener.beforeRecordProcessing(record1)).thenReturn(record1);
        when(firstProcessor.processRecord(record1)).thenThrow(exception);

        job = new JobBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .pipelineListener(pipelineListener)
                .build();

        job.call();

        verify(pipelineListener).onRecordProcessingException(record1, exception);
    }
    
    @Test
    public void allJobListenersShouldBeInvokedForEachRecordInOrder() throws Exception {

        JobReport jobReportReturned = job.call();

        InOrder inOrder = Mockito.inOrder(reader, firstProcessor, secondProcessor, jobListener1, jobListener2);

        inOrder.verify(jobListener1).beforeJobStart(any(JobParameters.class));
        inOrder.verify(jobListener2).beforeJobStart(any(JobParameters.class));

        inOrder.verify(reader).readRecord();
        inOrder.verify(firstProcessor).processRecord(record1);
        inOrder.verify(secondProcessor).processRecord(record1);
        inOrder.verify(reader).readRecord();
        inOrder.verify(firstProcessor).processRecord(record2);
        inOrder.verify(secondProcessor).processRecord(record2);
        inOrder.verify(reader).close();
        inOrder.verify(jobListener2).afterJobEnd(jobReportReturned);
        inOrder.verify(jobListener1).afterJobEnd(jobReportReturned);
    }
    
    @Test
    public void allRecordReaderListenersShouldBeInvokedForEachRecordInOrder() throws Exception {

        RecordReaderListener readerListener1 = mock(RecordReaderListener.class);
        RecordReaderListener readerListener2 = mock(RecordReaderListener.class);
        new JobBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .readerListener(readerListener1)
                .readerListener(readerListener2)
                .build().call();

        InOrder inOrder = Mockito.inOrder(reader, firstProcessor, secondProcessor, readerListener1, readerListener2);

        inOrder.verify(readerListener1).beforeRecordReading();
        inOrder.verify(readerListener2).beforeRecordReading();
        inOrder.verify(reader).readRecord();
        inOrder.verify(readerListener2).afterRecordReading(record1);
        inOrder.verify(readerListener1).afterRecordReading(record1);
        inOrder.verify(firstProcessor).processRecord(record1);
        inOrder.verify(secondProcessor).processRecord(record1);
        inOrder.verify(readerListener1).beforeRecordReading();
        inOrder.verify(readerListener2).beforeRecordReading();
        inOrder.verify(reader).readRecord();
        inOrder.verify(readerListener2).afterRecordReading(record2);
        inOrder.verify(readerListener1).afterRecordReading(record2);
        inOrder.verify(firstProcessor).processRecord(record2);
        inOrder.verify(secondProcessor).processRecord(record2);
        inOrder.verify(reader).close();
    }

    @Test
    public void allRecordWriterListenersShouldBeInvokedForEachRecordInOrder() throws Exception {

        RecordWriterListener writerListener1 = mock(RecordWriterListener.class);
        RecordWriterListener writerListener2 = mock(RecordWriterListener.class);
        new JobBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .writerListener(writerListener1)
                .writerListener(writerListener2)
                .batchSize(2)
                .writer(writer)
                .build().call();

        Batch batch = new Batch(record1, record2);
        InOrder inOrder = Mockito.inOrder(reader, writer, firstProcessor, secondProcessor, writerListener1, writerListener2);

        
        inOrder.verify(writer).open();
        
        inOrder.verify(reader).readRecord();
        inOrder.verify(firstProcessor).processRecord(record1);
        inOrder.verify(secondProcessor).processRecord(record1);
        inOrder.verify(reader).readRecord();
        inOrder.verify(firstProcessor).processRecord(record2);
        inOrder.verify(secondProcessor).processRecord(record2);
        
        inOrder.verify(writerListener1).beforeRecordWriting(batch);
        inOrder.verify(writerListener2).beforeRecordWriting(batch);
        inOrder.verify(writer).writeRecords(batch);
        inOrder.verify(writerListener2).afterRecordWriting(batch);
        inOrder.verify(writerListener1).afterRecordWriting(batch);
        inOrder.verify(reader).close();
        inOrder.verify(writer).close();
    }
    

    @Test
    public void allPipelineListenersShouldBeInvokedForEachRecordInOrder() throws Exception {

        PipelineListener pipelineListener1 = mock(PipelineListener.class);
        PipelineListener pipelineListener2 = mock(PipelineListener.class);

        doReturn(record1).when(pipelineListener1).beforeRecordProcessing(record1);
        doReturn(record2).when(pipelineListener1).beforeRecordProcessing(record2);
        doReturn(record1).when(pipelineListener2).beforeRecordProcessing(record1);
        doReturn(record2).when(pipelineListener2).beforeRecordProcessing(record2);
        doNothing().when(pipelineListener1).afterRecordProcessing(record1, record1);
        doNothing().when(pipelineListener1).afterRecordProcessing(record2, record2);
        doNothing().when(pipelineListener2).afterRecordProcessing(record1, record1);
        doNothing().when(pipelineListener2).afterRecordProcessing(record2, record2);
        
         new JobBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .pipelineListener(pipelineListener1)
                .pipelineListener(pipelineListener2)
                .build().call();

        InOrder inOrder = Mockito.inOrder(reader, firstProcessor, secondProcessor, pipelineListener1, pipelineListener2);

        inOrder.verify(reader).readRecord();
        inOrder.verify(pipelineListener1).beforeRecordProcessing(record1);
        inOrder.verify(pipelineListener2).beforeRecordProcessing(record1);
        inOrder.verify(firstProcessor).processRecord(record1);
        inOrder.verify(secondProcessor).processRecord(record1);
        inOrder.verify(pipelineListener2).afterRecordProcessing(record1, record1);
        inOrder.verify(pipelineListener1).afterRecordProcessing(record1, record1);

        inOrder.verify(reader).readRecord();
        inOrder.verify(pipelineListener1).beforeRecordProcessing(record2);
        inOrder.verify(pipelineListener2).beforeRecordProcessing(record2);
        inOrder.verify(firstProcessor).processRecord(record2);
        inOrder.verify(secondProcessor).processRecord(record2);
        inOrder.verify(pipelineListener2).afterRecordProcessing(record2, record2);
        inOrder.verify(pipelineListener1).afterRecordProcessing(record2, record2);

        inOrder.verify(reader).close();
    }

    @Test
    public void whenPreProcessorReturnsNull_thenTheRecordShouldBeSkipped() throws Exception {
        when(pipelineListener.beforeRecordProcessing(record1)).thenReturn(record1);
        when(pipelineListener.beforeRecordProcessing(record2)).thenReturn(null);
        when(firstProcessor.processRecord(record1)).thenReturn(record1);

        job = new JobBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .pipelineListener(pipelineListener)
                .build();

        job.call();

        verify(firstProcessor, times(1)).processRecord(record1);
        verify(firstProcessor, never()).processRecord(record2);
        verify(pipelineListener).afterRecordProcessing(record1, record1);
        verify(pipelineListener).afterRecordProcessing(record2, null);
    }

    /*
     * Job Interruption tests
     *
     * FIXME Is there a better way to test this ?
     */

    @Test
    @Ignore("This test may fail if the interruption signal is intercepted after starting the second batch")
    public void whenAJobIsInterrupted_thenNextBatchesShouldBeIgnored() throws Exception {
        // Given
        RecordCollector recordCollector = new RecordCollector();
        List<Integer> dataSource = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            dataSource.add(i);
        }

        Job job = JobBuilder.aNewJob()
                .reader(new IterableRecordReader(dataSource))
                .processor(recordCollector)
                .batchSize(500000)
                .build();

        // When
        JobExecutor jobExecutor = new JobExecutor();
        Future<JobReport> jobReportFuture = jobExecutor.submit(job);

        Thread.sleep(50); // prevent aborting the job before even starting
        jobReportFuture.cancel(true);
        jobExecutor.awaitTermination(5, TimeUnit.SECONDS);

        // Then

        // can't assert on job report because jobReportFuture.get throws java.util.concurrent.CancellationException since it is cancelled
        assertThat(recordCollector.getRecords()).hasSize(500000);
    }

    @Test
    @Ignore("This test may fail if the interruption signal is intercepted after starting the second job")
    public void whenAJobIsInterrupted_thenOtherJobsShouldNotBeInterrupted() throws Exception {
        // Given
        RecordCollector recordCollector1 = new RecordCollector();
        RecordCollector recordCollector2 = new RecordCollector();
        List<Integer> dataSource = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            dataSource.add(i);
        }

        Job job1 = JobBuilder.aNewJob()
                .named("job1")
                .reader(new IterableRecordReader(dataSource))
                .processor(recordCollector1)
                .batchSize(500000)
                .build();
        Job job2 = JobBuilder.aNewJob()
                .named("job2")
                .reader(new IterableRecordReader(dataSource))
                .processor(recordCollector2)
                .batchSize(500000)
                .build();

        // When
        JobExecutor jobExecutor = new JobExecutor();
        Future<JobReport> jobReportFuture1 = jobExecutor.submit(job1);
        Future<JobReport> jobReportFuture2 = jobExecutor.submit(job2);

        Thread.sleep(50); // prevent aborting the job before even starting
        jobReportFuture1.cancel(true);
        jobExecutor.awaitTermination(5, TimeUnit.SECONDS);

        // Then

        // can't assert on job report because jobReportFuture.get throws java.util.concurrent.CancellationException since it is cancelled
        assertThat(recordCollector1.getRecords()).hasSize(500000);

        JobReport jobReport2 = jobReportFuture2.get();
        assertThat(jobReport2.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(recordCollector2.getRecords()).hasSize(1000000);
    }

}
