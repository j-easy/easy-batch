/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.core.impl;

import org.easybatch.core.api.JobParameters;
import org.easybatch.core.api.JobReport;
import org.easybatch.core.api.JobStatus;
import org.easybatch.core.api.listener.JobListener;
import org.easybatch.core.util.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.DEFAULT_LIMIT;
import static org.easybatch.core.util.Utils.DEFAULT_SKIP;

/**
 * Job listener that logs and reports job parameters and metrics.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
class DefaultJobListener implements JobListener {

    private static final Logger LOGGER = Logger.getLogger(DefaultJobListener.class.getName());

    private JobReport jobReport;

    DefaultJobListener(JobReport jobReport) {
        this.jobReport = jobReport;
    }

    @Override
    public void beforeJobStart(final JobParameters jobParameters) {
        if (jobParameters.isSilentMode()) {
            Utils.muteLoggers();
        }
        LOGGER.info("Initializing the job");
        LOGGER.log(Level.INFO, "Job name: {0}", jobParameters.getName());
        LOGGER.log(Level.INFO, "Execution id: {0}", jobParameters.getExecutionId());
        String dataSource = jobParameters.getDataSource();
        LOGGER.log(Level.INFO, "Data source: {0}", dataSource == null ? "N/A" : dataSource);
        if (jobParameters.getSkip() != DEFAULT_SKIP) {
            LOGGER.log(Level.INFO, "Skip: {0}", jobParameters.getSkip());
        }
        if (jobParameters.getLimit() != DEFAULT_LIMIT ) {
            LOGGER.log(Level.INFO, "Limit: {0}", jobParameters.getLimit());
        }

        LOGGER.log(Level.INFO, "Strict mode: {0}", jobParameters.isStrictMode());
        LOGGER.log(Level.INFO, "Silent mode: {0}", jobParameters.isSilentMode());
        LOGGER.log(Level.INFO, "Jmx mode: {0}", jobParameters.isJmxMode());
        LOGGER.log(Level.INFO, "Keep alive: {0}", jobParameters.isKeepAlive());
        jobReport.getMetrics().setStartTime(System.currentTimeMillis()); //System.nanoTime() does not allow to have start time (see Javadoc)
        jobReport.setStatus(JobStatus.RUNNING);
        LOGGER.info("The job is running");

    }

    @Override
    public void afterJobEnd(JobReport jobReport) {
        jobReport.getMetrics().setEndTime(System.currentTimeMillis());
        if (!jobReport.getStatus().equals(JobStatus.ABORTED)) {
            jobReport.setStatus(JobStatus.FINISHED);
        }
    }
}
