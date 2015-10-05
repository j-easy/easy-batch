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
import org.easybatch.core.api.event.EventManager;
import org.easybatch.core.api.handler.ErrorRecordHandler;
import org.easybatch.core.api.handler.FilteredRecordHandler;
import org.easybatch.core.api.handler.RejectedRecordHandler;

import java.util.List;

/**
 * The processing pipeline is the list of stages to process a record.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
final class Pipeline {

    private List<RecordProcessor> processors;

    private ErrorRecordHandler errorRecordHandler;
    private FilteredRecordHandler filteredRecordHandler;
    private RejectedRecordHandler rejectedRecordHandler;

    private Report report;

    private EventManager eventManager;

    Pipeline(List<RecordProcessor> processors,
             Report report,
             EventManager eventManager,
             ErrorRecordHandler errorRecordHandler,
             RejectedRecordHandler rejectedRecordHandler,
             FilteredRecordHandler filteredRecordHandler) {
        this.processors = processors;
        this.errorRecordHandler = errorRecordHandler;
        this.rejectedRecordHandler = rejectedRecordHandler;
        this.filteredRecordHandler = filteredRecordHandler;
        this.report = report;
        this.eventManager = eventManager;
    }

    @SuppressWarnings({"unchecked"})
    public boolean process(Record currentRecord) {

        boolean processingError = false;
        Object processingResult = null;
        try {
            Object recordToProcess = eventManager.fireBeforeRecordProcessing(currentRecord);
            for (RecordProcessor processor : processors) {
                recordToProcess = processor.processRecord(recordToProcess);
            }
            RecordProcessor lastRecordProcessor = getLastProcessor();
            if (lastRecordProcessor != null && lastRecordProcessor instanceof ComputationalRecordProcessor) {
                processingResult = ((ComputationalRecordProcessor) lastRecordProcessor).getComputationResult();
            }
            report.incrementTotalSuccessRecord();
            eventManager.fireAfterRecordProcessing(recordToProcess, processingResult);
        } catch (RecordFilteringException e) {
            filteredRecordHandler.handle(currentRecord, e);
            report.incrementTotalFilteredRecords();
        } catch (RecordValidationException e) {
            rejectedRecordHandler.handle(currentRecord, e);
            report.incrementTotalRejectedRecord();
        } catch (Exception e) {
            processingError = true;
            errorRecordHandler.handle(currentRecord, e);
            report.incrementTotalErrorRecord();
            eventManager.fireOnRecordProcessingException(currentRecord, e);
        }
        return processingError;
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

    void setErrorRecordHandler(ErrorRecordHandler errorRecordHandler) {
        this.errorRecordHandler = errorRecordHandler;
    }

    public void setFilteredRecordHandler(FilteredRecordHandler filteredRecordHandler) {
        this.filteredRecordHandler = filteredRecordHandler;
    }

    public void setRejectedRecordHandler(RejectedRecordHandler rejectedRecordHandler) {
        this.rejectedRecordHandler = rejectedRecordHandler;
    }
}
