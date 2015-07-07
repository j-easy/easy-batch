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

package org.easybatch.xml;

import org.easybatch.core.api.Header;
import org.easybatch.core.api.RecordProcessingException;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class XmlRecordFlattenerTest {

    @Mock
    private Header header;

    @Mock
    private XmlRecord xmlRecord;

    private XmlRecordFlattener xmlRecordFlattener;

    @Before
    public void setUp() {
        when(xmlRecord.getHeader()).thenReturn(header);
        xmlRecordFlattener = new XmlRecordFlattener();
    }

    @Test
    public void testFlattenXmlRecord() throws RecordProcessingException {
        String expectedPayload = "<foo><bar><baz name='baz'/></bar></foo>";
        String payload = "<foo>\n<bar>\n<baz name='baz'/>\n</bar>\n</foo>";
        when(xmlRecord.getPayload()).thenReturn(payload);

        StringRecord flattenedRecord = xmlRecordFlattener.processRecord(xmlRecord);
        assertThat(flattenedRecord).isNotNull();
        assertThat(flattenedRecord.getHeader()).isEqualTo(header);
        assertThat(flattenedRecord.getPayload()).isEqualTo(expectedPayload);
    }
    
}