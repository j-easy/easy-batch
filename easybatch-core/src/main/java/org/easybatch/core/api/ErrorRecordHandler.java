package org.easybatch.core.api;

/**
 * Interface for handling error record
 *
 * @author Chellan https://github.com/chellan
 */
public interface ErrorRecordHandler  {

    /**
     * @param recordNumber - the current number of error record
     * @param record - the current error record number
     * @param e - the exception
     * @return void
     */
    void handle(final int recordNumber, final Record record, final Throwable e);

}
