/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.easybatch.core.job;

import org.easybatch.core.listener.JobListener;
import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.listener.RecordReaderListener;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;
import org.easybatch.core.validator.RecordValidationException;
import org.easybatch.core.validator.RecordValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventManagerTest {

    @Mock
    private JobListener jobListener1, jobListener2;
    @Mock
    private RecordReaderListener recordReaderListener1, recordReaderListener2;
    @Mock
    private PipelineListener pipelineListener1, pipelineListener2;
    @Mock
    private Throwable throwable;
    @Mock
    private Record record, processedRecord, record1, record2;
    @Mock
    private JobReport jobReport;
    @Mock
    private JobParameters jobParameters;

    private EventManager eventManager;

    @Before
    public void setUp() {
        eventManager = new EventManager();

        eventManager.addJobListener(jobListener1);
        eventManager.addJobListener(jobListener2);

        eventManager.addRecordReaderListener(recordReaderListener1);
        eventManager.addRecordReaderListener(recordReaderListener2);

        eventManager.addPipelineListener(pipelineListener1);
        eventManager.addPipelineListener(pipelineListener2);
    }

    @Test
    public void fireBeforeJobStart() {
        eventManager.fireBeforeJobStart(jobParameters);

        InOrder inOrder = inOrder(jobListener1, jobListener2);

        inOrder.verify(jobListener1).beforeJobStart(jobParameters);
        inOrder.verify(jobListener2).beforeJobStart(jobParameters);
    }

    @Test
    public void fireAfterJobEnd() {
        eventManager.fireAfterJobEnd(jobReport);

        InOrder inOrder = inOrder(jobListener1, jobListener2);

        inOrder.verify(jobListener1).afterJobEnd(jobReport);
        inOrder.verify(jobListener2).afterJobEnd(jobReport);
    }

    @Test
    public void fireBeforeRecordReading() {
        eventManager.fireBeforeRecordReading();

        InOrder inOrder = inOrder(recordReaderListener1, recordReaderListener2);

        inOrder.verify(recordReaderListener1).beforeRecordReading();
        inOrder.verify(recordReaderListener2).beforeRecordReading();
    }

    @Test
    public void fireAfterRecordReading() {
        eventManager.fireAfterRecordReading(record);

        InOrder inOrder = inOrder(recordReaderListener1, recordReaderListener2);

        inOrder.verify(recordReaderListener1).afterRecordReading(record);
        inOrder.verify(recordReaderListener2).afterRecordReading(record);
    }

    @Test
    public void fireOnRecordReadingException() {
        eventManager.fireOnRecordReadingException(throwable);

        InOrder inOrder = inOrder(recordReaderListener1, recordReaderListener2);

        inOrder.verify(recordReaderListener1).onRecordReadingException(throwable);
        inOrder.verify(recordReaderListener2).onRecordReadingException(throwable);
    }

    @Test
    public void fireBeforeRecordProcessing() {
        when(pipelineListener1.beforeRecordProcessing(record)).thenReturn(record1);
        when(pipelineListener2.beforeRecordProcessing(record1)).thenReturn(record2);

        Object result = eventManager.fireBeforeRecordProcessing(record);

        InOrder inOrder = inOrder(pipelineListener1, pipelineListener2);

        inOrder.verify(pipelineListener1).beforeRecordProcessing(record);
        inOrder.verify(pipelineListener2).beforeRecordProcessing(record1);

        assertThat(result).isEqualTo(record2);
    }

    @Test
    public void fireAfterRecordProcessing() {
        eventManager.fireAfterRecordProcessing(record, processedRecord);

        InOrder inOrder = inOrder(pipelineListener1, pipelineListener2);

        inOrder.verify(pipelineListener1).afterRecordProcessing(record, processedRecord);
        inOrder.verify(pipelineListener2).afterRecordProcessing(record, processedRecord);
    }

    @Test
    public void fireOnRecordProcessingException() {
        eventManager.fireOnRecordProcessingException(record, throwable);

        InOrder inOrder = inOrder(pipelineListener1, pipelineListener2);

        inOrder.verify(pipelineListener1).onRecordProcessingException(record, throwable);
        inOrder.verify(pipelineListener2).onRecordProcessingException(record, throwable);
    }

    /*
     * Integration tests for custom step listeners.
     */

    @Test
    public void testRecordModificationThroughCustomPipelineListener() throws Exception {
        Job job = JobBuilder.aNewJob()
                .reader(new StringRecordReader("foo" + LINE_SEPARATOR + "bar"))

                .pipelineListener(new PipelineListener() {
                    @Override
                    public Record beforeRecordProcessing(Record record) {
                        StringRecord stringRecord = (StringRecord) record;
                        return new StringRecord(stringRecord.getHeader(), ";" + stringRecord.getPayload());
                    }

                    @Override
                    public void afterRecordProcessing(Record record, Record processingResult) {

                    }

                    @Override
                    public void onRecordProcessingException(Record record, Throwable throwable) {

                    }
                })
                .validator(new RecordValidator<StringRecord>() {
                    @Override
                    public StringRecord processRecord(StringRecord record) throws RecordValidationException {
                        if (record.getPayload().startsWith(";")) {
                            throw new RecordValidationException();
                        }
                        return record;
                    }
                })
                .build();

        JobReport jobReport = JobExecutor.execute(job);

        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(2);
    }
}
