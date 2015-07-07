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

package org.easybatch.json;

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
public class JsonRecordFlattenerTest {

    @Mock
    private Header header;

    @Mock
    private JsonRecord jsonRecord;

    private String payload, expectedPayload;

    private JsonRecordFlattener jsonRecordFlattener;

    @Before
    public void setUp() {
        when(jsonRecord.getHeader()).thenReturn(header);
        jsonRecordFlattener = new JsonRecordFlattener();
    }
    
    @Test
    public void testFlattenSimpleJsonRecord() throws RecordProcessingException {
        expectedPayload = "{\"name\":\"foo\",\"age\":30,\"married\":false}";
        payload = "{\n  \"name\": \"foo\",\n  \"age\": 30,\n  \"married\": false\n}";
        when(jsonRecord.getPayload()).thenReturn(payload);

        StringRecord flattenedRecord = jsonRecordFlattener.processRecord(jsonRecord);
        assertThat(flattenedRecord).isNotNull();
        assertThat(flattenedRecord.getHeader()).isEqualTo(header);
        assertThat(flattenedRecord.getPayload()).isEqualTo(expectedPayload);
    }    
    
    @Test
    public void testFlattenComplexJsonRecord() throws RecordProcessingException {
        expectedPayload = "{\"datasetid\":\"arbresalignementparis2010\",\"recordid\":\"0e6cfe03082224225c54690e5a700987ffb1310f\",\"fields\":{\"adresse\":\"AVENUE GAMBETTA\",\"hauteurenm\":0.0,\"espece\":\"Tilia tomentosa\",\"circonfere\":78.0,\"geom_x_y\":[48.8691944661,2.40210336054],\"dateplanta\":\"1971-02-27\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[2.40210336054,48.8691944661]},\"record_timestamp\":\"2014-09-11T14:39:16.131843\"}";
        payload = "{\n  \"datasetid\": \"arbresalignementparis2010\",\n  \"recordid\": \"0e6cfe03082224225c54690e5a700987ffb1310f\",\n  \"fields\": {\n    \"adresse\": \"AVENUE GAMBETTA\",\n    \"hauteurenm\": 0.0,\n    \"espece\": \"Tilia tomentosa\",\n    \"circonfere\": 78.0,\n    \"geom_x_y\": [\n      48.8691944661,\n      2.40210336054\n    ],\n    \"dateplanta\": \"1971-02-27\"\n  },\n  \"geometry\": {\n    \"type\": \"Point\",\n    \"coordinates\": [\n      2.40210336054,\n      48.8691944661\n    ]\n  },\n  \"record_timestamp\": \"2014-09-11T14:39:16.131843\"\n}";
        when(jsonRecord.getPayload()).thenReturn(payload);

        StringRecord flattenedRecord = jsonRecordFlattener.processRecord(jsonRecord);
        assertThat(flattenedRecord).isNotNull();
        assertThat(flattenedRecord.getHeader()).isEqualTo(header);
        assertThat(flattenedRecord.getPayload()).isEqualTo(expectedPayload);
    }
}