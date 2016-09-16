package org.easybatch.core.job;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Record;

class NoOpRecordReader implements RecordReader {
    @Override
    public void open() throws Exception {

    }

    @Override
    public Record readRecord() throws Exception {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
