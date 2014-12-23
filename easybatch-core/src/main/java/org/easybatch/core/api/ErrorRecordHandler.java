package org.easybatch.core.api;

/**
 * Interface for handling error records.
 *
 * @author Chellan https://github.com/chellan
 */
public interface ErrorRecordHandler  {

    /**
     * @param record - the error record to handle
     * @param e - the exception that caused the record to be rejected
     */
    void handle(final Record record, final Throwable e);

}
