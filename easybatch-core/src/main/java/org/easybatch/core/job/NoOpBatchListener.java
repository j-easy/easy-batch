package org.easybatch.core.job;

import org.easybatch.core.listener.BatchListener;
import org.easybatch.core.record.Record;

import java.util.List;

class NoOpBatchListener implements BatchListener {

    @Override
    public void beforeBatch() {

    }

    @Override
    public void afterBatch(List<Record> records) {

    }
}
