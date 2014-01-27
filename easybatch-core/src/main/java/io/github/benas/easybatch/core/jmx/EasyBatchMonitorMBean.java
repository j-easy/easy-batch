/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.core.jmx;

/**
 *  Easy batch JMX MBean interface.
 *
 *  @author benas (md.benhassine@gmail.com)
 */
public interface EasyBatchMonitorMBean {

    /**
     * Get the data source name.
     * @return the data source name
     */
    String getDataSource();

    /**
     * Get the current record number being processed by the engine.
     * @return the current record number
     */
    long getCurrentRecordNumber();

    /**
     * Get total input records number.
     * @return total input records number
     */
    String getTotalRecords();

    /**
     * Get filtered records number.
     * @return filtered records number
     */
    String getFilteredRecords();

    /**
     * Get ignored records number.
     * @return ignored records number
     */
    String getIgnoredRecords();

    /**
     * Get rejected records number.
     * @return rejected records number
     */
    String getRejectedRecords();

    /**
     * Get error records number.
     * @return error records number
     */
    String getErrorRecords();

    /**
     * Get processed records number.
     * @return processed records number
     */
    String getProcessedRecords();

    /**
     * Get batch execution start time.
     * @return batch execution start time
     */
    String getStartTime();

    /**
     * Get batch execution end time.
     * @return batch execution end time
     */
    String getEndTime();

    /**
     * Get a human readable batch execution progress.
     * @return a human readable batch execution progress
     */
    String getProgress();

}
