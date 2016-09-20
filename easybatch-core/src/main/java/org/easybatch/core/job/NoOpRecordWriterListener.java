package org.easybatch.core.job;

import org.easybatch.core.listener.RecordWriterListener;
import org.easybatch.core.record.Record;

class NoOpRecordWriterListener implements RecordWriterListener {

    @Override
    public void beforeRecordWriting(Record record) {

    }

    @Override
    public void afterRecordWriting(Record record) {

    }

    @Override
    public void onRecordWritingException(Record record, Throwable throwable) {

    }
}
