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
    public void beforeBatch() {
        for (BatchListener listener : listeners) {
            listener.beforeBatch();
        }
    }

    @Override
    public void afterBatch(List<Record> records) {
        for (BatchListener listener : listeners) {
            listener.afterBatch(records);
        }
    }
}
