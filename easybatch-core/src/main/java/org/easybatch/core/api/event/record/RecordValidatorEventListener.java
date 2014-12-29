package org.easybatch.core.api.event.record;


import org.easybatch.core.api.ValidationError;

import java.util.Set;

/**
 * Event interface collection for RecordValidator events.
 * <p>
 * You should implement this interface in your validator to
 * declare the ability to receive events from the batch process.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public interface RecordValidatorEventListener {

    /**
     * Called before each validation.
     * @param mappedRecord The mapped record that is going to be validated.
     */
    public void beforeValidateRecord(Object mappedRecord);

    /**
     * Called after each validation.
     * @param validatedRecord The validated record.
     * @param validationErrors The Set of validation errors, if any.
     */
    public void afterValidateRecord(Object validatedRecord, Set<ValidationError> validationErrors);
}
