package org.easybatch.core.job;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Record;

import java.util.concurrent.Callable;

class RecordReadingCallable implements Callable<Record> {

    private RecordReader recordReader;

    public RecordReadingCallable(RecordReader recordReader) {
        this.recordReader = recordReader;
    }

    @Override
    public Record call() throws Exception {
        return recordReader.readNextRecord();
    }

}