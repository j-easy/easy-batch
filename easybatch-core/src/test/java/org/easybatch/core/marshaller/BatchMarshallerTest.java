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

package org.easybatch.core.marshaller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BatchMarshallerTest {

    @Mock
    private Object record1, record2, marshalledRecord1, marshalledRecord2;
    
    @Mock
    private RecordMarshaller<Object, Object> recordMarshaller;
    
    private BatchMarshaller batchMarshaller;
    
    @Before
    public void setUp() throws Exception {
        when(recordMarshaller.processRecord(record1)).thenReturn(marshalledRecord1);
        when(recordMarshaller.processRecord(record2)).thenReturn(marshalledRecord2);
        batchMarshaller = new BatchMarshaller(recordMarshaller);
    }

    @Test
    public void processRecord() throws RecordMarshallingException {
        List<Object> strings = batchMarshaller.processRecord(Arrays.asList(record1, record2));
        
        assertThat(strings)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(marshalledRecord1, marshalledRecord2);
    }
}
