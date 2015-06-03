/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.api;

import org.easybatch.core.api.event.batch.BatchProcessEventListener;
import org.easybatch.core.api.event.step.*;

import java.util.Set;

/**
 * Interface for the event manager.
 * <p>
 * There is one local implementation included. It could also be implemented using JMS or any other distributed
 * technology to get to know of any event happened while the batch is being processed.
 * <p>
 * Please note that if you implement something like a global or singleton event handler you must care for synchronized
 * access for your self.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public interface EventManager {

    /**
     * Add an event listener.
     *
     * @param batchProcessEventListener The listener to add.
     */
    void addBatchProcessEventListener(final BatchProcessEventListener batchProcessEventListener);

    /**
     * Add an event listener.
     *
     * @param recordReaderEventListener The listener to add.
     */
    void addRecordReaderEventListener(final RecordReaderEventListener recordReaderEventListener);

    /**
     * Add an event listener.
     *
     * @param recordFilterEventListener The listener to add.
     */
    void addRecordFilterEventListener(final RecordFilterEventListener recordFilterEventListener);

    /**
     * Add an event listener.
     *
     * @param recordMapperEventListener The listener to add.
     */
    void addRecordMapperEventListener(final RecordMapperEventListener recordMapperEventListener);

    /**
     * Add an event listener.
     *
     * @param recordValidatorEventListener The listener to add.
     */
    void addRecordValidatorEventListener(final RecordValidatorEventListener recordValidatorEventListener);

    /**
     * Add an event listener.
     *
     * @param recordProcessorEventListener The listener to add.
     */
    void addRecordProcessorEventListener(final RecordProcessorEventListener recordProcessorEventListener);

    /**
     * Called before the whole batch starts.
     */
    void fireBeforeBatchStart();

    /**
     * Called after the whole batch ends.
     */
    void fireAfterBatchEnd();

    /**
     * Called on any exception thrown in the whole process.
     *
     * @param throwable The exception that has been thrown.
     */
    void fireOnBatchException(Throwable throwable);

    /**
     * Called before the reader opens
     */
    void fireBeforeReaderOpening();

    /**
     * Called after the reader has been opened.
     */
    void fireAfterReaderOpening();

    /**
     * Called before a record gets read.
     */
    void fireBeforeRecordReading();

    /**
     * Called after the record was read and returned.
     *
     * @param record The record that has been read.
     */
    void fireAfterRecordReading(final Record record);

    /**
     * Called on exception while reading the record
     *
     * @param throwable The exception happened
     */
    void fireOnRecordReadingException(Throwable throwable);

    /**
     * Called before the record reader has been closed.
     */
    void fireBeforeRecordReaderClosing();

    /**
     * Called after the record reader has been closed.
     */
    void fireAfterRecordReaderClosing();

    /**
     * Called before the record is passed to the filter.
     *
     * @param record The record to be filtered
     */
    void fireBeforeRecordFiltering(final Record record);

    /**
     * Called after the record was filtered.
     *
     * @param record   Can be null in case the record was filtered.
     * @param filtered Record identified for filtering?
     */
    void fireAfterRecordFiltering(final Record record, boolean filtered);

    /**
     * Called before the record is passed into the mapper.
     *
     * @param record The record that is going to be mapped.
     */
    void fireBeforeRecordMapping(final Record record);

    /**
     * Called after the mapping process.
     *
     * @param record       The record that came in.
     * @param mappedRecord The result that came out.
     */
    void fireAfterRecordMapping(final Record record, final Object mappedRecord);

    /**
     * Called before the mapped record gets validated.
     *
     * @param mappedRecord the mapped record.
     */
    void fireBeforeRecordValidation(final Object mappedRecord);

    /**
     * Called after the record is validated.
     *
     * @param validatedRecord  The validated record.
     * @param validationErrors The set of validation errors that came out.
     */
    void fireAfterRecordValidation(final Object validatedRecord, final Set<ValidationError> validationErrors);

    /**
     * Called before the record gets processed.
     *
     * @param record The record to be processed.
     */
    void fireBeforeRecordProcessing(final Object record);

    /**
     * Called after the processing is done.
     * <p>
     * Do not use this method to do additional work on the record or the result.
     *
     * @param record           The record that has been processed.
     * @param processingResult The processing result, if any. This can be a null reference.
     */
    void fireAfterRecordProcessing(final Object record, final Object processingResult);

    /**
     * Called when an exception occurs during record processing
     *
     * @param record    the current processed record
     * @param throwable the exception occurred during record processing
     */
    void fireOnRecordProcessingException(final Object record, final Throwable throwable);
}
