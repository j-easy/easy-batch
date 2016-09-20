package org.easybatch.core.writer;

import org.easybatch.core.record.Record;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Composite writer that delegates record writing to a list of writers.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeRecordWriter implements RecordWriter {

    private List<RecordWriter> writers;

    /**
     * Create a {@link CompositeRecordWriter}.
     *
     * @param writers delegates
     */
    public CompositeRecordWriter(List<RecordWriter> writers) {
        this.writers = writers;
    }

    /**
     * Create a {@link CompositeRecordWriter}.
     *
     * @param writers delegates
     */
    public CompositeRecordWriter(RecordWriter... writers) {
        this.writers = asList(writers);
    }

    @Override
    public void open() throws Exception {
        for (RecordWriter writer : writers) {
            writer.open();
        }
    }

    @Override
    public void writeRecord(Record record) throws Exception {
        for (RecordWriter writer : writers) {
            writer.writeRecord(record);
        }
    }

    @Override
    public void close() throws Exception {
        for (RecordWriter writer : writers) {
            writer.close();
        }
    }
}
