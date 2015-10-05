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

package org.easybatch.jms;

import org.easybatch.core.api.RecordFilteringException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.Message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link JmsPoisonRecordFilter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class JmsPoisonRecordFilterTest {

    private JmsPoisonRecordFilter jmsPoisonRecordFilter;

    @Mock
    private Message payload;
    @Mock
    private JmsRecord jmsRecord;
    @Mock
    private JmsPoisonMessage jmsPoisonMessage;
    @Mock
    private Message nonJmsPoisonMessage;

    @Before
    public void setUp() throws Exception {
        jmsPoisonRecordFilter = new JmsPoisonRecordFilter();
        when(jmsRecord.getPayload()).thenReturn(payload);
    }

    @Test(expected = RecordFilteringException.class)
    public void testFilterPoisonRecordByType() throws Exception {
        when(payload.getJMSType()).thenReturn(JmsPoisonMessage.TYPE);
        jmsPoisonRecordFilter.processRecord(jmsRecord);
    }

    @Test(expected = RecordFilteringException.class)
    public void testFilterPoisonRecordByPayload() throws Exception {
        when(jmsRecord.getPayload()).thenReturn(jmsPoisonMessage);
        jmsPoisonRecordFilter.processRecord(jmsRecord);
    }

    @Test
    public void testFilterNonPoisonRecord() throws Exception {
        when(payload.getJMSType()).thenReturn("foo");
        assertThat(jmsPoisonRecordFilter.processRecord(jmsRecord)).isEqualTo(jmsRecord);

        when(jmsRecord.getPayload()).thenReturn(nonJmsPoisonMessage);
        assertThat(jmsPoisonRecordFilter.processRecord(jmsRecord)).isEqualTo(jmsRecord);
    }
}