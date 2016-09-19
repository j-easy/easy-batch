package org.easybatch.core.listener;

import org.easybatch.core.record.Record;

import java.util.List;

/**
 * Composite listener that delegate processing to other listeners.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeRecordReaderListener implements RecordReaderListener {

    private List<RecordReaderListener> listeners;

    /**
     * Create a new {@link CompositeRecordReaderListener}.
     *
     * @param listeners delegates
     */
    public CompositeRecordReaderListener(List<RecordReaderListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void beforeRecordReading() {
        listeners.forEach(RecordReaderListener::beforeRecordReading);
    }

    @Override
    public void afterRecordReading(Record record) {
        for (RecordReaderListener listener : listeners) {
            listener.afterRecordReading(record);
        }
    }

    @Override
    public void onRecordReadingException(Throwable throwable) {
        for (RecordReaderListener listener : listeners) {
            listener.onRecordReadingException(throwable);
        }
    }
}
