package org.easybatch.core.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.easybatch.core.api.IgnoredRecordHandler;
import org.easybatch.core.api.Record;

/**
 * A No Operation {@link IgnoredRecordHandler} implementation used by default by easy batch engine.
 *
 * @author Chellan https://github.com/chellan
 */
class NoOpIgnoredRecordHandler implements IgnoredRecordHandler {

    private static final Logger LOGGER = Logger.getLogger(NoOpIgnoredRecordHandler.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final Record record, final Throwable e) {
        LOGGER.log(Level.SEVERE, "Record #" + record.getNumber() + " [" + record + "] has been ignored. Root exception:", e);
    }

}
