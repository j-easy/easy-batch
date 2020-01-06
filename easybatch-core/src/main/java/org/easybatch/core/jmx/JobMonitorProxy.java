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
package org.easybatch.core.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;
import static org.easybatch.core.util.Utils.JMX_MBEAN_NAME;

/**
 * Proxy that subscribes to notifications sent by a running job monitor.
 * Notifications are broadcast to listeners registered using {@link JobMonitorProxy#addMonitoringListener(JobMonitoringListener)}
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobMonitorProxy implements Runnable {

    private static final String JMX_SERVICE_URL = "service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobMonitorProxy.class.getName());

    private String host;
    private int port;
    private String jobName;
    private List<JobMonitoringListener> notificationListeners;

    /**
     * Create a new {@link JobMonitorProxy}.
     *
     * @param host           on which the job is running
     * @param port           on which JMX notification are sent
     * @param jobName        the job name
     */
    public JobMonitorProxy(final String host, final int port, final String jobName) {
        this.host = host;
        this.port = port;
        this.jobName = jobName;
        notificationListeners = new ArrayList<>();
    }

    /**
     * Register a {@link JobMonitoringListener}.
     *
     * @param jobMonitoringListener to register
     */
    public void addMonitoringListener(final JobMonitoringListener jobMonitoringListener) {
        notificationListeners.add(jobMonitoringListener);
    }

    @Override
    public void run() {
        try {
            String serviceURL = format(JMX_SERVICE_URL, host, port);
            JMXServiceURL url = new JMXServiceURL(serviceURL);
            ObjectName objectName = new ObjectName(JMX_MBEAN_NAME + "name=" + jobName);
            JMXConnector jmxConnector = JMXConnectorFactory.connect(url, new HashMap<String, Object>());
            MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
            registerNotificationListeners(objectName, mBeanServerConnection, jmxConnector);
            LOGGER.info("Listening to notifications from {}", serviceURL);
        } catch (MalformedObjectNameException | IOException | InstanceNotFoundException e) {
            LOGGER.error("Unable to listen to job monitoring notifications", e);
        }
    }

    private void registerNotificationListeners(final ObjectName objectName, final MBeanServerConnection mBeanServerConnection, final JMXConnector jmxConnector) throws InstanceNotFoundException, IOException {
        for (JobMonitoringListener notificationListener : notificationListeners) {
            mBeanServerConnection.addNotificationListener(objectName, notificationListener, null, null);
            jmxConnector.addConnectionNotificationListener(notificationListener, new ConnectionClosedNotificationFilter(), null);
            jmxConnector.addConnectionNotificationListener(notificationListener, new ConnectionOpenedNotificationFilter(), null);
        }
    }

    private static class ConnectionClosedNotificationFilter implements NotificationFilter {
        public boolean isNotificationEnabled(Notification notification) {
            return notification.getType().equals(JMXConnectionNotification.CLOSED);
        }
    }

    private static class ConnectionOpenedNotificationFilter implements NotificationFilter {
        public boolean isNotificationEnabled(Notification notification) {
            return notification.getType().equals(JMXConnectionNotification.OPENED);
        }
    }


}
