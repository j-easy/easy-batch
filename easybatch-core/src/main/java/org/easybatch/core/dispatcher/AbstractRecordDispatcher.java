package org.easybatch.core.dispatcher;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordProcessor;

/**
 * Base class for record dispatchers.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public abstract class AbstractRecordDispatcher implements RecordProcessor<Record, Record> {

    protected abstract void dispatchRecord(Record record) throws Exception;

    @Override
    public Record processRecord(Record record) throws Exception {
        dispatchRecord(record);
        return record;
    }

}
