package org.easybatch.core.api;

/**
 * Interface for handling ignored records.
 *
 * @author Chellan https://github.com/chellan
 */
public interface IgnoredRecordHandler  {

    /**
     * @param record - the ignored record to handle
     * @param e - the exception that caused the record to be ignored
     */
    void handle(final Record record, final Throwable e);

}
