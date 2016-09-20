/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.listener.RecordReaderListener;
import org.easybatch.core.processor.ComputationalRecordProcessor;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.processor.RecordProcessingException;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.*;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.easybatch.core.retry.RetryPolicy;
import org.easybatch.core.validator.RecordValidationException;
import org.easybatch.core.validator.RecordValidator;
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
import java.util.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.easybatch.core.util.Utils.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class JobImplTest {

    private Job job;

    @Mock
    private Header header1, header2;
    @Mock
    private Record record1, record2;
    @Mock
    private RecordReader reader;
    @Mock
    private RecordReader unreliableReader;
    @Mock
    private RecordFilter filter;
    @Mock
    private RecordValidator validator;
    @Mock
    private RecordProcessor firstProcessor, secondProcessor;
    @Mock
    private ComputationalRecordProcessor computationalRecordProcessor;
    @Mock
    private JobResult jobResult;
    @Mock
    private JobReport jobReport;
    @Mock
    private JobListener jobListener;
    @Mock
    private RecordReaderListener recordReaderListener;
    @Mock
    private PipelineListener pipelineListener;
    @Mock
    private RecordReadingException recordReadingException;
    @Mock
    private RecordReaderOpeningException recordReaderOpeningException;
    @Mock
    private RecordProcessingException recordProcessingException;
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
        job = new JobBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .jobListener(jobListener)
                .build();
    }

    /*
     * Core job implementation tests
     */

    @Test
    public void allComponentsShouldBeInvokedForEachRecordInOrder() throws Exception {

        job.call();

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

        job.call();

        verify(reader, times(2)).hasNextRecord();
        verify(reader).readNextRecord();
        verify(reader).close();
    }

    @Test
    public void whenKeepAliveIsActivated_thenTheRecordReaderShouldNotBeClosedAtTheEndOfExecution() throws Exception {
        when(reader.hasNextRecord()).thenReturn(true, false);
        when(reader.readNextRecord()).thenReturn(record1);
        job = new JobBuilder().reader(reader, true).build();

        job.call();

        verify(reader, never()).close();
    }

    @Test
    public void whenNotAbleToOpenReader_ThenTheJobShouldBeFailed() throws Exception {
        doThrow(recordReaderOpeningException).when(reader).open();

        JobReport jobReport = JobExecutor.execute(job);

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FAILED);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getTotalCount()).isNull();
        assertThat(jobReport.getMetrics().getDuration()).isGreaterThanOrEqualTo(0);
        assertThat(jobReport.getMetrics().getLastError()).isEqualTo(recordReaderOpeningException);
        assertThat(jobReport.getParameters().getDataSource()).isNull();
    }

    @Test
    public void whenNotAbleToOpenReader_thenTheJobListenerShouldBeInvoked() throws Exception {
        doThrow(recordReaderOpeningException).when(reader).open();

        JobReport jobReport = JobExecutor.execute(job);

        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FAILED);
        assertThat(jobReport.getMetrics().getLastError()).isEqualTo(recordReaderOpeningException);
        verify(jobListener).afterJobEnd(jobReport);
    }

    @Test
    public void whenNotAbleToReadNextRecord_ThenTheJobShouldBeAborted() throws Exception {
        when(reader.hasNextRecord()).thenReturn(true);
        when(reader.readNextRecord()).thenThrow(recordReadingException);

        JobReport jobReport = JobExecutor.execute(job);

        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getTotalCount()).isNull();
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.ABORTED);
        assertThat(jobReport.getMetrics().getLastError()).isEqualTo(recordReadingException);
    }

    @Test
    public void jobResultShouldBeReturnedFromTheLastProcessorInThePipeline() throws Exception {
        when(computationalRecordProcessor.getComputationResult()).thenReturn(jobResult);

        job = new JobBuilder()
                .reader(reader)
                .processor(computationalRecordProcessor)
                .build();
        JobReport jobReport = JobExecutor.execute(job);

        assertThat(jobReport.getResult()).isEqualTo(jobResult);
    }

    @Test
    public void reportShouldBeCorrect() throws Exception {
        JobReport jobReport = JobExecutor.execute(job);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(2);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(jobReport.getMetrics().getLastError()).isNull();
    }

    @Test
    public void whenErrorThresholdIsSpecified_thenTheJobShouldBeAbortedWhenThresholdExceeded() throws Exception {
        when(firstProcessor.processRecord(record1)).thenReturn(record1);
        when(secondProcessor.processRecord(record1)).thenThrow(recordProcessingException);
        job = new JobBuilder()
                .reader(reader)
                .processor(firstProcessor)
                .processor(secondProcessor)
                .errorThreshold(1)
                .build();
        JobReport jobReport = JobExecutor.execute(job);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(1);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.ABORTED);
        verify(firstProcessor).processRecord(record1);
        verify(secondProcessor).processRecord(record1);
        verifyNoMoreInteractions(firstProcessor);
        verifyNoMoreInteractions(secondProcessor);
    }

    @Test
    public void whenARecordProcessorReturnsNull_thenTheRecordShouldBeFiltered() throws Exception {
        when(reader.hasNextRecord()).thenReturn(true, false);
        when(reader.readNextRecord()).thenReturn(record1);
        when(firstProcessor.processRecord(record1)).thenReturn(null);

        JobReport jobReport = JobExecutor.execute(job);

        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(1);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.COMPLETED);
        verify(firstProcessor).processRecord(record1);
        verify(secondProcessor, never()).processRecord(record1);
    }

    @Test
    public void whenARuntimeExceptionIsThrown_thenTheJobShouldFail() {
        when(reader.hasNextRecord()).thenThrow(runtimeException);
        JobReport jobReport = JobExecutor.execute(job);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FAILED);
        assertThat(jobReport.getMetrics().getLastError()).isEqualTo(runtimeException);
    }
    
    /*
     * JMX tests
     */

    @Test
    public void whenJobNameIsNotSpecified_thenTheJmxMBeanShouldBeRegisteredWithDefaultJobName() throws Exception {
        job = new JobBuilder().jmxMode(true).build();
        job.call();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        assertThat(mbs.isRegistered(new ObjectName(JMX_MBEAN_NAME + "name=" + JobParameters.DEFAULT_JOB_NAME + ",id=" + job.getExecutionId()))).isTrue();
    }

    @Test
    public void whenJobNameIsSpecified_thenTheJmxMBeanShouldBeRegisteredWithTheGivenJobName() throws Exception {
        String name = "master";
        job = new JobBuilder().jmxMode(true).named(name).build();
        job.call();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        assertThat(mbs.isRegistered(new ObjectName(JMX_MBEAN_NAME + "name=" + name + ",id=" + job.getExecutionId()))).isTrue();
    }

    /*
     * Skip & limit parameters tests
     */

    @Test
    public void testRecordSkipping() throws Exception {
        List<String> dataSource = Arrays.asList("foo", "bar");

        JobReport jobReport = aNewJob()
                .reader(new IterableRecordReader(dataSource))
                .skip(1)
                .processor(new RecordCollector())
                .call();

        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getSkippedCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(1);

        List<GenericRecord> records = (List<GenericRecord>) jobReport.getResult();

        assertThat(records).hasSize(1);
        assertThat(records.get(0).getPayload()).isEqualTo("bar");

    }

    @Test
    public void testRecordLimit() throws Exception {
        List<String> dataSource = Arrays.asList("foo", "bar", "baz");

        JobReport jobReport = aNewJob()
                .reader(new IterableRecordReader(dataSource))
                .limit(2)
                .processor(new RecordCollector())
                .call();

        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(2);

        List<GenericRecord> records = (List<GenericRecord>) jobReport.getResult();

        assertThat(records).extracting("payload").containsExactly("foo", "bar");
    }

    @Test
    public void testTimeout() throws Exception {
        List<String> dataSource = Arrays.asList("foo", "bar", "baz");

        JobReport jobReport = aNewJob()
                .reader(new IterableRecordReader(dataSource))
                .timeout(1, SECONDS)
                .processor(new RecordProcessor<Record, Record>() {
                    @Override
                    public Record processRecord(Record record) throws RecordProcessingException {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RecordProcessingException(e);
                        }
                        return record;
                    }
                })
                .call();

        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.ABORTED);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(1);
    }

    /*
     * Test retry parameter
     */

    @Test
    public void testRetry_whenMaxAttemptsExceeded() throws Exception {
        job = new JobBuilder()
                .reader(new UnreliableRecordReader(), new RetryPolicy(2, 1, SECONDS))
                .processor(new RecordCollector())
                .build();

        JobReport jobReport = job.call();

        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.ABORTED);
    }

    @Test
    public void testRetry_whenMaxAttemptsNotExceeded() throws Exception {
        job = new JobBuilder()
                .reader(new UnreliableRecordReader(), new RetryPolicy(5, 1, SECONDS))
                .processor(new RecordCollector())
                .build();

        JobReport jobReport = job.call();

        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(3);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(3);

        List<Record> records = (List<Record>) jobReport.getResult();
        assertThat(records).hasSize(3);
        assertThat(records.get(0).getPayload()).isEqualTo("r1");
        assertThat(records.get(1).getPayload()).isEqualTo("r2");
        assertThat(records.get(2).getPayload()).isEqualTo("r3");
    }

    /*
     * Job/Step listeners tests
     */

    @Test
    public void recordReaderListenerShouldBeInvokedForEachEvent() throws Exception {
        when(reader.hasNextRecord()).thenReturn(true, false);
        when(reader.readNextRecord()).thenReturn(record1);
        job = new JobBuilder()
                .reader(reader)
                .readerListener(recordReaderListener)
                .build();
        job.call();

        verify(recordReaderListener).beforeRecordReading();
        verify(recordReaderListener).afterRecordReading(record1);
    }

    @Test
    public void pipelineListenerShouldBeInvokedForEachEvent() throws Exception {

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

}
