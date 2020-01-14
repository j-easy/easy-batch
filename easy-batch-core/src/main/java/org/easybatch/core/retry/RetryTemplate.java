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
package org.easybatch.core.retry;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Support class providing a template method for a retryable code with a retry policy.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class RetryTemplate {

    protected RetryPolicy retryPolicy;

    /**
     * Create a new {@link RetryTemplate}.
     *
     * @param retryPolicy the retry policy to apply
     */
    public RetryTemplate(final RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    /**
     * Execute the callable with retries.
     *
     * @param callable to execute
     * @param <T> the return type of the callable
     * @return the result of the callable
     * @throws Exception if the callable still throw an exception after all retries
     */
    public <T> T execute(final Callable<T> callable) throws Exception {
        int attempts = 0;
        int maxAttempts = retryPolicy.getMaxAttempts();
        long delay = retryPolicy.getDelay();
        TimeUnit timeUnit = retryPolicy.getTimeUnit();
        while(attempts < maxAttempts) {
            try {
                attempts++;
                beforeCall();
                T result = callable.call();
                afterCall(result);
                return result;
            } catch (Exception e) {
                onException(e);
                if (attempts >= maxAttempts) {
                    onMaxAttempts(e);
                    throw e;
                }
                beforeWait();
                sleep(timeUnit.toMillis(delay));
                afterWait();
            }
        }
        return null;
    }

    /**
     * Called before calling the callable.
     */
    protected abstract void beforeCall();

    /**
     * Called after calling the callable.
     *
     * @param result the result of the callable
     */
    protected abstract void afterCall(final Object result);

    /**
     * Called whenever the callable throw an exception.
     *
     * @param exception thrown by the callable
     */
    protected abstract void onException(final Exception exception);

    /**
     * Called whenever all retries have been executed.
     *
     * @param exception thrown by the callable on the last attempt
     */
    protected abstract void onMaxAttempts(final Exception exception);

    /**
     * Called before waiting for next attempt.
     */
    protected abstract void beforeWait();

    /**
     * Called after waiting for next attempt.
     */
    protected abstract void afterWait();

}
