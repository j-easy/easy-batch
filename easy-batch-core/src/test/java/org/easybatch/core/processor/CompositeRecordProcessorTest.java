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
package org.easybatch.core.processor;

import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
