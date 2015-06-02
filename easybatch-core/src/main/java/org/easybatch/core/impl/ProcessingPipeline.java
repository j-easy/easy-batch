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
        eventManager.fireBeforeProcessingRecord(typedRecord);
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
        eventManager.fireAfterProcessingRecord(typedRecord, processingResult);
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
