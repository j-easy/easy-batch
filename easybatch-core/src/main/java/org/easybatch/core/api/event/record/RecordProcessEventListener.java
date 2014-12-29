package org.easybatch.core.api.event.record;

import org.easybatch.core.api.event.global.BatchProcessEventListener;
import org.easybatch.core.impl.EasyBatchEngineBuilder;

/**
 * Enables the implementing class to get an event on different steps within the batch process.
 * <p>
 * Do not use this interface directly, instead use the correct inheritance for your need.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public interface RecordProcessEventListener extends BatchProcessEventListener {

    /**
     * Callback after the add to the builder.
     * The events happens in {@link org.easybatch.core.impl.EasyBatchEngineBuilder#registerRecordMapper}.
     *
     * @param batchEngineBuilder The current state of the batch builder after adding this mapper.
     */
    public void afterAdd(EasyBatchEngineBuilder batchEngineBuilder);
}
