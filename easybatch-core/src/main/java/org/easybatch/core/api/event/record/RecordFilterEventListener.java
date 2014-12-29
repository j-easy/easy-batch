package org.easybatch.core.api.event.record;

import org.easybatch.core.api.Record;

/**
 * Event interface collection for RecordFilter events.
 * <p>
 * You should implement this interface in your filter to
 * declare the ability to receive events from the batch process.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public interface RecordFilterEventListener {

    /**
     * Called before the filter step.
     * @param record The record that gets filtered
     */
    public void beforeFilterRecord(Record record);

    /**
     * Called after the filter step.
     * @param record The record that has been processed.
     * @param filterRecord Indicates if the filter has been applied successfully. True if filtered, false if not.
     */
    public void afterFilterRecord(Record record, boolean filterRecord);
}
