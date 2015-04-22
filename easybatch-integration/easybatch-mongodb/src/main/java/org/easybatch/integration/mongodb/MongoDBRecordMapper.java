package org.easybatch.integration.mongodb;

import com.mongodb.DBObject;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordMapper;
import org.mongodb.morphia.Morphia;

/**
 * A mapper that maps Mongo {@link com.mongodb.DBObject} to domain objects using
 * <a href="https://github.com/mongodb/morphia">Morphia</a>.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class MongoDBRecordMapper<T> implements RecordMapper<T> {

    private Morphia morphia;

    private Class<T> type;

    public MongoDBRecordMapper(Class<T> type) {
        this.type = type;
        this.morphia = new Morphia();
        this.morphia.map(type);
    }

    @Override
    public T mapRecord(Record record) throws Exception {
        MongoDBRecord mongoDBRecord = (MongoDBRecord) record;
        DBObject dbObject = mongoDBRecord.getPayload();
        return morphia.fromDBObject(type, dbObject);
    }

}
