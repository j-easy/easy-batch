package org.easybatch.core.api.event.record;

import org.easybatch.core.api.Record;

public interface RecordReaderEventListener extends RecordProcessEventListener {

    public void beforeReaderOpen();

    public void afterReaderOpen();

    public void beforeRecordRead();

    public void afterRecordRead(Record record);
}
