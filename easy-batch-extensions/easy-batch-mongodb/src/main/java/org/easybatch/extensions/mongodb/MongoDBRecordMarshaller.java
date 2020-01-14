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

import com.mongodb.DBObject;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.record.Record;
import org.mongodb.morphia.Morphia;

/**
 * A marshaller that marshals domain objects to Mongo {@link DBObject} using
 * <a href="https://github.com/mongodb/morphia">Morphia</a>.
 *
 * @param <T> The source object type.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MongoDBRecordMarshaller<T> implements RecordMapper<Record<T>, MongoDBRecord> {

    private Morphia morphia;

    /**
     * Create a new {@link MongoDBRecordMarshaller}.
     *
     * @param type the source object type
     */
    public MongoDBRecordMarshaller(final Class<T> type) {
        this.morphia = new Morphia();
        this.morphia.map(type);
    }

    @Override
    public MongoDBRecord processRecord(final Record<T> record) {
        return new MongoDBRecord(record.getHeader(), morphia.toDBObject(record.getPayload()));
    }

}
