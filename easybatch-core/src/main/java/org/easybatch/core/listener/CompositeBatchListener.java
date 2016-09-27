package org.easybatch.core.listener;

import org.easybatch.core.record.Batch;

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
        for (BatchListener listener : listeners) {
            listener.beforeBatchReading();
        }
    }

    @Override
    public void afterBatchProcessing(Batch batch) {
        for (BatchListener listener : listeners) {
            listener.afterBatchProcessing(batch);
        }
    }

    @Override
    public void afterBatchWriting(Batch batch) {
        for (BatchListener listener : listeners) {
            listener.afterBatchWriting(batch);
        }
    }

    @Override
    public void onBatchWritingException(Batch batch, Throwable throwable) {
        for (BatchListener listener : listeners) {
            listener.onBatchWritingException(batch, throwable);
        }
    }
}
