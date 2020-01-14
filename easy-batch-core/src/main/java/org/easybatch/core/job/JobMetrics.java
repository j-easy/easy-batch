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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Metrics of a job.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobMetrics implements Serializable {

    private long startTime;

    private long endTime;

    private long readCount;

    private long writeCount;

    private long filterCount;

    private long errorCount;

    private Map<String, Object> customMetrics = new HashMap<>();

    public void incrementFilterCount() {
        filterCount++;
    }

    public void incrementFilterCount(long count) {
        filterCount += count;
    }

    public void incrementErrorCount() {
        errorCount++;
    }

    public void incrementErrorCount(long count) {
        errorCount += count;
    }

    public void incrementReadCount() {
        readCount++;
    }

    public void incrementReadCount(long count) {
        readCount += count;
    }

    public void incrementWriteCount(long count) {
        writeCount += count;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return getEndTime() - getStartTime();
    }

    public long getFilterCount() {
        return filterCount;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public long getReadCount() {
        return readCount;
    }

    public long getWriteCount() {
        return writeCount;
    }

    public void addMetric(String name, Object value) {
        customMetrics.put(name, value);
    }

    public Map<String, Object> getCustomMetrics() {
        return customMetrics;
    }
}
