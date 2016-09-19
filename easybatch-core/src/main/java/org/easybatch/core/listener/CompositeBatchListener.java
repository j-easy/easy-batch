package org.easybatch.core.listener;

import org.easybatch.core.record.Record;

import java.util.List;

/**
 * Composite listener that delegate processing to other listeners.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeBatchListener implements BatchListener {

    private List<BatchListener> listeners;

    /**
     * Create a new {@link CompositeBatchListener}.
     *
     * @param listeners delegates
     */
    public CompositeBatchListener(List<BatchListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void beforeBatchReading() {
        listeners.forEach(BatchListener::beforeBatchReading);
    }

    @Override
    public void afterBatchProcessing(List<Record> records) {
        for (BatchListener listener : listeners) {
            listener.afterBatchProcessing(records);
        }
    }

    @Override
    public void afterBatchWriting(List<Record> records) {
        for (BatchListener listener : listeners) {
            listener.afterBatchWriting(records);
        }
    }

    @Override
    public void onBatchWritingException(List<Record> records, Throwable throwable) {
        for (BatchListener listener : listeners) {
            listener.onBatchWritingException(records, throwable);
        }
    }
}
