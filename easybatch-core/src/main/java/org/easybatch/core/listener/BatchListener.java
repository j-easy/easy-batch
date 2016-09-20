package org.easybatch.core.listener;

import org.easybatch.core.record.Batch;

/**
 * Allow implementing classes to get notified before/after processing each batch.
 * Exception handling is done by the implementing class.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public interface BatchListener {

    /**
     * Executed before reading each batch.
     */
    void beforeBatchReading();

    /**
     * Executed after processing each batch.
     *
     * @param batch the batch of records that has been processed
     */
    void afterBatchProcessing(final Batch batch);

    /**
     * Executed after successfully writing each batch.
     *
     * @param batch the batch of records that has been written
     */
    void afterBatchWriting(final Batch batch);

    /**
     * Executed when an error occurs during writing each batch.
     *
     * @param batch   the batch attempted to be written
     * @param throwable the error occurred
     */
    void onBatchWritingException(final Batch batch, Throwable throwable);

}
