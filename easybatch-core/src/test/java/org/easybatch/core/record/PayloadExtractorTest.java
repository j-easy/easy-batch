/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core.record;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PayloadExtractorTest {

    @Mock
    private Object payload1, payload2;
    @Mock
    private Record<Object> record1, record2;
    @Mock
    private Batch batch;

    @Before
    public void setUp() throws Exception {
        when(record1.getPayload()).thenReturn(payload1);
        when(record2.getPayload()).thenReturn(payload2);
        when(batch.getPayload()).thenReturn(Arrays.<Record>asList(record1, record2));
    }

    @Test
    public void testExtractPayloadsFromListOfRecords() throws Exception {
        List<Object> list = PayloadExtractor.extractPayloads(Arrays.asList(record1, record2));
        assertThat(list).containsExactly(payload1, payload2);
    }

    @Test
    public void testExtractPayloadsFromBatchOfRecords() throws Exception {
        List<Object> list = PayloadExtractor.extractPayloads(batch);
        assertThat(list).containsExactly(payload1, payload2);
    }
}
