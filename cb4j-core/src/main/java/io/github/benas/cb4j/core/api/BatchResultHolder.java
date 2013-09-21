package io.github.benas.cb4j.core.api;

/**
 * Batch execution result holder.
 *
 * @param <R> The result type.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public interface BatchResultHolder<R> {

    /**
     * Get batch execution result.
     *
     * @return batch result.
     */
    R getResult();

}
