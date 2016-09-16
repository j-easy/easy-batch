package org.easybatch.core.job;

import org.easybatch.core.listener.RecordReaderListener;
import org.easybatch.core.record.Record;

class NoOpRecordReaderListener implements RecordReaderListener {

    @Override
    public void beforeRecordReading() {

    }

    @Override
    public void afterRecordReading(Record record) {

    }

    @Override
    public void onRecordReadingException(Throwable throwable) {

    }
}
