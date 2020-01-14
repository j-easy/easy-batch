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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pipeline listener that calculates the processing time of a record.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 * @deprecated This class is deprecated since v5.3 and will be removed in v6.
 */
@Deprecated
public class RecordProcessingTimeListener implements PipelineListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordProcessingTimeListener.class.getName());

    private long recordNumber;

    private long startTime;

    @Override
    public Record beforeRecordProcessing(Record inputRecord) {
        recordNumber = inputRecord.getHeader().getNumber();
        startTime = System.currentTimeMillis();
        return inputRecord;
    }

    @Override
    public void afterRecordProcessing(Record inputRecord, Record outputRecord) {
        logProcessingTime();
    }

    @Override
    public void onRecordProcessingException(Record record, Throwable throwable) {
        logProcessingTime();
    }

    private void logProcessingTime() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        LOGGER.info("Record N° {} processing time = {}ms", recordNumber, elapsedTime);
    }
}
