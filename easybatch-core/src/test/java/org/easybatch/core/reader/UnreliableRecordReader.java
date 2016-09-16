package org.easybatch.core.reader;

import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.util.Date;

public class UnreliableRecordReader implements RecordReader {

    public static final String DATA_SOURCE_NAME = "dummy source";
    public static final int TOTAL_RECORDS = 3;
    private int attempt;
    private int nbRecord;

    @Override
    public void open() throws Exception {
        attempt = 0;
        nbRecord = 0;
    }

    @Override
    public Record readRecord() throws Exception {
        if (++nbRecord > TOTAL_RECORDS) {
            return null;
        }
        attempt++;
        if (attempt >= 3) {
            return new StringRecord(new Header((long) nbRecord, DATA_SOURCE_NAME, new Date()), "r" + nbRecord);
        } else {
            throw new Exception("Data source has gone!");
        }
    }

    @Override
    public void close() throws Exception {

    }
}
