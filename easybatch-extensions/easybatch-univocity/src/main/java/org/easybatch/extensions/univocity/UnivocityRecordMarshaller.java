package org.easybatch.extensions.univocity;

import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

/**
 * Created by anthony on 4/12/16.
 */
public class UnivocityRecordMarshaller<P> implements RecordMarshaller<Record<P>, StringRecord> {

    public UnivocityRecordMarshaller(Class<P> recordClass) {
    }

    @Override
    public StringRecord processRecord(Record<P> record) throws Exception {
        return null;
    }
}
