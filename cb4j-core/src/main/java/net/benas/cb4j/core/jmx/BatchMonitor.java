/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.core.jmx;

import net.benas.cb4j.core.api.BatchReport;
import net.benas.cb4j.core.api.BatchReporter;

import javax.management.*;
import java.util.Date;

/**
 *  CB4J JMX MBean implementation of {@link BatchMonitorMBean}
 *  @author benas (md.benhassine@gmail.com)
 */
public class BatchMonitor extends NotificationBroadcasterSupport implements BatchMonitorMBean {

    /**
     * The batch reporter holding data exposed as JMX attributes
     */
    private BatchReporter batchReporter;

    /**
     * JMX notification sequence number.
     */
    private long sequenceNumber = 1;

    public BatchMonitor(BatchReporter batchReporter) {
        this.batchReporter = batchReporter;
    }

    /**
     * {@inheritDoc}
     */
    public long getInputRecordsNumber() {
        return batchReporter.getInputRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public long getTotalInputRecordsNumber() {
        return batchReporter.getTotalInputRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public long getIgnoredRecordsNumber() {
        return batchReporter.getIgnoredRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public long getRejectedRecordsNumber() {
        return batchReporter.getRejectedRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public long getErrorRecordsNumber() {
        return batchReporter.getErrorRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public long getProcessedRecordsNumber() {
        return batchReporter.getProcessedRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public String getStartTime() {
        return new Date(batchReporter.getStartTime()).toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getEndTime() {
        long endTime = batchReporter.getEndTime();
        return (endTime == 0) ? "" : new Date(endTime).toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getBatchStatus() {
        return batchReporter.getBatchStatus().toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getProgress() {
        long inputRecordsNumber = getInputRecordsNumber();
        long totalInputRecordsNumber = getTotalInputRecordsNumber();
        String ratio = inputRecordsNumber + "/" + totalInputRecordsNumber;
        String percent = " (" + ((int) (((float) inputRecordsNumber / (float) totalInputRecordsNumber) * 100)) + "%)";
        return ratio + percent;
    }

    public void notifyBatchReportUpdate(BatchReport batchReport) {
        Notification n =
                new AttributeChangeNotification(this,
                        sequenceNumber++,
                        System.currentTimeMillis(),
                        "batch report updated",
                        "BatchReport",
                        BatchReport.class.getName(),
                        null,//no need for old value
                        batchReport);
        //send asynchronous jmx notification for clients
        sendNotification(n);
    }
}
