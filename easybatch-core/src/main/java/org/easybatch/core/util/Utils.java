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

package org.easybatch.core.util;

import org.easybatch.core.api.Engine;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.Report;
import org.easybatch.core.jmx.Monitor;
import org.easybatch.core.record.MultiRecord;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * Easy Batch's utilities class.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public abstract class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static final String JAVA_IO_TMPDIR = System.getProperty("java.io.tmpdir");

    public static final String DEFAULT_ENGINE_NAME = "engine";

    public static final String JMX_MBEAN_NAME = "org.easybatch.core.jmx:";

    public static final Long DEFAULT_LIMIT = Long.MAX_VALUE;

    private Utils() {

    }

    /**
     * Mute easy batch loggers when silent mode is enabled.
     */
    public static void muteLoggers() {
        Enumeration<String> loggerNames = LogManager.getLogManager().getLoggerNames();
        while (loggerNames.hasMoreElements()) {
            String loggerName = loggerNames.nextElement();
            if (loggerName.startsWith("org.easybatch")) {
                muteLogger(loggerName);
            }
        }
    }

    private static void muteLogger(String logger) {
        Logger.getLogger(logger).setUseParentHandlers(false);
        Handler[] handlers = Logger.getLogger(logger).getHandlers();
        for (Handler handler : handlers) {
            Logger.getLogger(logger).removeHandler(handler);
        }
    }

    public static void registerJmxMBean(Report report, Engine engine) {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name;
        try {
            name = new ObjectName(JMX_MBEAN_NAME + "name=" + engine.getName() + ",id=" + engine.getExecutionId());
            if (!mbs.isRegistered(name)) {
                Monitor monitor = new Monitor(report);
                mbs.registerMBean(monitor, name);
                LOGGER.log(Level.INFO, "JMX MBean registered successfully as: {0}", name.getCanonicalName());
            } else {
                LOGGER.log(Level.WARNING, "JMX MBean {0} already registered for another engine." +
                                " If you use multiple engines in parallel and you would like to monitor each of them, make sure they have different names",
                        name.getCanonicalName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to register Easy Batch JMX MBean.", e);
        }
    }

    public static void checkNotNull(Object argument, String argumentName) {
        if (argument == null) {
            throw new IllegalArgumentException(format("The %s must not be null", argumentName));
        }
    }

    public static void checkArgument(boolean assertion, String message) {
        if (!assertion) {
            throw new IllegalArgumentException(message);
        }
    }

    public static Map<String, Method> getGetters(final Class type) throws IntrospectionException {
        Map<String, Method> getters = new HashMap<String, Method>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            getters.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod());
        }
        getters.remove("class"); //exclude property "class"
        return getters;
    }

    public static boolean isRecord(final Object record) {
        return Record.class.isAssignableFrom(record.getClass());
    }

    public static boolean isMultiRecord(final Object record) {
        return MultiRecord.class.isAssignableFrom(record.getClass());
    }

    public static boolean isCollection(final Object record) {
        return Collection.class.isAssignableFrom(record.getClass());
    }
}
