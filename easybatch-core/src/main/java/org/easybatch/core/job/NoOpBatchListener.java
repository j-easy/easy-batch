package org.easybatch.core.job;

import org.easybatch.core.listener.BatchListener;
import org.easybatch.core.record.Record;

import java.util.List;

class NoOpBatchListener implements BatchListener {


    @Override
    public void beforeBatchReading() {

    }

    @Override
    public void afterBatchProcessing(List<Record> records) {

    }

    @Override
    public void afterBatchWriting(List<Record> records) {

    }

    @Override
    public void onBatchWritingException(List<Record> records, Throwable throwable) {

    }
}
