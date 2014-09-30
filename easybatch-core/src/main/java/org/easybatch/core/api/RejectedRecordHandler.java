package org.easybatch.core.api;

import java.util.Set;
import java.util.logging.Level;

/**
 * Interface for handling rejected record
 *
 * @author Chellan https://github.com/chellan
 */
public interface RejectedRecordHandler  {

    /**
     * @param recordNumber - the current number of rejected record
     * @param record - the current error rejected number
     * @param e - the exception
     * @return void
     */
    void handle(final int recordNumber, final Record record, final Throwable e);
    
    /**
     * @param recordNumber - the current number of rejected record
     * @param record - the current error rejected number
     * @param validationsErrors - a set of validationsErrors that caused that record to be rejected
     * @return void
     */
    void handle(final int recordNumber, final Record record, Set<ValidationError> validationsErrors);

}
