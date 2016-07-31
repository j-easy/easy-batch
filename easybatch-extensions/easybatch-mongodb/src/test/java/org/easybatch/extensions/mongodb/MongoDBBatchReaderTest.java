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

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;

@Ignore("Ignored since it's impossible to embed a MongoDB instance ..")
@SuppressWarnings("unchecked")
public class MongoDBBatchReaderTest {

    private static final int BATCH_SIZE = 2;

    private MongoDBBatchReader mongoDBBatchReader;

    @Before
    public void setUp() throws Exception {
        MongoClient mongoClient = new MongoClient();
        DBCollection collection = mongoClient.getDB("test").getCollection("tweets");
        DBObject query = new BasicDBObject();

        mongoDBBatchReader = new MongoDBBatchReader(collection, query, BATCH_SIZE);
        //db.tweets.remove({})
        //db.tweets.insert({name:'foo'})
        //db.tweets.insert({name:'bar'})
        //db.tweets.insert({name:'baz'})
    }

    @Test
    public void testBatchProcessing() throws Exception {

        Job job = aNewJob()
                .reader(mongoDBBatchReader)
                .processor(new RecordCollector())
                .build();

        JobReport jobReport = JobExecutor.execute(job);

        List<Batch> batches = (List<Batch>) jobReport.getResult();

        assertThat(batches).isNotNull().hasSize(2);

        Batch batch = batches.get(0);
        List<Record> payload = batch.getPayload();
        assertThat(payload).hasSize(2);

        MongoDBRecord mongoDBRecord = (MongoDBRecord) payload.get(0);
        assertThat(mongoDBRecord.getPayload().get("name")).isEqualTo("foo");

        mongoDBRecord = (MongoDBRecord) payload.get(1);
        assertThat(mongoDBRecord.getPayload().get("name")).isEqualTo("bar");

        batch = batches.get(1);
        payload = batch.getPayload();
        assertThat(payload).hasSize(1);

        mongoDBRecord = (MongoDBRecord) payload.get(0);
        assertThat(mongoDBRecord.getPayload().get("name")).isEqualTo("baz");
    }
}
