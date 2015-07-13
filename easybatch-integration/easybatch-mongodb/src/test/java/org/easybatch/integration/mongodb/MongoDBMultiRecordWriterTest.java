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

package org.easybatch.integration.mongodb;

import com.mongodb.*;
import org.assertj.core.api.Assertions;
import org.easybatch.core.api.*;
import org.easybatch.core.reader.IterableMultiRecordReader;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.MultiRecord;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.EngineBuilder.aNewEngine;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link MongoDBMultiRecordWriter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class MongoDBMultiRecordWriterTest {

    @Mock
    private DBCollection collection;

    @Mock
    private DBObject dbObject, anotherDbObject;

    @Mock
    private BulkWriteOperation bulkWriteOperation;

    @Mock
    private RuntimeException exception;

    MongoDBMultiRecordWriter mongoDBMultiRecordWriter;

    @Before
    public void setUp() throws Exception {
        mongoDBMultiRecordWriter = new MongoDBMultiRecordWriter(collection);
        when(collection.initializeOrderedBulkOperation()).thenReturn(bulkWriteOperation);
    }

    @Test
    public void testMultiRecordWriting() throws Exception {
        mongoDBMultiRecordWriter.processRecord(asList(dbObject, anotherDbObject));

        InOrder inOrder = Mockito.inOrder(bulkWriteOperation, dbObject, anotherDbObject);
        inOrder.verify(bulkWriteOperation).insert(dbObject);
        inOrder.verify(bulkWriteOperation).insert(anotherDbObject);
        inOrder.verify(bulkWriteOperation).execute();
    }

    @Test(expected = RecordProcessingException.class)
    public void testMultiRecordWritingWithError() throws Exception {
        when(bulkWriteOperation.execute()).thenThrow(exception);

        mongoDBMultiRecordWriter.processRecord(asList(dbObject, anotherDbObject));
    }

    @Test
    @Ignore("Ignored since it's impossible to embed a MongoDB instance ..")
    public void multiRecordWritingIntegrationTest() throws Exception {

        MongoClient mongoClient = new MongoClient();
        DBCollection collection = mongoClient.getDB("test").getCollection("tweets");
        collection.remove(new BasicDBObject());

        DBObject tweet1 = new BasicDBObject("_id", 1)
                .append("user", "foo")
                .append("message", "hello");

        DBObject tweet2 = new BasicDBObject("_id", 2)
                .append("user", "bar")
                .append("message", "hey");

        DBObject tweet3 = new BasicDBObject("_id", 3)
                .append("user", "toto")
                .append("message", "hi");

        List<DBObject> tweets = Arrays.asList(tweet1, tweet2, tweet3);

        int chunkSize = 2;

        Report report = aNewEngine()
                .reader(new IterableMultiRecordReader<DBObject>(chunkSize, tweets))
                .mapper(new MultiRecordToDBObjectListMapper())
                .writer(new MongoDBMultiRecordWriter(collection))
                .build().call();

        assertThat(report).isNotNull();
        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(2);

        assertThat(collection.count()).isEqualTo(3);

        DBCursor cursor = collection.find();
        DBObject tweet = cursor.next();
        Assertions.assertThat(tweet.get("_id")).isEqualTo(1);
        Assertions.assertThat(tweet.get("user")).isEqualTo("foo");
        Assertions.assertThat(tweet.get("message")).isEqualTo("hello");

        tweet = cursor.next();
        Assertions.assertThat(tweet.get("_id")).isEqualTo(2);
        Assertions.assertThat(tweet.get("user")).isEqualTo("bar");
        Assertions.assertThat(tweet.get("message")).isEqualTo("hey");

        tweet = cursor.next();
        Assertions.assertThat(tweet.get("_id")).isEqualTo(3);
        Assertions.assertThat(tweet.get("user")).isEqualTo("toto");
        Assertions.assertThat(tweet.get("message")).isEqualTo("hi");

        mongoClient.close();
    }

    private class MultiRecordToDBObjectListMapper implements RecordMapper<List<DBObject>> {

        //TODO Should be type safe and built-in in core module
        @Override
        public List<DBObject> mapRecord(Record record) throws RecordMappingException {
            MultiRecord multiRecord = (MultiRecord) record;
            List<Record> records = multiRecord.getPayload();
            List<DBObject> dbObjects = new ArrayList<DBObject>();
            for (Record r : records) {
                dbObjects.add(((GenericRecord<DBObject>) r).getPayload());
            }
            return dbObjects;
        }
    }
}