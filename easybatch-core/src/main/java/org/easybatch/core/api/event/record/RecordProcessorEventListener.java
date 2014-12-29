package org.easybatch.core.api.event.record;

/**
 * Event interface collection for RecordProcessor events.
 * <p>
 * You should implement this interface in your processor to
 * declare the ability to receive events from the batch process.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public interface RecordProcessorEventListener {

    /**
     * Called before the record gets processed.
     * @param record The record that will be processed.
     */
    public void beforeProcessRecord(Object record);

    /**
     * Called after the record has been processed.
     * @param record The processed record.
     * @param processResult The process result, called from {@link org.easybatch.core.api.ComputationalRecordProcessor#getComputationResult()}
     */
    public void afterRecordProcessed(Object record, Object processResult);
}
