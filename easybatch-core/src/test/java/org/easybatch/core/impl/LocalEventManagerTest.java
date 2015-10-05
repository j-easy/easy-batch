/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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
package org.easybatch.core.impl;

import org.easybatch.core.api.*;
import org.easybatch.core.api.event.JobEventListener;
import org.easybatch.core.api.event.PipelineEventListener;
import org.easybatch.core.api.event.RecordReaderEventListener;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.record.StringRecord;
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
public class LocalEventManagerTest {

    @Mock
    private JobEventListener jobEventListener1, jobEventListener2;
    @Mock
    private RecordReaderEventListener recordReaderEventListener1, recordReaderEventListener2;
    @Mock
    private PipelineEventListener pipelineEventListener1, pipelineEventListener2;
    @Mock
    private Throwable throwable;
    @Mock
    private Record record, record1, record2;
    @Mock
    private Object processingResult;

    private LocalEventManager localEventManager;

    @Before
    public void setUp() {
        localEventManager = new LocalEventManager();

        localEventManager.addJobEventListener(jobEventListener1);
        localEventManager.addJobEventListener(jobEventListener2);

        localEventManager.addRecordReaderEventListener(recordReaderEventListener1);
        localEventManager.addRecordReaderEventListener(recordReaderEventListener2);

        localEventManager.addPipelineEventListener(pipelineEventListener1);
        localEventManager.addPipelineEventListener(pipelineEventListener2);
    }

    @Test
    public void fireBeforeJobStart() {
        localEventManager.fireBeforeJobStart();

        InOrder inOrder = inOrder(jobEventListener1, jobEventListener2);

        inOrder.verify(jobEventListener1).beforeJobStart();
        inOrder.verify(jobEventListener2).beforeJobStart();
    }

    @Test
    public void fireAfterJobEnd() {
        localEventManager.fireAfterJobEnd();

        InOrder inOrder = inOrder(jobEventListener1, jobEventListener2);

        inOrder.verify(jobEventListener1).afterJobEnd();
        inOrder.verify(jobEventListener2).afterJobEnd();
    }

    @Test
    public void fireBeforeRecordReading() {
        localEventManager.fireBeforeRecordReading();

        InOrder inOrder = inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).beforeRecordReading();
        inOrder.verify(recordReaderEventListener2).beforeRecordReading();
    }

    @Test
    public void fireAfterRecordReading() {
        localEventManager.fireAfterRecordReading(record);

        InOrder inOrder = inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).afterRecordReading(record);
        inOrder.verify(recordReaderEventListener2).afterRecordReading(record);
    }

    @Test
    public void fireOnRecordReadingException() {
        localEventManager.fireOnRecordReadingException(throwable);

        InOrder inOrder = inOrder(recordReaderEventListener1, recordReaderEventListener2);

        inOrder.verify(recordReaderEventListener1).onRecordReadingException(throwable);
        inOrder.verify(recordReaderEventListener2).onRecordReadingException(throwable);
    }
    @Test
    public void fireBeforeRecordProcessing() {
        when(pipelineEventListener1.beforeRecordProcessing(record)).thenReturn(record1);
        when(pipelineEventListener2.beforeRecordProcessing(record1)).thenReturn(record2);

        Object result = localEventManager.fireBeforeRecordProcessing(record);

        InOrder inOrder = inOrder(pipelineEventListener1, pipelineEventListener2);

        inOrder.verify(pipelineEventListener1).beforeRecordProcessing(record);
        inOrder.verify(pipelineEventListener2).beforeRecordProcessing(record1);

        assertThat(result).isEqualTo(record2);
    }

    @Test
    public void fireAfterRecordProcessing() {
        localEventManager.fireAfterRecordProcessing(record, processingResult);

        InOrder inOrder = inOrder(pipelineEventListener1, pipelineEventListener2);

        inOrder.verify(pipelineEventListener1).afterRecordProcessing(record, processingResult);
        inOrder.verify(pipelineEventListener2).afterRecordProcessing(record, processingResult);
    }

    @Test
    public void fireOnRecordProcessingException() {
        localEventManager.fireOnRecordProcessingException(record, throwable);

        InOrder inOrder = inOrder(pipelineEventListener1, pipelineEventListener2);

        inOrder.verify(pipelineEventListener1).onRecordProcessingException(record, throwable);
        inOrder.verify(pipelineEventListener2).onRecordProcessingException(record, throwable);
    }

    /*
     * Integration tests for custom step listeners.
     */

    @Test
    public void testRecordModificationThroughCustomPipelineEventListener() throws Exception {
        Engine engine = EngineBuilder.aNewEngine()
                .reader(new StringRecordReader("foo" + LINE_SEPARATOR + "bar"))

                .pipelineEventListener(new PipelineEventListener() {
                    @Override
                    public Object beforeRecordProcessing(Object record) {
                        StringRecord stringRecord = (StringRecord) record;
                        return new StringRecord(stringRecord.getHeader(), ";" + stringRecord.getPayload());
                    }

                    @Override
                    public void afterRecordProcessing(Object record, Object processingResult) {

                    }

                    @Override
                    public void onRecordProcessingException(Object record, Throwable throwable) {

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

        Report report = engine.call();

        assertThat(report.getRejectedRecordsCount()).isEqualTo(2);
    }
}