package org.easybatch.core.listener;

import org.easybatch.core.record.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite listener that delegates processing to other listeners.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeRecordReaderListener implements RecordReaderListener {

    private List<RecordReaderListener> listeners;

    /**
     * Create a new {@link CompositeRecordReaderListener}.
     */
    public CompositeRecordReaderListener() {
        this(new ArrayList<RecordReaderListener>());
    }

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
        for (RecordReaderListener listener : listeners) {
            listener.beforeRecordReading();
        }
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

    public void addRecordReaderListener(RecordReaderListener recordReaderListener) {
        listeners.add(recordReaderListener);
    }
}
