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

import org.easybatch.core.api.*;

import java.util.List;

/**
 * The processing pipeline is the set of stages to process a record.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
final class ProcessingPipeline {

    private List<RecordProcessor> processors;

    private ErrorRecordHandler errorRecordHandler;

    private Report report;

    private EventManager eventManager;

    ProcessingPipeline(List<RecordProcessor> processors, ErrorRecordHandler errorRecordHandler, Report report, EventManager eventManager) {
        this.processors = processors;
        this.errorRecordHandler = errorRecordHandler;
        this.report = report;
        this.eventManager = eventManager;
    }

    @SuppressWarnings({"unchecked"})
    public boolean process(Record currentRecord, Object typedRecord) {

        boolean processingError = false;
        Object processingResult = null;
        eventManager.fireBeforeRecordProcessing(typedRecord);
        for (RecordProcessor recordProcessor : processors) {
            try {
                typedRecord = recordProcessor.processRecord(typedRecord);
                if (recordProcessor instanceof ComputationalRecordProcessor) {
                    processingResult = ((ComputationalRecordProcessor) recordProcessor).getComputationResult();
                }
            } catch (Exception e) {
                processingError = true;
                report.incrementTotalErrorRecord();
                errorRecordHandler.handle(currentRecord, e);
                eventManager.fireOnBatchException(e);
                eventManager.fireOnRecordProcessingException(typedRecord, e);
                break;
            }
        }
        eventManager.fireAfterRecordProcessing(typedRecord, processingResult);
        return processingError;
    }

    public RecordProcessor getLastProcessor() {
        return processors.get(processors.size() - 1);
    }

    public void addProcessor(RecordProcessor recordProcessor) {
        processors.add(recordProcessor);
    }

    void setErrorRecordHandler(ErrorRecordHandler errorRecordHandler) {
        this.errorRecordHandler = errorRecordHandler;
    }

}
