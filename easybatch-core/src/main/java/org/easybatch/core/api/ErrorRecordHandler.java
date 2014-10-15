package org.easybatch.core.api;

/**
 * Interface for handling error records.
 *
 * @author Chellan https://github.com/chellan
 */
public interface ErrorRecordHandler  {

    /**
     * @param recordNumber - the current number of error record
     * @param record - the current error record
     * @param e - the exception that caused the record to be rejected
     */
    void handle(final int recordNumber, final Record record, final Throwable e);

}
