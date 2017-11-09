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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Metrics of a job.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobMetrics implements Serializable {

    private AtomicLong startTime = new AtomicLong();

    private AtomicLong endTime = new AtomicLong();

    private AtomicLong readCount = new AtomicLong();

    private AtomicLong writeCount = new AtomicLong();

    private AtomicLong filterCount = new AtomicLong();

    private AtomicLong errorCount = new AtomicLong();

    private ConcurrentHashMap<String, Object> customMetrics = new ConcurrentHashMap<>();

    /**
     * @deprecated use {@link JobMetrics#incrementFilterCount()}. This method will be removed in v5.3
     */
    @Deprecated
    public void incrementFilteredCount() {
        filterCount.addAndGet(1);
    }

    public void incrementFilterCount() {
        filterCount.addAndGet(1);
    }

    public void incrementErrorCount() {
        errorCount.addAndGet(1);
    }

    public void incrementReadCount() {
        readCount.addAndGet(1);
    }

    public void incrementWriteCount(long count) {
        writeCount.addAndGet(count);
    }

    public long getStartTime() {
        return startTime.get();
    }

    public void setStartTime(long startTime) {
        this.startTime.set(startTime);
    }

    public long getEndTime() {
        return endTime.get();
    }

    public void setEndTime(long endTime) {
        this.endTime.set(endTime);
    }

    public long getDuration() {
        return getEndTime() - getStartTime();
    }

    /**
     * @deprecated use {@link JobMetrics#getFilterCount()}. This method will be removed in v5.3
     */
    @Deprecated
    public long getFilteredCount() {
        return filterCount.get();
    }

    public long getFilterCount() {
        return filterCount.get();
    }

    public long getErrorCount() {
        return errorCount.get();
    }

    public long getReadCount() {
        return readCount.get();
    }

    public long getWriteCount() {
        return writeCount.get();
    }

    public void addMetric(String name, Object value) {
        customMetrics.put(name, value);
    }

    public Map<String, Object> getCustomMetrics() {
        return customMetrics;
    }
}
