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
public class CompositeRecordWriterListenerTest {

    @Mock
    private Batch batch;
    @Mock
    private Throwable exception;
    @Mock
    private RecordWriterListener recordWriterListener1, recordWriterListener2;

    private CompositeRecordWriterListener compositeRecordWriterListener;

    @Before
    public void setUp() throws Exception {
        compositeRecordWriterListener = new CompositeRecordWriterListener(asList(recordWriterListener1, recordWriterListener2));
    }

    @Test
    public void testBeforeRecordWriting() throws Exception {
        compositeRecordWriterListener.beforeRecordWriting(batch);

        InOrder inOrder = inOrder(recordWriterListener1, recordWriterListener2);
        inOrder.verify(recordWriterListener1).beforeRecordWriting(batch);
        inOrder.verify(recordWriterListener2).beforeRecordWriting(batch);

    }

    @Test
    public void testAfterRecordWriting() throws Exception {
        compositeRecordWriterListener.afterRecordWriting(batch);

        InOrder inOrder = inOrder(recordWriterListener1, recordWriterListener2);
        inOrder.verify(recordWriterListener2).afterRecordWriting(batch);
        inOrder.verify(recordWriterListener1).afterRecordWriting(batch);
    }

    @Test
    public void testOnRecordWritingException() throws Exception {
        compositeRecordWriterListener.onRecordWritingException(batch, exception);

        InOrder inOrder = inOrder(recordWriterListener1, recordWriterListener2);
        inOrder.verify(recordWriterListener2).onRecordWritingException(batch, exception);
        inOrder.verify(recordWriterListener1).onRecordWritingException(batch, exception);
    }
}
