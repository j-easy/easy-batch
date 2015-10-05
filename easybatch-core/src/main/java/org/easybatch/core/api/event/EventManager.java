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

package org.easybatch.core.api.event;

import org.easybatch.core.api.Record;

/**
 * Interface for the event manager.
 * <p>
 * There is one local implementation included. It could also be implemented using JMS or any other distributed
 * technology to get to know of any event happened while the job is running.
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
     * @param jobEventListener The listener to add.
     */
    void addJobEventListener(final JobEventListener jobEventListener);

    /**
     * Add an event listener.
     *
     * @param recordReaderEventListener The listener to add.
     */
    void addRecordReaderEventListener(final RecordReaderEventListener recordReaderEventListener);

    /**
     * Add a pipeline event listener.
     *
     * @param pipelineEventListener The listener to add.
     */
    void addPipelineEventListener(final PipelineEventListener pipelineEventListener);

    /**
     * Called before the whole batch starts.
     */
    void fireBeforeJobStart();

    /**
     * Called after the whole batch ends.
     */
    void fireAfterJobEnd();

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
     * Called before the record gets processed.
     *
     * @param record The record to be processed.
     */
    Object fireBeforeRecordProcessing(final Record record);

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
