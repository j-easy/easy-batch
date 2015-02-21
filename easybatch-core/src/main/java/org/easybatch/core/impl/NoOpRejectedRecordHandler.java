/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

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
    public void handle(final Record record, final Throwable e) {
        LOGGER.log(Level.SEVERE, "An exception occurred while validating record #" + record.getHeader().getNumber() + " [" + record + "]", e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final Record record, final Set<ValidationError> validationsErrors) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ValidationError validationError : validationsErrors) {
            stringBuilder.append(validationError.getMessage()).append(" | ");
        }
        LOGGER.log(Level.SEVERE, "Record #{0} [{1}] has been rejected. Validation error(s): {2}", new Object[]{record.getHeader().getNumber(), record, stringBuilder.toString()});
    }

}
