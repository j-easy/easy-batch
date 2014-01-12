/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package io.github.benas.easybatch.core.impl;

import io.github.benas.easybatch.core.api.Record;
import io.github.benas.easybatch.core.api.RecordProcessor;

import java.util.logging.Logger;

/**
 * A No Operation {@link RecordProcessor} implementation used by default by easy batch engine.
 *
 * @author benas (md.benhassine@gmail.com)
 */
class NoOpRecordProcessor implements RecordProcessor<Record, Object> {

    private Logger logger = Logger.getLogger(NoOpRecordProcessor.class.getName());

    /**
     * {@inheritDoc}
     */
    public void processRecord(final Record record) throws Exception {
        logger.info("Processing record : " + record);
    }

    /**
     * {@inheritDoc}
     */
    public Object getEasyBatchResult() {
        return new Object();
    }

}
