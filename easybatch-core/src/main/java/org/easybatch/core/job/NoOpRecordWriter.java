package org.easybatch.core.job;

import org.easybatch.core.record.Batch;
import org.easybatch.core.writer.RecordWriter;

class NoOpRecordWriter implements RecordWriter {

    @Override
    public void open() throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void writeRecords(Batch batch) throws Exception {

    }
}
