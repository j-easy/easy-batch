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

package org.easybatch.core.api.event.batch;

/**
 * Enables the implementing class to get an event on different steps within the batch process.
 * <p/>
 * Use this interface when you want a listener that is not bound to a specific processor type, otherwise look at:
 * <ul>
 * <li>{@link org.easybatch.core.api.event.step.RecordReaderEventListener}: for pre/post reading events</li>
 * <li>{@link org.easybatch.core.api.event.step.RecordFilterEventListener}: for pre/post filtering events</li>
 * <li>{@link org.easybatch.core.api.event.step.RecordMapperEventListener}: for pre/post mapping events</li>
 * <li>{@link org.easybatch.core.api.event.step.RecordValidatorEventListener}: for pre/post validating events</li>
 * <li>{@link org.easybatch.core.api.event.step.RecordProcessorEventListener}: for pre/post processing events</li>
 * </ul>
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public interface BatchProcessEventListener {

    /**
     * Called before the {@link org.easybatch.core.api.RecordReader#open()} call.
     */
    void beforeBatchStart();

    /**
     * Called after the {@link org.easybatch.core.api.RecordReader#close()} call.
     */
    void afterBatchEnd();

    /**
     * Called on any throwable event while processing.
     * <p/>
     * There is no context information available, so this will probably something you want to use for logging
     * purposes or similar.
     *
     * @param throwable The exception thrown at the time of call.
     */
    void onBatchException(Throwable throwable);
}
