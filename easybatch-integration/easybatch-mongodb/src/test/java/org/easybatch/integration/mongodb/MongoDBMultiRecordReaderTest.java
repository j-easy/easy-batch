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

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.easybatch.core.api.Engine;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.Report;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.MultiRecord;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.EngineBuilder.aNewEngine;

/**
 * Integration test for {@link MongoDBMultiRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@Ignore("Ignored since it's impossible to embed a MongoDB instance ..")
@SuppressWarnings("unchecked")
public class MongoDBMultiRecordReaderTest {

    private static final int CHUNK_SIZE = 2;

    private MongoDBMultiRecordReader mongoDBMultiRecordReader;

    @Before
    public void setUp() throws Exception {
        MongoClient mongoClient = new MongoClient();
        DBCollection collection = mongoClient.getDB("test").getCollection("tweets");
        DBObject query = new BasicDBObject();

        mongoDBMultiRecordReader = new MongoDBMultiRecordReader(collection, query, CHUNK_SIZE);
        //db.tweets.insert({name:'foo'})
        //db.tweets.insert({name:'bar'})
        //db.tweets.insert({name:'baz'})
    }

    @Test
    public void testChunkProcessing() throws Exception {

        Engine engine = aNewEngine()
                .reader(mongoDBMultiRecordReader)
                .processor(new RecordCollector<MultiRecord>())
                .build();

        Report report = engine.call();

        List<MultiRecord> multiRecords = (List<MultiRecord>) report.getJobResult();

        assertThat(multiRecords).isNotNull().hasSize(2);

        MultiRecord multiRecord = multiRecords.get(0);
        List<Record> payload = multiRecord.getPayload();
        assertThat(payload).hasSize(2);

        MongoDBRecord mongoDBRecord = (MongoDBRecord) payload.get(0);
        assertThat(mongoDBRecord.getPayload().get("name")).isEqualTo("foo");

        mongoDBRecord = (MongoDBRecord) payload.get(1);
        assertThat(mongoDBRecord.getPayload().get("name")).isEqualTo("bar");

        multiRecord = multiRecords.get(1);
        payload = multiRecord.getPayload();
        assertThat(payload).hasSize(1);

        mongoDBRecord = (MongoDBRecord) payload.get(0);
        assertThat(mongoDBRecord.getPayload().get("name")).isEqualTo("baz");
    }
}