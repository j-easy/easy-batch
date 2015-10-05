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

package org.easybatch.core.mapper;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.Report;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.reader.IterableMultiRecordReader;
import org.easybatch.core.record.MultiRecord;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.EngineBuilder.aNewEngine;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GenericMultiRecordMapperTest {

    @Mock
    private MultiRecord multiRecord;

    @Mock
    private StringRecord record1, record2;

    private GenericMultiRecordMapper<String> multiRecordMapper;

    @Before
    public void setUp() throws Exception {
        multiRecordMapper = new GenericMultiRecordMapper<String>();
        when(record1.getPayload()).thenReturn("foo");
        when(record2.getPayload()).thenReturn("bar");
        when(multiRecord.getPayload()).thenReturn(Arrays.<Record>asList(record1, record2));
    }

    @Test
    public void testMapRecord() throws Exception {
        assertThat(multiRecordMapper.processRecord(multiRecord)).containsExactly("foo", "bar");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void genericMultiRecordMapperIntegrationTest() throws Exception {

        List<String> dataSource = Arrays.asList("foo", "bar", "toto", "titi", "baz");

        Report report = aNewEngine()
                .reader(new IterableMultiRecordReader<String>(dataSource, 2))
                .mapper(new GenericMultiRecordMapper())
                .processor(new RecordCollector())
                .build().call();

        List<List<String>> result = (List<List<String>>) report.getJobResult();
        assertThat(result).hasSize(3);

        assertThat(result.get(0)).containsExactly("foo", "bar");
        assertThat(result.get(1)).containsExactly("toto", "titi");
        assertThat(result.get(2)).containsExactly("baz");

    }
}