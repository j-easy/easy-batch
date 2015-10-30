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

import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.easybatch.core.writer.AbstractBatchWriter;
import org.easybatch.core.writer.RecordWritingException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Writes a batch of Mongo {@link DBObject} to a given collection in bulk mode.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class MongoDBBatchWriter extends AbstractBatchWriter {

    private DBCollection collection;

    /**
     * Create a MongoDB batch writer.
     *
     * @param collection the collection to write documents to.
     */
    public MongoDBBatchWriter(final DBCollection collection) {
        checkNotNull(collection, "collection");
        this.collection = collection;
    }

    @Override
    protected void writeRecord(final Object batch) throws RecordWritingException {
        List records = getRecords(batch);
        Collection<DBObject> documents = asDocuments(records);
        BulkWriteOperation bulkWriteOperation = collection.initializeOrderedBulkOperation();

        for (DBObject document : documents) {
            bulkWriteOperation.insert(document);
        }

        try {
            bulkWriteOperation.execute();
        } catch (Exception e) {
            throw new RecordWritingException(format("Unable to write documents [%s] to Mongo DB server", documents), e);
        }
    }

    private List<DBObject> asDocuments(final List records) {
        List<DBObject> documents = new ArrayList<>();
        for (Object record : records) {
            if (DBObject.class.isAssignableFrom(record.getClass())) {
                documents.add((DBObject) record);
            }
        }
        return documents;
    }

}
