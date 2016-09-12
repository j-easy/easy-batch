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

package org.easybatch.core.retry;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Retry policy for a retryable code.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class RetryPolicy implements Serializable {

    private int maxAttempts;

    private long delay;

    private TimeUnit timeUnit;

    /**
     * Create a new {@link RetryPolicy}.
     *
     * @param maxAttempts number of retries
     * @param delay to wait between retries
     * @param timeUnit of the delay to wait between retries
     */
    public RetryPolicy(final int maxAttempts, final long delay, final TimeUnit timeUnit) {
        this.maxAttempts = maxAttempts;
        this.delay = delay;
        this.timeUnit = timeUnit;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public long getDelay() {
        return delay;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public String toString() {
        return "{ maxAttempts = " + maxAttempts + ", delay = " + delay + " " + timeUnit + " }";
    }
}
