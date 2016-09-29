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
        inOrder.verify(batchListener1).afterBatchProcessing(batch);
        inOrder.verify(batchListener2).afterBatchProcessing(batch);
    }

    @Test
    public void testAfterBatchWriting() throws Exception {
        compositeBatchListener.afterBatchWriting(batch);

        InOrder inOrder = inOrder(batchListener1, batchListener2);
        inOrder.verify(batchListener1).afterBatchWriting(batch);
        inOrder.verify(batchListener2).afterBatchWriting(batch);
    }

    @Test
    public void testOnBatchWritingException() throws Exception {
        compositeBatchListener.onBatchWritingException(batch, exception);

        InOrder inOrder = inOrder(batchListener1, batchListener2);
        inOrder.verify(batchListener1).onBatchWritingException(batch, exception);
        inOrder.verify(batchListener2).onBatchWritingException(batch, exception);
    }
}