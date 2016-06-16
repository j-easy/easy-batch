/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core.job;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.Date;

/**
 * JMX MBean implementation of {@link JobMonitorMBean}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class JobMonitor extends NotificationBroadcasterSupport implements JobMonitorMBean {

    /**
     * JMX notification sequence number.
     */
    private long sequenceNumber = 1;

    /**
     * The batch report holding data exposed as JMX attributes.
     */
    private JobReport jobReport;

    public JobMonitor(final JobReport jobReport) {
        this.jobReport = jobReport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJobName() {
        return jobReport.getParameters().getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJobExecutionId() {
        return jobReport.getParameters().getExecutionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDataSource() {
        return jobReport.getParameters().getDataSource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTotalCount() {
        return jobReport.getFormattedTotalCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRecordsLimit() {
        return jobReport.getFormattedLimit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTimeout() {
        return jobReport.getFormattedTimeout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSkippedCount() {
        return jobReport.getFormattedSkippedCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilteredCount() {
        return jobReport.getFormattedFilteredCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorCount() {
        return jobReport.getFormattedErrorCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSuccessCount() {
        return jobReport.getFormattedSuccessCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStartTime() {
        return jobReport.getFormattedStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEndTime() {
        return (jobReport.getMetrics().getEndTime() == 0) ? "" : jobReport.getFormattedEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProgress() {
        return jobReport.getFormattedProgress();
    }

    @Override
    public String getJobStatus() {
        return jobReport.getStatus().toString();
    }

    void notifyJobReportUpdate() {
        Notification notification = new AttributeChangeNotification(
                this,
                sequenceNumber++,
                new Date().getTime(),
                "job report updated",
                "JobReport",
                JobReport.class.getName(),
                null, //no need for old value
                jobReport);
        sendNotification(notification);
    }
}
