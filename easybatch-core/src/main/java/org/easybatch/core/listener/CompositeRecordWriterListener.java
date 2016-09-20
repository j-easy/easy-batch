package org.easybatch.core.listener;

import org.easybatch.core.record.Record;

import java.util.List;

/**
 * Composite listener that delegate processing to other listeners.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeRecordWriterListener implements RecordWriterListener {

    private List<RecordWriterListener> listeners;

    /**
     * Create a new {@link CompositeRecordWriterListener}.
     *
     * @param listeners delegates
     */
    public CompositeRecordWriterListener(List<RecordWriterListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void beforeRecordWriting(Record record) {
        for (RecordWriterListener listener : listeners) {
            listener.beforeRecordWriting(record);
        }
    }

    @Override
    public void afterRecordWriting(Record record) {
        for (RecordWriterListener listener : listeners) {
            listener.afterRecordWriting(record);
        }
    }

    @Override
    public void onRecordWritingException(Record record, Throwable throwable) {
        for (RecordWriterListener listener : listeners) {
            listener.onRecordWritingException(record, throwable);
        }
    }
}
