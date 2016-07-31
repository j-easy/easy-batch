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

package org.easybatch.extensions.mongodb;

import com.mongodb.*;
import org.assertj.core.api.Assertions;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.mapper.BatchMapper;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.mapper.RecordMappingException;
import org.easybatch.core.processor.RecordProcessingException;
import org.easybatch.core.reader.IterableBatchReader;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBBatchWriterTest {

    @Mock
    private Batch batch;
    @Mock
    private Header header;
    @Mock
    private DBCollection collection;
    @Mock
    private MongoDBRecord record1, record2;
    @Mock
    private DBObject dbObject1, dbObject2;
    @Mock
    private BulkWriteOperation bulkWriteOperation;
    @Mock
    private RuntimeException exception;

    private MongoDBBatchWriter mongoDBBatchWriter;

    @Before
    public void setUp() throws Exception {
        when(record1.getPayload()).thenReturn(dbObject1);
        when(record2.getPayload()).thenReturn(dbObject2);
        when(batch.getHeader()).thenReturn(header);
        when(batch.getPayload()).thenReturn(Arrays.<Record>asList(record1, record2));

        mongoDBBatchWriter = new MongoDBBatchWriter(collection);
        when(collection.initializeOrderedBulkOperation()).thenReturn(bulkWriteOperation);
    }

    @Test
    public void testBatchWriting() throws Exception {
        Batch actual = mongoDBBatchWriter.processRecord(this.batch);

        InOrder inOrder = inOrder(bulkWriteOperation, dbObject1, dbObject2);
        inOrder.verify(bulkWriteOperation).insert(dbObject1);
        inOrder.verify(bulkWriteOperation).insert(dbObject2);
        inOrder.verify(bulkWriteOperation).execute();

        assertThat(actual.getHeader()).isEqualTo(header);
    }

    @Test(expected = RecordProcessingException.class)
    public void testBatchWritingWithError() throws Exception {
        when(bulkWriteOperation.execute()).thenThrow(exception);

        mongoDBBatchWriter.processRecord(batch);
    }

    @Test
    @Ignore("Ignored since it's impossible to embed a MongoDB instance ..")
    //db.tweets.remove({})
    public void batchWritingIntegrationTest() throws Exception {

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

        int batchSize = 2;

        JobReport jobReport = aNewJob()
                .reader(new IterableBatchReader(tweets, batchSize))
                .mapper(new BatchMapper(new RecordMapper<GenericRecord<DBObject>, MongoDBRecord>() {
                    @Override
                    public MongoDBRecord processRecord(GenericRecord<DBObject> record) throws RecordMappingException {
                        return new MongoDBRecord(record.getHeader(), record.getPayload());
                    }
                }))
                .writer(new MongoDBBatchWriter(collection))
                .call();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(2);

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
}
