/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.core.impl;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.Report;
import org.easybatch.core.api.Status;
import org.easybatch.core.api.listener.RecordReaderListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default listener that logs and reports record reading exceptions.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
class DefaultRecordReaderListener implements RecordReaderListener {

    private static final Logger LOGGER = Logger.getLogger(DefaultRecordReaderListener.class.getName());

    private Report report;

    DefaultRecordReaderListener(Report report) {
        this.report = report;
    }

    /**
     * Called before each record read operation.
     */
    @Override
    public void beforeRecordReading() {

    }

    /**
     * Called after each record read operation.
     *
     * @param record The record that has been read.
     */
    @Override
    public void afterRecordReading(Record record) {
        if (record == null) {
            LOGGER.log(Level.SEVERE, "The record reader returned null for next record, aborting execution");
            report.setStatus(Status.ABORTED);
            report.setEndTime(System.currentTimeMillis());
        }
    }

    /**
     * Called when an exception occurs during record reading.
     *
     * @param throwable the throwable thrown during record reading
     */
    @Override
    public void onRecordReadingException(Throwable throwable) {
        LOGGER.log(Level.SEVERE, "An exception occurred while reading next record, aborting execution", throwable);
        report.setStatus(Status.ABORTED);
        report.setEndTime(System.currentTimeMillis());
    }
}
