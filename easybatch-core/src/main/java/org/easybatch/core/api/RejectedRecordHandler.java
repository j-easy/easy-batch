package org.easybatch.core.api;

import java.util.Set;

/**
 * Interface for handling rejected records.
 *
 * @author Chellan https://github.com/chellan
 */
public interface RejectedRecordHandler  {

    /**
     * @param record - the rejected record to handle
     * @param e - the exception that caused the record to be rejected
     */
    void handle(final Record record, final Throwable e);
    
    /**
     * @param record - the rejected record to handle
     * @param validationsErrors - a set of validationsErrors that caused that record to be rejected
     */
    void handle(final Record record, Set<ValidationError> validationsErrors);

}
