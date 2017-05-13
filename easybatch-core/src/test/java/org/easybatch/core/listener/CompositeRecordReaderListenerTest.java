package org.easybatch.core.listener;

import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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