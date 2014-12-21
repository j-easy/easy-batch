package org.easybatch.core.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.easybatch.core.api.ErrorRecordHandler;
import org.easybatch.core.api.Record;

/**
 * A No Operation {@link ErrorRecordHandler} implementation used by default by easy batch engine.
 *
 * @author Chellan https://github.com/chellan
 */
class NoOpErrorRecordHandler implements ErrorRecordHandler {

    private static final Logger LOGGER = Logger.getLogger(NoOpErrorRecordHandler.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(int recordNumber, Record record, Throwable e) {
        LOGGER.log(Level.SEVERE, "Error while processing record #" + recordNumber + "[" + record + "]", e);
    }

}
