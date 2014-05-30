/*
 * The MIT License
 *
 *   Copyright (c) 2014, Mahmoud Ben Hassine (md.benhassine@gmail.com)
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

package org.easybatch.core.jmx;

import org.easybatch.core.api.EasyBatchReport;

/**
 *  Easy batch JMX MBean implementation of {@link EasyBatchMonitorMBean}.
 *
 *  @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class EasyBatchMonitor implements EasyBatchMonitorMBean {

    /**
     * The batch report holding data exposed as JMX attributes.
     */
    private EasyBatchReport easyBatchReport;

    public EasyBatchMonitor(final EasyBatchReport easyBatchReport) {
        this.easyBatchReport = easyBatchReport;
    }

    @Override
    public String getDataSource() {
        return easyBatchReport.getDataSource();
    }

    /**
     * {@inheritDoc}
     */
    public long getCurrentRecordNumber() {
        return easyBatchReport.getCurrentRecordNumber();
    }

    /**
     * {@inheritDoc}
     */
    public String getTotalRecords() {
        Integer totalRecords = easyBatchReport.getTotalRecords();
        return totalRecords == null ? "N/A" : totalRecords.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getFilteredRecords() {
        return easyBatchReport.getFormattedFilteredRecords();
    }

    /**
     * {@inheritDoc}
     */
    public String getIgnoredRecords() {
        return easyBatchReport.getFormattedIgnoredRecords();
    }

    /**
     * {@inheritDoc}
     */
    public String getRejectedRecords() {
        return easyBatchReport.getFormattedRejectedRecords();
    }

    /**
     * {@inheritDoc}
     */
    public String getErrorRecords() {
        return easyBatchReport.getFormattedErrorRecords();
    }

    /**
     * {@inheritDoc}
     */
    public String getSuccessRecords() {
        return easyBatchReport.getFormattedSuccessRecords();
    }

    /**
     * {@inheritDoc}
     */
    public String getStartTime() {
        return easyBatchReport.getFormattedStartTime();
    }

    /**
     * {@inheritDoc}
     */
    public String getEndTime() {
        return (easyBatchReport.getEndTime() == 0) ? "" : easyBatchReport.getFormattedEndTime();
    }

    /**
     * {@inheritDoc}
     */
    public String getProgress() {
        return easyBatchReport.getFormattedProgress();
    }

}
