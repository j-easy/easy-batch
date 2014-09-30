package org.easybatch.core.api;

/**
 * Interface for handling ignored record
 *
 * @author Chellan https://github.com/chellan
 */
public interface IgnoredRecordHandler  {

    /**
     * @param recordNumber - the current number of ignored record
     * @param record - the current ignored record number
     * @param e - the exception
     * @return void
     */
    void handle(final int recordNumber, final Record record, final Throwable e);

}
