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

import org.easybatch.core.record.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Composite listener that delegates processing to other listeners.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeRecordReaderListener implements RecordReaderListener {

    private List<RecordReaderListener> listeners;

    /**
     * Create a new {@link CompositeRecordReaderListener}.
     */
    public CompositeRecordReaderListener() {
        this(new ArrayList<RecordReaderListener>());
    }

    /**
     * Create a new {@link CompositeRecordReaderListener}.
     *
     * @param listeners delegates
     */
    public CompositeRecordReaderListener(List<RecordReaderListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void beforeRecordReading() {
        for (RecordReaderListener listener : listeners) {
            listener.beforeRecordReading();
        }
    }

    @Override
    public void afterRecordReading(Record record) {
        for (ListIterator<RecordReaderListener> iterator
                = listeners.listIterator(listeners.size());
                iterator.hasPrevious();) {
            iterator.previous().afterRecordReading(record);
        }
    }

    @Override
    public void onRecordReadingException(Throwable throwable) {
        for (ListIterator<RecordReaderListener> iterator
                = listeners.listIterator(listeners.size());
                iterator.hasPrevious();) {
            iterator.previous().onRecordReadingException(throwable);
        }
    }

    /**
     * Add a delegate listener.
     *
     * @param recordReaderListener to add
     */
    public void addRecordReaderListener(RecordReaderListener recordReaderListener) {
        listeners.add(recordReaderListener);
    }
}
