package org.easybatch.core.processor;

import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings(value = "unchecked")
public class CompositeRecordProcessorTest {

    @Mock
    private Record record, processedRecord;
    @Mock
    private RecordProcessor recordProcessor1, recordProcessor2;

    private CompositeRecordProcessor compositeRecordProcessor;

    @Before
    public void setUp() throws Exception {
        compositeRecordProcessor = new CompositeRecordProcessor(asList(recordProcessor1, recordProcessor2));
    }

    @Test
    public void recordsShouldBeProcessedByDelegates() throws Exception {
        //given
        when(recordProcessor1.processRecord(record)).thenReturn(processedRecord);
        when(recordProcessor2.processRecord(processedRecord)).thenReturn(processedRecord);

        //when
        Record actual = compositeRecordProcessor.processRecord(record);

        //then
        assertThat(actual).isEqualTo(processedRecord);
    }

    @Test
    public void whenProcessorReturnsNull_thenNextProcessorsShouldBeSkipped() throws Exception {
        //given
        when(recordProcessor1.processRecord(record)).thenReturn(null);

        //when
        Record actual = compositeRecordProcessor.processRecord(record);

        //then
        assertThat(actual).isNull();
    }
}