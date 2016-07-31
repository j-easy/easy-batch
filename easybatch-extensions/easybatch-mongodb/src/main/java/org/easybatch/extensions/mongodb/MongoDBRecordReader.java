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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Header;

import java.util.Date;

import static org.easybatch.core.util.Utils.checkArgument;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Reader that reads documents from a MongoDB collection.
 * <p/>
 * This reader produces {@link MongoDBRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MongoDBRecordReader implements RecordReader {

    private DBCollection collection;

    private DBObject query;

    private DBCursor cursor;

    private int nbLimit;

    private int nbSkip;

    private DBObject orderBy;

    private long currentRecordNumber;

    /**
     * Reader that reads documents from a MongoDB collection.
     * <p/>
     * This reader produces {@link MongoDBRecord} instances.
     *
     * @param collection the collection to read documents from
     * @param query      the query to fetch data
     */
    public MongoDBRecordReader(final DBCollection collection, final DBObject query) {
        this.collection = collection;
        this.query = query;
    }

    @Override
    public void open() {
        currentRecordNumber = 0;
        cursor = collection.find(query);
        if (nbLimit >= 1) {
            cursor.limit(nbLimit);
        }
        if (nbSkip >= 1) {
            cursor.skip(nbSkip);
        }
        if (orderBy != null) {
            cursor.sort(orderBy);
        }
    }

    @Override
    public boolean hasNextRecord() {
        return cursor.hasNext();
    }

    @Override
    public MongoDBRecord readNextRecord() {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new MongoDBRecord(header, cursor.next());
    }

    @Override
    public Long getTotalRecords() {
        return (long) cursor.count();
    }

    @Override
    public String getDataSourceName() {
        return "MongoDB collection: " + collection.getName();
    }

    @Override
    public void close() {
        cursor.close();
    }

    /**
     * Set cursor limit.
     *
     * @param limit the number of documents limit
     */
    public void setLimit(final int limit) {
        checkArgument(limit >= 1, "limit parameter should be greater than or equal to 1");
        this.nbLimit = limit;
    }

    /**
     * Set the number of documents to skip.
     *
     * @param skip the number of documents to skip.
     */
    public void setSkip(final int skip) {
        checkArgument(skip >= 1, "skip parameter should be greater than or equal to 1");
        this.nbSkip = skip;
    }

    /**
     * Set the sort criteria.
     *
     * @param orderBy the sort criteria
     */
    public void setSort(final DBObject orderBy) {
        checkNotNull(orderBy, "orderBy");
        this.orderBy = orderBy;
    }

}
