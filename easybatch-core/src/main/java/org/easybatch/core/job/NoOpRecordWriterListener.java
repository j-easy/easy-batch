package org.easybatch.core.job;

import org.easybatch.core.listener.RecordWriterListener;
import org.easybatch.core.record.Record;

import java.util.List;

class NoOpRecordWriterListener implements RecordWriterListener {

    @Override
    public void beforeRecordWriting(List<Record> batch) {

    }

    @Override
    public void afterRecordWriting(List<Record> batch) {

    }

    @Override
    public void onRecordWritingException(List<Record> batch, Throwable throwable) {

    }
}
