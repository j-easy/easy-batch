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
package org.easybatch.core.job;

/**
 * JMX MBean interface to expose monitoring attributes.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public interface JobMonitorMBean {

    /**
     * Get the job name.
     *
     * @return the job name
     */
    String getJobName();

    /**
     * Get read records count.
     *
     * @return read records count
     */
    long getReadCount();

    /**
     * Get written records count.
     *
     * @return written records count
     */
    long getWriteCount();

    /**
     * Get filtered records count.
     *
     * @return filtered records count
     */
    long getFilterCount();

    /**
     * Get error records count.
     *
     * @return error records count
     */
    long getErrorCount();

    /**
     * Get batch execution start time.
     *
     * @return batch execution start time
     */
    String getStartTime();

    /**
     * Get batch execution end time.
     *
     * @return batch execution end time
     */
    String getEndTime();

    /**
     * Get the job {@link JobStatus}.
     *
     * @return the job status
     */
    String getJobStatus();

}
