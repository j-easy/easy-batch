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

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.easybatch.core.api.RecordWritingException;
import org.easybatch.core.writer.AbstractRecordWriter;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Writes a Mongo {@link DBObject} in a given collection.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class MongoDBRecordWriter extends AbstractRecordWriter<DBObject> {

    private DBCollection collection;

    /**
     * Create a MongoDB writer.
     *
     * @param collection the collection to write documents to.
     */
    public MongoDBRecordWriter(final DBCollection collection) {
        checkNotNull(collection, "collection");
        this.collection = collection;
    }

    @Override
    public void writeRecord(final DBObject record) throws RecordWritingException {
        try {
            collection.save(record);
        } catch (Exception e) {
            throw new RecordWritingException("Unable to write document " + record + " to MongoDB server", e);
        }
    }

}
