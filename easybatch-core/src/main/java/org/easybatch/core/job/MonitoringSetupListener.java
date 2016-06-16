/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import org.easybatch.core.listener.JobListener;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.JMX_MBEAN_NAME;

/**
 * Job listener that sets up monitoring MBean if JMX mode is enabled.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class MonitoringSetupListener implements JobListener {

    private static final Logger LOGGER = Logger.getLogger(MonitoringSetupListener.class.getName());

    private JobImpl job;

    MonitoringSetupListener(JobImpl job) {
        this.job = job;
    }

    @Override
    public void beforeJobStart(JobParameters jobParameters) {
        if (jobParameters.isJmxMode()) {
            registerJmxMBean(job);
            LOGGER.log(Level.INFO, "Calculating the total number of records");
            Long totalRecords = job.getRecordReader().getTotalRecords();
            job.getJobReport().getMetrics().setTotalCount(totalRecords);
            LOGGER.log(Level.INFO, "Total records count = {0}", totalRecords == null ? "N/A" : totalRecords);
        }
    }

    @Override
    public void afterJobEnd(JobReport jobReport) {
        // no-op
    }

    private void registerJmxMBean(JobImpl job) {
        LOGGER.log(Level.INFO, "Registering JMX MBean for job {0}", job.getName());
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name;
        try {
            name = new ObjectName(JMX_MBEAN_NAME + "name=" + job.getName() + ",id=" + job.getExecutionId());
            if (!mbs.isRegistered(name)) {
                mbs.registerMBean(job.getJobMonitor(), name);
                LOGGER.log(Level.INFO, "JMX MBean registered successfully as: {0}", name.getCanonicalName());
            } else {
                LOGGER.log(Level.WARNING, "JMX MBean {0} already registered for another job." +
                                " If you run multiple jobs in parallel and you would like to monitor each of them, make sure they have different names",
                        name.getCanonicalName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to register Easy Batch JMX MBean.", e);
        }
    }
}
