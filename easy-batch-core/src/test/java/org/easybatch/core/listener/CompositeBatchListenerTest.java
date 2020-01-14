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

import org.easybatch.core.record.Batch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.class)
public class CompositeBatchListenerTest {

    @Mock
    private Batch batch;
    @Mock
    private Throwable exception;
    @Mock
    private BatchListener batchListener1, batchListener2;

    private CompositeBatchListener compositeBatchListener;

    @Before
    public void setUp() throws Exception {
        compositeBatchListener = new CompositeBatchListener(asList(batchListener1, batchListener2));
    }

    @Test
    public void testBeforeBatchReading() throws Exception {
        compositeBatchListener.beforeBatchReading();

        InOrder inOrder = inOrder(batchListener1, batchListener2);
        inOrder.verify(batchListener1).beforeBatchReading();
        inOrder.verify(batchListener2).beforeBatchReading();
    }

    @Test
    public void testAfterBatchProcessing() throws Exception {
        compositeBatchListener.afterBatchProcessing(batch);

        InOrder inOrder = inOrder(batchListener1, batchListener2);
        inOrder.verify(batchListener2).afterBatchProcessing(batch);
        inOrder.verify(batchListener1).afterBatchProcessing(batch);
    }

    @Test
    public void testAfterBatchWriting() throws Exception {
        compositeBatchListener.afterBatchWriting(batch);

        InOrder inOrder = inOrder(batchListener1, batchListener2);
        inOrder.verify(batchListener2).afterBatchWriting(batch);
        inOrder.verify(batchListener1).afterBatchWriting(batch);
    }

    @Test
    public void testOnBatchWritingException() throws Exception {
        compositeBatchListener.onBatchWritingException(batch, exception);

        InOrder inOrder = inOrder(batchListener1, batchListener2);
        inOrder.verify(batchListener2).onBatchWritingException(batch, exception);
        inOrder.verify(batchListener1).onBatchWritingException(batch, exception);
    }
}
