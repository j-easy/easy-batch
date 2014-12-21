package org.easybatch.core.impl;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RejectedRecordHandler;
import org.easybatch.core.api.ValidationError;

/**
 * A No Operation {@link RejectedRecordHandler} implementation used by default by easy batch engine.
 *
 * @author Chellan https://github.com/chellan
 */
class NoOpRejectedRecordHandler implements RejectedRecordHandler {

    private static final Logger LOGGER = Logger.getLogger(NoOpRejectedRecordHandler.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(int recordNumber, Record record, Throwable e) {
        LOGGER.log(Level.SEVERE, "An exception occurred while validating record #" + recordNumber + " [" + record + "]", e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(int recordNumber, Record record, Set<ValidationError> validationsErrors) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ValidationError validationError : validationsErrors) {
            stringBuilder.append(validationError.getMessage()).append(" | ");
        }
        LOGGER.log(Level.SEVERE, "Record #" + recordNumber + " [" + record + "] has been rejected. Validation error(s): " + stringBuilder.toString());
    }

}
