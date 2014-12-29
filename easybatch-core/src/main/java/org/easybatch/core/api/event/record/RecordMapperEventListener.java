package org.easybatch.core.api.event.record;

import org.easybatch.core.api.Record;

/**
 * Event interface collection for RecordMapper events.
 * <p>
 * You should implement this interface in your mapper to
 * declare the ability to receive events from the batch process.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public interface RecordMapperEventListener extends RecordProcessEventListener {

    /**
     * Called before the mapper call.
     *
     * @param record The record that will be passed to the record call.
     */
    public void beforeMapRecord(Record record);

    /**
     * Called directly after the mapper call.
     *
     * @param record The incoming record.
     * @param typedRecord The mapping result.
     */
    public void afterMapRecord(Record record, Object typedRecord);

    /**
     * When the mapper throws an exception, this method will get the exception.
     * In addition you will receive the incoming record for further inspection.
     *
     * @param t The exception thrown.
     * @param record The record that was processed while the exception was thrown.
     */
    public void onMapperException(Throwable t, Record record);
}
