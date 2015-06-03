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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.easybatch.core.api.Header;
import org.easybatch.core.api.RecordReader;

import java.util.Date;

/**
 * Reader that reads documents from a MongoDB collection.
 * <p/>
 * This reader produces {@link MongoDBRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class MongoDBRecordReader implements RecordReader {

    private DBCollection collection;

    private DBObject query;

    private DBCursor cursor;

    private boolean limit;
    private int nbLimit;

    private boolean skip;
    private int nbSkip;

    private boolean sort;
    private DBObject orderBy;

    private long currentRecordNumber;

    public MongoDBRecordReader(DBCollection collection, DBObject query) {
        this.collection = collection;
        this.query = query;
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        cursor = collection.find(query);
        if (limit) {
            cursor.limit(nbLimit);
        }
        if (skip) {
            cursor.skip(nbSkip);
        }
        if (sort) {
            cursor.sort(orderBy);
        }
    }

    @Override
    public boolean hasNextRecord() {
        return cursor.hasNext();
    }

    @Override
    public MongoDBRecord readNextRecord() throws Exception {
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
    public void close() throws Exception {
        cursor.close();
    }

    /**
     * Set cursor limit.
     *
     * @param limit the number of documents limit
     */
    public void setLimit(int limit) {
        this.nbLimit = limit;
        this.limit = true;
    }

    /**
     * Set the number of documents to skip.
     *
     * @param skip the number of documents to skip.
     */
    public void setSkip(int skip) {
        this.nbSkip = skip;
        this.skip = true;
    }

    /**
     * Set the sort criteria.
     *
     * @param orderBy the sort criteria
     */
    public void setSort(DBObject orderBy) {
        this.orderBy = orderBy;
        this.sort = true;
    }

}
