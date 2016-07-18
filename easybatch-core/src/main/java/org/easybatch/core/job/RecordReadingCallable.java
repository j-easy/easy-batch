package org.easybatch.core.job;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Record;

import java.util.concurrent.Callable;

/**
 * Wrapper of record reading code in a {@link Callable} to be able to use it in a {@link org.easybatch.core.retry.RetryTemplate}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
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