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

package net.benas.cb4j.monitor.gui;

import net.benas.cb4j.core.api.BatchReport;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * CB4J report update notification listener.
 * @author benas (md.benhassine@gmail.com)
 */
public class GuiReportUpdateNotificationListener implements NotificationListener {

    /**
     * The batch report instance that handles the current execution progress.
     */
    private BatchReport batchReport;

    /**
     * The GUI to update when an execution progress notification is received via JMX.
     */
    private CB4JMonitor cb4JMonitor;

    public GuiReportUpdateNotificationListener(CB4JMonitor cb4JMonitor) {
        this.cb4JMonitor = cb4JMonitor;
    }

    /**
     * {@inheritDoc}
     */
    public void handleNotification(Notification notification, Object handback) {

        if (notification instanceof AttributeChangeNotification) {
            AttributeChangeNotification acn = (AttributeChangeNotification) notification;
            BatchReport batchReport = (BatchReport)acn.getNewValue();
            this.batchReport = batchReport;
            cb4JMonitor.update(batchReport);
        }

    }

    /**
     * Getter for batch report.
     * @return the batch report
     */
    public BatchReport getBatchReport() {
        return batchReport;
    }

}