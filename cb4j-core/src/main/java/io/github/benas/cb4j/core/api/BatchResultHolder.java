package io.github.benas.cb4j.core.api;

/**
 * Batch execution result holder.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public interface BatchResultHolder<R> {

    /**
     * Get batch execution result.
     */
    public R getResult();

}
