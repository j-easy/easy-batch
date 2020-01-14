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
package org.easybatch.core.listener;

import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompositePipelineListenerTest {

    @Mock
    private Record inputRecord, processedRecord, outputRecord;
    @Mock
    private Throwable exception;
    @Mock
    private PipelineListener pipelineListener1, pipelineListener2;

    private CompositePipelineListener compositePipelineListener;

    @Before
    public void setUp() throws Exception {
        when(pipelineListener1.beforeRecordProcessing(inputRecord)).thenReturn(processedRecord);
        when(pipelineListener2.beforeRecordProcessing(processedRecord)).thenReturn(outputRecord);
        compositePipelineListener = new CompositePipelineListener(asList(pipelineListener1, pipelineListener2));
    }

    @Test
    public void testBeforeRecordProcessing() throws Exception {
        compositePipelineListener.beforeRecordProcessing(inputRecord);

        InOrder inOrder = inOrder(pipelineListener1, pipelineListener2);
        inOrder.verify(pipelineListener1).beforeRecordProcessing(inputRecord);
        inOrder.verify(pipelineListener2).beforeRecordProcessing(processedRecord);
    }

    @Test
    public void testAfterRecordProcessing() throws Exception {
        compositePipelineListener.afterRecordProcessing(inputRecord, outputRecord);

        InOrder inOrder = inOrder(pipelineListener1, pipelineListener2);
        inOrder.verify(pipelineListener2).afterRecordProcessing(inputRecord, outputRecord);
        inOrder.verify(pipelineListener1).afterRecordProcessing(inputRecord, outputRecord);
    }

    @Test
    public void testOnRecordProcessingException() throws Exception {
        compositePipelineListener.onRecordProcessingException(inputRecord, exception);

        InOrder inOrder = inOrder(pipelineListener1, pipelineListener2);
        inOrder.verify(pipelineListener2).onRecordProcessingException(inputRecord, exception);
        inOrder.verify(pipelineListener1).onRecordProcessingException(inputRecord, exception);
    }

}
