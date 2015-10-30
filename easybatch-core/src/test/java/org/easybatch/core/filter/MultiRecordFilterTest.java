/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.core.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.easybatch.core.record.MultiRecord;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MultiRecordFilterTest {

    @Mock
    private MultiRecord multiRecord;
    @Mock
    private Record record1, record2;
    @Mock
    private RecordFilter recordFilter;
    
    private MultiRecordFilter multiRecordFilter;
    
    @Before
    public void setUp() {
        multiRecordFilter = new MultiRecordFilter(recordFilter);
        ArrayList<Record> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        when(multiRecord.getPayload()).thenReturn(records);
        when(recordFilter.processRecord(record1)).thenReturn(record1);
        when(recordFilter.processRecord(record2)).thenReturn(null);

    }

    @Test
    public void name() {
        multiRecordFilter.processRecord(multiRecord);
        
        assertThat(multiRecord).isNotNull();
        assertThat(multiRecord.getPayload()).isNotNull().isNotEmpty().hasSize(1).containsOnly(record1);
    }
    
}
