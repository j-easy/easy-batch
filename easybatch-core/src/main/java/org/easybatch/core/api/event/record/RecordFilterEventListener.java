package org.easybatch.core.api.event.record;

import org.easybatch.core.api.Record;

public interface RecordFilterEventListener extends RecordProcessEventListener {
    public void beforeFilterRecord(Record record);
    public void afterFilterRecord(Record record, boolean filterRecord);
}
