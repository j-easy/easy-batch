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

package org.easybatch.extensions.mongodb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.easybatch.core.processor.RecordProcessingException;
import org.easybatch.core.record.Header;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBRecordWriterTest {

    @Mock
    private DBCollection collection;
    @Mock
    private DBObject dbObject;
    @Mock
    private Header header;
    @Mock
    private MongoDBRecord mongoDBRecord;
    @Mock
    private RuntimeException exception;

    private MongoDBRecordWriter mongoDBRecordWriter;

    @Before
    public void setUp() throws Exception {
        when(mongoDBRecord.getHeader()).thenReturn(header);
        when(mongoDBRecord.getPayload()).thenReturn(dbObject);
        mongoDBRecordWriter = new MongoDBRecordWriter(collection);
    }

    @Test
    public void testProcessRecord() throws Exception {
        MongoDBRecord actual = mongoDBRecordWriter.processRecord(this.mongoDBRecord);
        
        verify(collection).save(dbObject);
        assertThat(actual).isEqualTo(mongoDBRecord);
    }

    @Test(expected = RecordProcessingException.class)
    public void testRecordProcessingWithError() throws Exception {
        when(collection.save(dbObject)).thenThrow(exception);

        mongoDBRecordWriter.processRecord(mongoDBRecord);
    }
}
