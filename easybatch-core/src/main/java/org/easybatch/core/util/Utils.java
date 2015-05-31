package org.easybatch.core.util;

import org.easybatch.core.api.Report;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.jmx.Monitor;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Enumeration;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Easy Batch's utilities class.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public abstract class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    public static final String DEFAULT_ENGINE_NAME = "engine";

    public static final String JMX_MBEAN_NAME = "org.easybatch.core.jmx:name=";

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
            name = new ObjectName(JMX_MBEAN_NAME + engine.toString());
            if (!mbs.isRegistered(name)) {
                Monitor monitor = new Monitor(report);
                mbs.registerMBean(monitor, name);
                LOGGER.log(Level.INFO, "Easy Batch JMX MBean registered successfully as: {0}", name.getCanonicalName());
            } else {
                LOGGER.log(Level.WARNING, "JMX MBean {0} already registered for another engine." +
                        " If you use multiple engines in parallel and you would like to monitor each of them, make sure they have different names",
                        name.getCanonicalName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to register Easy Batch JMX MBean.", e);
        }
    }
}
