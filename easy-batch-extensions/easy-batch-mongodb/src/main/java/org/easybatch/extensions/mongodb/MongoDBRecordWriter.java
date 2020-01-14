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
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.RecordWriter;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Writes a Mongo {@link DBObject} in a given collection.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MongoDBRecordWriter implements RecordWriter {

    private DBCollection collection;

    /**
     * Create a new {@link MongoDBRecordWriter}.
     *
     * @param collection to write documents to.
     */
    public MongoDBRecordWriter(final DBCollection collection) {
        checkNotNull(collection, "collection");
        this.collection = collection;
    }

    @Override
    public void open() throws Exception {
        // no op
    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        BulkWriteOperation bulkWriteOperation = collection.initializeOrderedBulkOperation();
        for (Record record : batch) {
            bulkWriteOperation.insert((DBObject) record.getPayload());
        }
        bulkWriteOperation.execute();
    }

    @Override
    public void close() throws Exception {
        // no op
    }
}
