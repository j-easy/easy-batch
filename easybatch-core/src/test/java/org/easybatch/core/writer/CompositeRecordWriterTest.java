package org.easybatch.core.writer;

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
public class CompositeRecordWriterTest {

    @Mock
    private Batch batch;
    @Mock
    private RecordWriter recordWriter1, recordWriter2;

    private CompositeRecordWriter compositeRecordWriter;

    @Before
    public void setUp() throws Exception {
        compositeRecordWriter = new CompositeRecordWriter(asList(recordWriter1, recordWriter2));
    }

    @Test
    public void testOpen() throws Exception {
        compositeRecordWriter.open();

        InOrder inOrder = inOrder(recordWriter1, recordWriter2);
        inOrder.verify(recordWriter1).open();
        inOrder.verify(recordWriter2).open();
    }

    @Test
    public void testWriteRecords() throws Exception {
        compositeRecordWriter.writeRecords(batch);

        InOrder inOrder = inOrder(recordWriter1, recordWriter2);
        inOrder.verify(recordWriter1).writeRecords(batch);
        inOrder.verify(recordWriter2).writeRecords(batch);
    }

    @Test
    public void testClose() throws Exception {
        compositeRecordWriter.close();

        InOrder inOrder = inOrder(recordWriter1, recordWriter2);
        inOrder.verify(recordWriter1).close();
        inOrder.verify(recordWriter2).close();
    }
}