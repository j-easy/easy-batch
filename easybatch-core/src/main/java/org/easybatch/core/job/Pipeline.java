/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core.job;

import org.easybatch.core.processor.RecordProcessingException;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.record.Record;

import java.util.List;

/**
 * The processing pipeline is the list of stages to process a record.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
final class Pipeline {

    private List<RecordProcessor> processors;

    private EventManager eventManager;

    Pipeline(List<RecordProcessor> processors, EventManager eventManager) {
        this.processors = processors;
        this.eventManager = eventManager;
    }

    @SuppressWarnings({"unchecked"})
    public void process(final Record currentRecord) throws RecordProcessingException {
        try {
            Record recordToProcess = eventManager.fireBeforeRecordProcessing(currentRecord);
            for (RecordProcessor processor : processors) {
                recordToProcess = processor.processRecord(recordToProcess);
                if (recordToProcess == null) {
                    break; // filter record
                }
            }
            eventManager.fireAfterRecordProcessing(currentRecord, recordToProcess);
        } catch (Exception e) {
            eventManager.fireOnRecordProcessingException(currentRecord, e);
            throw new RecordProcessingException(e);
        }
    }

    public RecordProcessor getLastProcessor() {
        if (!processors.isEmpty()) {
            return processors.get(processors.size() - 1);
        }
        return null;
    }

    public void addProcessor(RecordProcessor recordProcessor) {
        processors.add(recordProcessor);
    }

}
