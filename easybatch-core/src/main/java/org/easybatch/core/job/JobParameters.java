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

import org.easybatch.core.retry.RetryPolicy;
import org.easybatch.core.util.Utils;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Parameters of a job.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobParameters implements Serializable {

    public static final String DEFAULT_JOB_NAME = "job";

    public static final Long DEFAULT_LIMIT = Long.MAX_VALUE;

    public static final Long DEFAULT_ERROR_THRESHOLD = Long.MAX_VALUE;

    public static final Long DEFAULT_SKIP = 0L;

    public static final long DEFAULT_TIMEOUT = TimeUnit.MILLISECONDS.convert(31, TimeUnit.DAYS);

    private  static final RetryPolicy DEFAULT_RETRY_POLICY = new RetryPolicy(1, 1, TimeUnit.SECONDS);

    private String name;

    private String executionId;

    private String hostname;

    private String dataSource;

    private long skip;

    private long limit;

    private long timeout;

    private long errorThreshold;

    private boolean silentMode;

    private boolean jmxMonitoring;

    private boolean keepAlive;

    private Properties systemProperties;

    private RetryPolicy retryPolicy;

    public JobParameters() {
        this.name = DEFAULT_JOB_NAME;
        this.executionId = UUID.randomUUID().toString();
        this.skip = DEFAULT_SKIP;
        this.limit = DEFAULT_LIMIT;
        this.timeout = DEFAULT_TIMEOUT;
        this.errorThreshold = DEFAULT_ERROR_THRESHOLD;
        this.systemProperties = System.getProperties();
        this.hostname = Utils.getHostName();
        this.retryPolicy = DEFAULT_RETRY_POLICY;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public long getSkip() {
        return skip;
    }

    public void setSkip(long skip) {
        this.skip = skip;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getErrorThreshold() {
        return errorThreshold;
    }

    public void setErrorThreshold(long errorThreshold) {
        this.errorThreshold = errorThreshold;
    }

    public boolean isSilentMode() {
        return silentMode;
    }

    public void setSilentMode(boolean silentMode) {
        this.silentMode = silentMode;
    }

    public boolean isJmxMonitoring() {
        return jmxMonitoring;
    }

    public void setJmxMonitoring(boolean jmxMonitoring) {
        this.jmxMonitoring = jmxMonitoring;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Properties getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Properties systemProperties) {
        this.systemProperties = systemProperties;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }
}
