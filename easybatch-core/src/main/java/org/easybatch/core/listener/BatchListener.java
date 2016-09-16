package org.easybatch.core.listener;

import org.easybatch.core.record.Record;

import java.util.List;

/**
 * Allow implementing classes to get notified before/after each batch.
 * Exception handling is done by the implementing class.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public interface BatchListener {

    /**
     * Executed before processing each batch.
     */
    void beforeBatch();

    /**
     * Executed after processing each batch.
     *
     * @param records the batch of records that has been processed
     */
    void afterBatch(List<Record> records);

}
