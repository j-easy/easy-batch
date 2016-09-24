package org.easybatch.core.job;

import org.easybatch.core.listener.RecordWriterListener;
import org.easybatch.core.record.Batch;

class NoOpRecordWriterListener implements RecordWriterListener {

    @Override
    public void beforeRecordWriting(Batch batch) {

    }

    @Override
    public void afterRecordWriting(Batch batch) {

    }

    @Override
    public void onRecordWritingException(Batch batch, Throwable throwable) {

    }
}
