/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.jmx;

/**
 * JMX MBean interface to expose monitoring attributes.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public interface MonitorMBean {

    /**
     * Get the engine name.
     * 
     * @return the engine name
     */
    String getEngineName();

    /**
     * Get the job execution id.
     * 
     * @return the job execution id
     */
    String getExecutionId();
    
    /**
     * Get the data source name.
     *
     * @return the data source name
     */
    String getDataSource();

    /**
     * Get the current record number being processed by the engine.
     *
     * @return the current record number
     */
    long getCurrentRecordNumber();

    /**
     * Get total input records count.
     *
     * @return total input records count
     */
    String getTotalRecords();

    /**
     * Get skipped records count.
     *
     * @return skipped records count
     */
    String getSkippedRecords();

    /**
     * Get filtered records count.
     *
     * @return filtered records count
     */
    String getFilteredRecords();

    /**
     * Get ignored records count.
     *
     * @return ignored records count
     */
    String getIgnoredRecords();

    /**
     * Get rejected records count.
     *
     * @return rejected records count
     */
    String getRejectedRecords();

    /**
     * Get error records count.
     *
     * @return error records count
     */
    String getErrorRecords();

    /**
     * Get success records count.
     *
     * @return success records count
     */
    String getSuccessRecords();

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
     * Get a human readable batch execution progress.
     *
     * @return a human readable batch execution progress
     */
    String getProgress();

    /**
     * Get engine {@link org.easybatch.core.api.Status}.
     *
     * @return engine status
     */
    String getStatus();

}
