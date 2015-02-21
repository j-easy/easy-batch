package org.easybatch.integration.mongodb;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.easybatch.core.api.Header;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordReader;

import java.util.Date;

/**
 * Reader that reads documents from a MongoDB collection.
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
        if(limit) {
            cursor.limit(nbLimit);
        }
        if(skip) {
            cursor.skip(nbSkip);
        }
        if(sort) {
            cursor.sort(orderBy);
        }
    }

    @Override
    public boolean hasNextRecord() {
        return cursor.hasNext();
    }

    @Override
    public Record readNextRecord() throws Exception {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new MongoRecord(header, cursor.next());
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
     * @param limit the number of documents limit
     */
    public void setLimit(int limit) {
        this.nbLimit = limit;
        this.limit = true;
    }

    /**
     * Set the number of documents to skip.
     * @param skip the number of documents to skip.
     */
    public void setSkip(int skip) {
        this.nbSkip = skip;
        this.skip = true;
    }

    /**
     * Set the sort criteria.
     * @param orderBy the sort criteria
     */
    public void setSort(DBObject orderBy) {
        this.orderBy = orderBy;
        this.sort = true;
    }

}
