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

import org.easybatch.core.api.event.global.BatchProcessEventListener;
import org.easybatch.core.api.event.record.*;

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
     * @param batchProcessEventListener The listener to add.
     */
    public void addBatchProcessListener(final BatchProcessEventListener batchProcessEventListener);

    /**
     * Add an event listener.
     * @param recordReaderEventListener The listener to add.
     */
    public void addRecordReaderEventListener(final RecordReaderEventListener recordReaderEventListener);

    /**
     * Add an event listener.
     * @param recordFilterEventListener The listener to add.
     */
    public void addRecordFilterEventListener(final RecordFilterEventListener recordFilterEventListener);

    /**
     * Add an event listener.
     * @param recordMapperEventListener The listener to add.
     */
    public void addRecordMapperEventListener(final RecordMapperEventListener recordMapperEventListener);

    /**
     * Add an event listener.
     * @param recordValidatorEventListener The listener to add.
     */
    public void addRecordValidatorEventListener(final RecordValidatorEventListener recordValidatorEventListener);

    /**
     * Add an event listener.
     * @param recordProcessorEventListener The listener to add.
     */
    public void addRecordProcessorEventListener(final RecordProcessorEventListener recordProcessorEventListener);

    /**
     * Called before the whole batch starts.
     */
    public void fireBeforeBatchStart();

    /**
     * Called after the whole batch ends.
     */
    public void fireAfterBatchEnd();

    /**
     * Called on any exception thrown in the whole process.
     * @param t The exception that has been thrown.
     */
    public void fireOnBatchException(Throwable t);

    /**
     * Called before the reader opens
     */
    public void fireBeforeReaderOpen();

    /**
     * Called after the reader has been opened.
     */
    public void fireAfterReaderOpen();

    /**
     * Called before a record gets read.
     */
    public void fireBeforeRecordRead();

    /**
     * Called after the record was read and returned.
     * @param record The record that has been read.
     */
    public void fireAfterRecordRead(final Record record);

    /**
     * Called on exception while reading the record
     * @param throwable The exception happened
     */
    public void fireOnRecordReadException(Throwable throwable);

    /**
     * Called before the record reader has been closed.
     */
    public void fireBeforeRecordReaderClose();

    /**
     * Called after the record reader has been closed.
     */
    public void fireAfterRecordReaderClose();

    /**
     * Called before the record is passed to the filter.
     * @param record The record to be filtered
     */
    public void fireBeforeFilterRecord(final Record record);

    /**
     * Called after the record was filtered.
     * @param record Can be null in case the record was filtered.
     * @param filterRecord Record identified for filtering?
     */
    public void fireAfterFilterRecord(final Record record, boolean filterRecord);

    /**
     * Called before the record is passed into the mapper.
     * @param record The record that is going to be mapped.
     */
    public void fireBeforeMapRecord(final Record record);

    /**
     * Called after the mapping process.
     * @param record The record that came in.
     * @param mapResult The result that came out.
     */
    public void fireAfterMapRecord(final Record record, final Object mapResult);

    /**
     * Called before the mapped record gets validated.
     * @param mappedRecord the mapped record.
     */
    public void fireBeforeValidateRecord(final Object mappedRecord);

    /**
     * Called after the record is validated.
     * @param validatedRecord The validated record.
     * @param validationErrors The set of validation errors that came out.
     */
    public void fireAfterValidateRecord(final Object validatedRecord, final Set<ValidationError> validationErrors);

    /**
     * Called before the record gets processed.
     * @param record The record to be processed.
     */
    public void fireBeforeProcessRecord(final Object record);

    /**
     * Called after the processing is done.
     *
     * Do not use this method to do additional work on the record or the result.
     *
     * @param record The record that has been processed.
     * @param processResult The processing result, if any. This can be a null reference.
     */
    public void fireAfterRecordProcessed(final Object record, final Object processResult);
}
