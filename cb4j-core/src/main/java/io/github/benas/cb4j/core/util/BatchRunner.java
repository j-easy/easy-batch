/*
 * The MIT License
 *
 *  Copyright (c) 2013, benas (md.benhassine@gmail.com)
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

package io.github.benas.cb4j.core.util;

import io.github.benas.cb4j.core.api.BatchEngine;
import io.github.benas.cb4j.core.api.BatchReport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class to launch a batch engine.
 * @author benas (md.benhassine@gmail.com)
 */
public class BatchRunner {

    private Logger logger = Logger.getLogger(BatchConstants.LOGGER_CB4J);

    /**
     * The batch engine to use.
     */
    private BatchEngine batchEngine;

    /**
     * The executor service used to submit batch task and get result.
     */
    ExecutorService executorService;

    public BatchRunner(BatchEngine batchEngine) {
        this.batchEngine = batchEngine;
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Run the batch engine.
     * @return the batch report
     */
    public BatchReport run() {

        try {
            batchEngine.init();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An exception occurred during engine initialization. "
                    + "In order to avoid any unexpected behavior during batch execution due to this exception, execution is aborted. Root error : ", e);
            return null; // for security reason, abort execution
        }

        BatchReport batchReport;
        try {
            batchReport = executorService.submit(batchEngine).get();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An unexpected exception occurred during engine execution, root error : ", e);
            return null;
        } finally { //shutdown executor service in all cases (issue #5)
            executorService.shutdown();
        }

        try {
            batchEngine.shutdown();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An unexpected exception occurred during engine finalization, root error : ", e);
            return batchReport;
        }

        return batchReport;

    }
}
