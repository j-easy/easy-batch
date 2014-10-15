package org.easybatch.core.api;

import java.util.Set;

/**
 * Interface for handling rejected records.
 *
 * @author Chellan https://github.com/chellan
 */
public interface RejectedRecordHandler  {

    /**
     * @param recordNumber - the current number of rejected record
     * @param record - the current rejected record
     * @param e - the exception that caused the record to be rejected
     */
    void handle(final int recordNumber, final Record record, final Throwable e);
    
    /**
     * @param recordNumber - the current number of rejected record
     * @param record - the current error rejected number
     * @param validationsErrors - a set of validationsErrors that caused that record to be rejected
     */
    void handle(final int recordNumber, final Record record, Set<ValidationError> validationsErrors);

}
