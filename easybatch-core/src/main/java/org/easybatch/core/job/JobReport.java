/**
 * The MIT License
 *
 *   Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import java.io.Serializable;
import java.util.Properties;

/**
 * Class holding job reporting data.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobReport implements Serializable {

    private String jobName;

    private JobParameters parameters;

    private JobMetrics metrics;

    private JobStatus status;

    private Throwable lastError;

    private Properties systemProperties;

    public String getJobName() {
        return jobName;
    }

    public JobParameters getParameters() {
        return parameters;
    }

    public synchronized JobMetrics getMetrics() {
        return metrics;
    }

    public synchronized JobStatus getStatus() {
        return status;
    }

    public synchronized Throwable getLastError() {
        return lastError;
    }

    public synchronized void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public synchronized void setParameters(JobParameters parameters) {
        this.parameters = parameters;
    }

    public synchronized void setMetrics(JobMetrics metrics) {
        this.metrics = metrics;
    }

    public synchronized void setStatus(JobStatus status) {
        this.status = status;
    }

    public synchronized void setLastError(Throwable lastError) {
        this.lastError = lastError;
    }

    public synchronized Properties getSystemProperties() {
        return systemProperties;
    }

    public synchronized void setSystemProperties(Properties systemProperties) {
        this.systemProperties = systemProperties;
    }

    @Override
    public String toString() {
        return new DefaultJobReportFormatter().formatReport(this);
    }
}
