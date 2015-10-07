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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The processing pipeline is the list of stages to process a record.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
final class Pipeline {

    private static final Logger LOGGER = Logger.getLogger(Pipeline.class.getName());

    private List<RecordProcessor> processors;

    private Report report;

    private EventManager eventManager;

    Pipeline(List<RecordProcessor> processors, Report report, EventManager eventManager) {
        this.processors = processors;
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
            LOGGER.log(Level.INFO, "Record {0} has been filtered", currentRecord);
            report.incrementTotalFilteredRecords();
            eventManager.fireOnRecordProcessingException(currentRecord, e);
        } catch (RecordValidationException e) {
            LOGGER.log(Level.SEVERE, "Record " + currentRecord + " has been rejected", e);
            report.incrementTotalRejectedRecord();
            eventManager.fireOnRecordProcessingException(currentRecord, e);
        } catch (Exception e) {
            processingError = true;
            LOGGER.log(Level.SEVERE, "An exception occurred while attempting to process record " + currentRecord, e);
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
    
}
