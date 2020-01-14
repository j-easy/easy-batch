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

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.easybatch.core.record.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBRecordReaderTest {

    private static final String DATA_SOURCE_NAME = "things";

    private MongoDBRecordReader reader;

    @Mock
    private DBCollection collection;
    @Mock
    private DBObject query;
    @Mock
    private DBCursor cursor;
    @Mock
    private DBObject dbObject;

    @Before
    public void setUp() throws Exception {
        reader = new MongoDBRecordReader(collection, query);

        when(collection.find(query)).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(true);
        when(cursor.next()).thenReturn(dbObject);
        when(collection.getName()).thenReturn(DATA_SOURCE_NAME);

        reader.open();
    }

    @Test
    public void testReadRecord() throws Exception {
        Record record = reader.readRecord();

        assertThat(record.getHeader().getNumber()).isEqualTo(1);
        assertThat(record.getHeader().getSource()).isEqualTo("MongoDB collection: " + DATA_SOURCE_NAME);
        assertThat(record.getPayload()).isEqualTo(dbObject);
        verify(cursor).next();
    }

    @After
    public void tearDown() throws Exception {
        reader.close();
    }
}
