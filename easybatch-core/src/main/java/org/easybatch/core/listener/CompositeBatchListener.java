package org.easybatch.core.listener;

import org.easybatch.core.record.Batch;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Composite listener that delegates processing to other listeners.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeBatchListener implements BatchListener {

    private List<BatchListener> listeners;

    /**
     * Create a new {@link CompositeBatchListener}.
     */
    public CompositeBatchListener() {
        this(new ArrayList<BatchListener>());
    }

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
        for (ListIterator<BatchListener> iterator
                = listeners.listIterator(listeners.size());
                iterator.hasPrevious();) {
            iterator.previous().afterBatchProcessing(batch);
        }
    }

    @Override
    public void afterBatchWriting(Batch batch) {
        for (ListIterator<BatchListener> iterator
                = listeners.listIterator(listeners.size());
                iterator.hasPrevious();) {
            iterator.previous().afterBatchWriting(batch);
        }
    }

    @Override
    public void onBatchWritingException(Batch batch, Throwable throwable) {
        for (ListIterator<BatchListener> iterator
                = listeners.listIterator(listeners.size());
                iterator.hasPrevious();) {
            iterator.previous().onBatchWritingException(batch, throwable);
        }
    }

    /**
     * Add a delegate listener.
     *
     * @param batchListener to add
     */
    public void addBatchListener(final BatchListener batchListener) {
        listeners.add(batchListener);
    }
}
