package org.easybatch.tutorials.advanced.restart;

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.RecordWriter;

import java.util.Random;

public class BuggyWriter implements RecordWriter {

    private RecordWriter recordWriter;

    public BuggyWriter(RecordWriter recordWriter) {
        this.recordWriter = recordWriter;
    }

    @Override
    public void open() throws Exception {
        recordWriter.open();
    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        boolean crash = new Random().nextBoolean();
        if (crash) {
            for (Record record : batch) {
                if (record.getHeader().getNumber().equals(4L)) {
                    throw new Exception("Unable to write records");
                }
            }
        }
        recordWriter.writeRecords(batch);
    }

    @Override
    public void close() throws Exception {
        recordWriter.close();
    }
}