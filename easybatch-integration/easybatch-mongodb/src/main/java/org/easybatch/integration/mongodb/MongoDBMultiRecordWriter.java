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

import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.easybatch.core.api.RecordProcessingException;
import org.easybatch.core.writer.AbstractRecordWriter;

import java.util.List;

import static java.lang.String.format;

/**
 * Writes a list of Mongo {@link DBObject} to a given collection in bulk mode.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class MongoDBMultiRecordWriter extends AbstractRecordWriter<List<DBObject>> {

    private DBCollection collection;

    /**
     * Create a MongoDB mutli-record writer.
     *
     * @param collection the collection to write documents to.
     */
    public MongoDBMultiRecordWriter(final DBCollection collection) {
        this.collection = collection;
    }

    @Override
    protected void writeRecord(List<DBObject> documents) throws RecordProcessingException {
        BulkWriteOperation bulkWriteOperation = collection.initializeOrderedBulkOperation();

        for (DBObject document : documents) {
            bulkWriteOperation.insert(document);
        }

        try {
            bulkWriteOperation.execute();
        } catch (Exception e) {
            throw new RecordProcessingException(format("Unable to write documents [%s] to Mongo DB server", documents), e);
        }
    }

}
