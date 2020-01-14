/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.core.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Date;

import static org.easybatch.core.util.Utils.JMX_MBEAN_NAME;
import static org.easybatch.core.util.Utils.formatTime;

/**
 * JMX MBean implementation of {@link JobMonitorMBean}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class JobMonitor extends NotificationBroadcasterSupport implements JobMonitorMBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobMonitor.class.getName());

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
        return jobReport.getJobName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getReadCount() {
        return jobReport.getMetrics().getReadCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getFilterCount() {
        return jobReport.getMetrics().getFilterCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getErrorCount() {
        return jobReport.getMetrics().getErrorCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getWriteCount() {
        return jobReport.getMetrics().getWriteCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStartTime() {
        return formatTime(jobReport.getMetrics().getStartTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEndTime() {
        return (jobReport.getMetrics().getEndTime() == 0) ? "" : formatTime(jobReport.getMetrics().getEndTime());
    }

    @Override
    public String getJobStatus() {
        return jobReport.getStatus().name();
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

    void registerJmxMBeanFor(Job job) {
        LOGGER.info("Registering JMX MBean for job {}", job.getName());
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name;
        try {
            name = new ObjectName(JMX_MBEAN_NAME + "name=" + job.getName());
            if (!mbs.isRegistered(name)) {
                mbs.registerMBean(this, name);
                LOGGER.info("JMX MBean registered successfully as: {0}", name.getCanonicalName());
            }
        } catch (Exception e) {
            LOGGER.warn("Unable to register MBean for job {}", job.getName(), e);
        }
    }
}
