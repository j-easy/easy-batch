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
