package org.easybatch.core.listener;

import org.easybatch.core.record.Record;

import java.util.List;

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
     * @param records the batch of records that has been processed
     */
    void afterBatchProcessing(List<Record> records);

    /**
     * Executed after successfully writing each batch.
     *
     * @param records the batch of records that has been written
     */
    void afterBatchWriting(List<Record> records);

    /**
     * Executed when an error occurs during writing each batch.
     *
     * @param records   the batch attempted to be written
     * @param throwable the error occurred
     */
    void onBatchWritingException(List<Record> records, Throwable throwable);

}
