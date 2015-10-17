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

package org.easybatch.core.jmx;

import org.easybatch.core.api.Report;

import static java.lang.String.valueOf;
import static org.easybatch.core.util.Utils.DEFAULT_LIMIT;

/**
 * JMX MBean implementation of {@link MonitorMBean}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class Monitor implements MonitorMBean {

    /**
     * The batch report holding data exposed as JMX attributes.
     */
    private Report report;

    public Monitor(final Report report) {
        this.report = report;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEngineName() {
        return report.getEngineName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExecutionId() {
        return report.getExecutionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDataSource() {
        return report.getDataSource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCurrentRecordNumber() {
        return report.getCurrentRecordNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTotalRecords() {
        Long totalRecords = report.getTotalRecords();
        return totalRecords == null ? "N/A" : totalRecords.toString();
    }

    /**
     * Get records limit.
     *
     * @return records limit
     */
    @Override
    public String getRecordsLimit() {
        long limit = report.getLimit();
        return limit != DEFAULT_LIMIT ? valueOf(limit) : "No limit";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSkippedRecords() {
        return report.getFormattedSkippedRecords();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilteredRecords() {
        return report.getFormattedFilteredRecords();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorRecords() {
        return report.getFormattedErrorRecords();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSuccessRecords() {
        return report.getFormattedSuccessRecords();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStartTime() {
        return report.getFormattedStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEndTime() {
        return (report.getEndTime() == 0) ? "" : report.getFormattedEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProgress() {
        return report.getFormattedProgress();
    }

    @Override
    public String getStatus() {
        return report.getStatus().toString();
    }

}
