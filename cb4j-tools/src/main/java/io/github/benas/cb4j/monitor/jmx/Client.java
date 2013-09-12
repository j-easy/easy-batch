/*
 * The MIT License
 *
 *    Copyright (c) 2013, benas (md.benhassine@gmail.com)
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

package io.github.benas.cb4j.monitor.jmx;

import javax.management.*;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * A custom JMX client that subscribes to notifications sent by CB4J monitor.
 * @author benas (md.benhassine@gmail.com)
 */
public class Client {

    /**
     * The host where CB4J batch is running.
     */
    private String host;

    /**
     * The port used for remote JMX monitoring.
     */
    private String port;

    public Client(String host, String port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Initialize the JMX client to listen to notification sent by CB4J monitor.
     * @param notificationListener the listener for report updates
     * @param connectionClosedNotificationListener the listener for JMX connection-closed event
     * @throws Exception thrown if an exception occurs during client initialization
     */
    public void init(NotificationListener notificationListener, NotificationListener connectionClosedNotificationListener) throws Exception {
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        ObjectName mbeanName = new ObjectName("io.github.benas.cb4j.jmx:type=BatchMonitorMBean");
        mbsc.addNotificationListener(mbeanName, notificationListener, null, null);
        if (connectionClosedNotificationListener != null) {
            jmxc.addConnectionNotificationListener(connectionClosedNotificationListener, new ConnectionClosedNotificationFilter(), null);
        }
    }

    /**
     * A notification filter that filters all notification events and notifies only the connection-closed event {@link JMXConnectionNotification#CLOSED}
     */
    private static class ConnectionClosedNotificationFilter implements NotificationFilter {

        @Override
        public boolean isNotificationEnabled(Notification notification) {
            return notification.getType().equals(JMXConnectionNotification.CLOSED);
        }

    }

}
