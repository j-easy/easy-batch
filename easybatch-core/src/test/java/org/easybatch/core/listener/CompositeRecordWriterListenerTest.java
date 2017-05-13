package org.easybatch.core.listener;

import org.easybatch.core.record.Batch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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