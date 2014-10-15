package org.easybatch.core.api;

/**
 * Interface for handling ignored records.
 *
 * @author Chellan https://github.com/chellan
 */
public interface IgnoredRecordHandler  {

    /**
     * @param recordNumber - the current number of ignored record
     * @param record - the current ignored record
     * @param e - the exception that caused the record to be ignored
     */
    void handle(final int recordNumber, final Record record, final Throwable e);

}
