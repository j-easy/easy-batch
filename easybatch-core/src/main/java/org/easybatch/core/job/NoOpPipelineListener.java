package org.easybatch.core.job;

import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.record.Record;

class NoOpPipelineListener implements PipelineListener {

    @Override
    public Record beforeRecordProcessing(Record record) {
        return record;
    }

    @Override
    public void afterRecordProcessing(Record inputRecord, Record outputRecord) {

    }

    @Override
    public void onRecordProcessingException(Record record, Throwable throwable) {

    }
}
