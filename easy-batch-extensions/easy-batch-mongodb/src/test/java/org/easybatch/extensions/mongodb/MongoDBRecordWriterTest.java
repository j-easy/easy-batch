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
package org.easybatch.extensions.mongodb;

import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.easybatch.core.record.Batch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBRecordWriterTest {

    @Mock
    private DBCollection collection;
    @Mock
    private DBObject dbObject;
    @Mock
    private MongoDBRecord mongoDBRecord;
    @Mock
    private RuntimeException exception;
    @Mock
    private BulkWriteOperation bulkWriteOperation;

    private MongoDBRecordWriter mongoDBRecordWriter;

    @Before
    public void setUp() throws Exception {
        when(mongoDBRecord.getPayload()).thenReturn(dbObject);
        when(collection.initializeOrderedBulkOperation()).thenReturn(bulkWriteOperation);
        mongoDBRecordWriter = new MongoDBRecordWriter(collection);
    }

    @Test
    public void testWriteRecords() throws Exception {
        mongoDBRecordWriter.writeRecords(new Batch(mongoDBRecord));

        verify(bulkWriteOperation).insert(dbObject);
    }

    @Test(expected = Exception.class)
    public void testRecordWritingWithError() throws Exception {
        when(bulkWriteOperation.execute()).thenThrow(exception);

        mongoDBRecordWriter.writeRecords(new Batch(mongoDBRecord));
    }
}
