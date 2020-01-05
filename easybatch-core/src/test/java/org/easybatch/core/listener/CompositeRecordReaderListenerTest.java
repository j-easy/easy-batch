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

@RunWith(MockitoJUnitRunner.class)
public class CompositeRecordReaderListenerTest {

    @Mock
    private Record record;
    @Mock
    private Throwable exception;
    @Mock
    private RecordReaderListener recordReaderListener1, recordReaderListener2;

    private CompositeRecordReaderListener compositeRecordReaderListener;

    @Before
    public void setUp() throws Exception {
        compositeRecordReaderListener = new CompositeRecordReaderListener(asList(recordReaderListener1, recordReaderListener2));
    }

    @Test
    public void testBeforeRecordReading() throws Exception {
        compositeRecordReaderListener.beforeRecordReading();

        InOrder inOrder = inOrder(recordReaderListener1, recordReaderListener2);
        inOrder.verify(recordReaderListener1).beforeRecordReading();
        inOrder.verify(recordReaderListener2).beforeRecordReading();
    }

    @Test
    public void testAfterRecordReading() throws Exception {
        compositeRecordReaderListener.afterRecordReading(record);

        InOrder inOrder = inOrder(recordReaderListener1, recordReaderListener2);
        inOrder.verify(recordReaderListener2).afterRecordReading(record);
        inOrder.verify(recordReaderListener1).afterRecordReading(record);
    }

    @Test
    public void testOnRecordReadingException() throws Exception {
        compositeRecordReaderListener.onRecordReadingException(exception);

        InOrder inOrder = inOrder(recordReaderListener1, recordReaderListener2);
        inOrder.verify(recordReaderListener2).onRecordReadingException(exception);
        inOrder.verify(recordReaderListener1).onRecordReadingException(exception);
    }
}
