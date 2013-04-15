/*
 * The MIT License
 *
 *    Copyright (c) 2012, benas (md.benhassine@gmail.com)
 *
 *    Permission is hereby granted, free of charge, to any person obtaining a copy
 *    of this software and associated documentation files (the "Software"), to deal
 *    in the Software without restriction, including without limitation the rights
 *    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *    copies of the Software, and to permit persons to whom the Software is
 *    furnished to do so, subject to the following conditions:
 *
 *    The above copyright notice and this permission notice shall be included in
 *    all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *    THE SOFTWARE.
 */

package net.benas.cb4j.monitor.cli;

import net.benas.cb4j.core.api.BatchReport;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * CB4J report update notification listener.
 * @author benas (md.benhassine@gmail.com)
 */
public class ReportUpdateNotificationListener implements NotificationListener {

    private BatchReport batchReport;

    /**
     * {@inheritDoc}
     */
    public void handleNotification(Notification notification, Object handback) {

        if (notification instanceof AttributeChangeNotification) {
            AttributeChangeNotification acn = (AttributeChangeNotification) notification;
            BatchReport batchReport = (BatchReport)acn.getNewValue();
            this.batchReport = batchReport;
            printProgress(batchReport);
        }

    }

    /**
     * Prints a string representation of execution progress.
     * @param batchReport the report to print
     */
    public static void printProgress(BatchReport batchReport) {

        long totalInputRecordsNumber = batchReport.getTotalInputRecordsNumber();
        long inputRecordsNumber = batchReport.getInputRecordsNumber();
        long ignoredRecordsNumber = batchReport.getIgnoredRecordsNumber();
        long rejectedRecordsNumber = batchReport.getRejectedRecordsNumber();
        long errorRecordsNumber = batchReport.getErrorRecordsNumber();
        long processedRecordsNumber = inputRecordsNumber - ignoredRecordsNumber - rejectedRecordsNumber - errorRecordsNumber;

        StringBuilder bar = new StringBuilder("[CB4J progress] ");
        bar.append("Total= ").append(totalInputRecordsNumber);
        bar.append(" | ");
        bar.append("Current= ").append(inputRecordsNumber);
        bar.append(" | ");
        bar.append("Progress= ").append(percent(inputRecordsNumber, totalInputRecordsNumber));
        bar.append("% | ");
        bar.append("Ignored= ").append(ignoredRecordsNumber).append("(").append(percent(ignoredRecordsNumber, totalInputRecordsNumber)).append("%)");
        bar.append(" | ");
        bar.append("Rejected= ").append(rejectedRecordsNumber).append("(").append(percent(rejectedRecordsNumber, totalInputRecordsNumber)).append("%)");
        bar.append(" | ");
        bar.append("Processed= ").append(processedRecordsNumber).append("(").append(percent(processedRecordsNumber, totalInputRecordsNumber)).append("%)");
        bar.append(" | ");
        bar.append("Errors= ").append(errorRecordsNumber).append("(").append(percent(errorRecordsNumber, totalInputRecordsNumber)).append("%)");
        System.out.print("\r" + bar.toString());
    }

    /**
     * Calculate a percent of a progress.
     * @param current the current amount of records processed
     * @param total the total amount of records to process
     * @return the progress in percent
     */
    public static int percent(long current, long total) {
        return ((int) (((float) current / (float) total) * 100));
    }

    /**
     * Getter for batch report.
     * @return the batch report
     */
    public BatchReport getBatchReport() {
        return batchReport;
    }

}