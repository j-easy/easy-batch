/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package org.easybatch.core.listener;

import org.easybatch.core.record.Batch;

/**
 * Enables the implementing class to get notified before/after writing a batch of records.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public interface RecordWriterListener {

    /**
     * Called before writing each batch of records.
     *
     * @param batch to be written
     */
    void beforeRecordWriting(Batch batch);

    /**
     * Called after writing each batch of records.
     *
     * @param batch of records that have been written.
     */
    void afterRecordWriting(Batch batch);

    /**
     * Called when an exception occurs during batch writing.
     *
     * @param batch attempted to be written
     * @param throwable the throwable thrown during record writing
     */
    void onRecordWritingException(Batch batch, final Throwable throwable);

}
