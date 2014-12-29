package org.easybatch.core.api.event.record;

public interface RecordProcessorEventListener extends RecordProcessEventListener {

    public void beforeProcessRecord(Object record);

    public void afterRecordProcessed(Object record, Object processResult);
}
