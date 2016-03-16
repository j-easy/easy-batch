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

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.easybatch.core.writer.AbstractRecordWriter;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Writes a Mongo {@link DBObject} in a given collection.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MongoDBRecordWriter extends AbstractRecordWriter<DBObject, MongoDBRecord> {

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
    protected void writePayload(final DBObject record) throws Exception {
        collection.save(record);
    }

}
