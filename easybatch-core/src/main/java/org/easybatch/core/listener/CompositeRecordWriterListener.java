package org.easybatch.core.listener;

import org.easybatch.core.record.Batch;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite listener that delegates processing to other listeners.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeRecordWriterListener implements RecordWriterListener {

    private List<RecordWriterListener> listeners;

    /**
     * Create a new {@link CompositeRecordWriterListener}.
     */
    public CompositeRecordWriterListener() {
        this(new ArrayList<RecordWriterListener>());
    }

    /**
     * Create a new {@link CompositeRecordWriterListener}.
     *
     * @param listeners delegates
     */
    public CompositeRecordWriterListener(List<RecordWriterListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void beforeRecordWriting(Batch batch) {
        for (RecordWriterListener listener : listeners) {
            listener.beforeRecordWriting(batch);
        }
    }

    @Override
    public void afterRecordWriting(Batch batch) {
        for (RecordWriterListener listener : listeners) {
            listener.afterRecordWriting(batch);
        }
    }

    @Override
    public void onRecordWritingException(Batch batch, Throwable throwable) {
        for (RecordWriterListener listener : listeners) {
            listener.onRecordWritingException(batch, throwable);
        }
    }

    /**
     * Add a delegate listener.
     *
     * @param recordWriterListener to add
     */
    public void addRecordWriterListener(RecordWriterListener recordWriterListener) {
        listeners.add(recordWriterListener);
    }
}
