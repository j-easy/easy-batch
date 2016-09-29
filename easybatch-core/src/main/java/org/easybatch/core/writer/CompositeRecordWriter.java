package org.easybatch.core.writer;

import org.easybatch.core.record.Batch;

import java.util.List;

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

    @Override
    public void open() throws Exception {
        for (RecordWriter writer : writers) {
            writer.open();
        }
    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        for (RecordWriter writer : writers) {
            writer.writeRecords(batch);
        }
    }

    @Override
    public void close() throws Exception {
        for (RecordWriter writer : writers) {
            writer.close();
        }
    }
}
