package org.easybatch.core.job;

import org.easybatch.core.listener.BatchListener;
import org.easybatch.core.record.Batch;

class NoOpBatchListener implements BatchListener {


    @Override
    public void beforeBatchReading() {

    }

    @Override
    public void afterBatchProcessing(final Batch batch) {

    }

    @Override
    public void afterBatchWriting(final Batch batch) {

    }

    @Override
    public void onBatchWritingException(final Batch batch, final Throwable throwable) {

    }
}
