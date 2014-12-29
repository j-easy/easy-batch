/*
 * The MIT License
 *
 *   Copyright (c) 2014, Mahmoud Ben Hassine (md.benhassine@gmail.com)
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
import org.easybatch.core.api.event.global.BatchProcessEventListener;
import org.easybatch.core.api.event.record.*;

/**
 * Easy batch engine instance builder.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public final class EasyBatchEngineBuilder {

    /**
     * The easy batch engine to build.
     */
    private EasyBatchEngine easyBatchEngine;

    public EasyBatchEngineBuilder() {
        RecordReader recordReader = new NoOpRecordReader();
        RecordFilter recordFilter = new NoOpRecordFilter();
        RecordMapper recordMapper = new NoOpRecordMapper();
        RecordValidator recordValidator = new NoOpRecordValidator();
        RecordProcessor recordProcessor = new NoOpRecordProcessor();
        IgnoredRecordHandler ignoredRecordHandler = new NoOpIgnoredRecordHandler();
        RejectedRecordHandler rejectedRecordHandler = new NoOpRejectedRecordHandler();
        ErrorRecordHandler errorRecordHandler = new NoOpErrorRecordHandler();
        easyBatchEngine = new EasyBatchEngine(recordReader, recordFilter, recordMapper, recordValidator, recordProcessor, ignoredRecordHandler, rejectedRecordHandler, errorRecordHandler);
    }

    /**
     * Register a record filter.
     * @param recordFilter the record filter to register
     * @return the engine builder
     */
    public EasyBatchEngineBuilder registerRecordFilter(final RecordFilter recordFilter) {
        easyBatchEngine.setRecordFilter(recordFilter);
        return this;
    }

    /**
     * Register a record reader.
     * @param recordReader the record reader to register
     * @return the engine builder
     */
    public EasyBatchEngineBuilder registerRecordReader(final RecordReader recordReader) {
        easyBatchEngine.setRecordReader(recordReader);
        return this;
    }

    /**
     * Register a record mapper.
     * @param recordMapper the record mapper to register
     * @return the engine builder
     */
    public EasyBatchEngineBuilder registerRecordMapper(final RecordMapper recordMapper) {
        easyBatchEngine.setRecordMapper(recordMapper);
        return this;
    }

    /**
     * Register a record validator.
     * @param recordValidator the record validator to register
     * @return the engine builder
     */
    public EasyBatchEngineBuilder registerRecordValidator(final RecordValidator recordValidator) {
        easyBatchEngine.setRecordValidator(recordValidator);
        return this;
    }

    /**
     * Register a record processor.
     * @param recordProcessor the record processor to register
     * @return the engine builder
     */
    public EasyBatchEngineBuilder registerRecordProcessor(final RecordProcessor recordProcessor) {
        easyBatchEngine.setRecordProcessor(recordProcessor);
        return this;
    }

    /**
     * Register a ignored record handler.
     * @param ignoredRecordHandler the handler to process ignored record
     * @return the engine builder
     */
    public EasyBatchEngineBuilder registerIgnoredRecordHandler(final IgnoredRecordHandler ignoredRecordHandler) {
        easyBatchEngine.setIgnoredRecordHandler(ignoredRecordHandler);
        return this;
    }
    
    /**
     * Register a rejected record handler.
     * @param rejectedRecordHandler the handler to process rejected record
     * @return the engine builder
     */
    public EasyBatchEngineBuilder registerRejectedRecordHandler(final RejectedRecordHandler rejectedRecordHandler) {
        easyBatchEngine.setRejectedRecordHandler(rejectedRecordHandler);
        return this;
    }
    
    /**
     * Register a rejected record handler.
     * @param errorRecordHandler the handler to process error record
     * @return the engine builder
     */
    public EasyBatchEngineBuilder registerErrorRecordHandler(final ErrorRecordHandler errorRecordHandler) {
        easyBatchEngine.setErrorRecordHandler(errorRecordHandler);
        return this;
    }
    
    /**
     * Enable strict mode : if true, then the execution will be aborted on first mapping, validating or processing error.
     * @param strictMode true if strict mode should be enabled
     * @return the engine builder
     */
    public EasyBatchEngineBuilder enableStrictMode(final boolean strictMode) {
        easyBatchEngine.setStrictMode(strictMode);
        return this;
    }

    /**
     * Register a batch process event listener.
     * See {@link org.easybatch.core.api.event.global.BatchProcessEventListener} for available callback methods.
     *
     * @param eventListener The event listener to add.
     * @return the engine builder
     */
    public EasyBatchEngineBuilder addBatchProcessEventListener(final BatchProcessEventListener eventListener) {
        easyBatchEngine.getEventManager().addBatchProcessListener(eventListener);
        return this;
    }

    /**
     * Register a batch process event listener.
     * See {@link org.easybatch.core.api.event.record.RecordReaderEventListener} for available callback methods.
     *
     * @param eventListener The event listener to add.
     * @return the engine builder
     */
    public EasyBatchEngineBuilder addRecordReaderEventListener(final RecordReaderEventListener eventListener) {
        easyBatchEngine.getEventManager().addRecordReaderListener(eventListener);
        return this;
    }

    /**
     * Register a batch process event listener.
     * See {@link org.easybatch.core.api.event.record.RecordFilterEventListener} for available callback methods.
     *
     * @param eventListener The event listener to add.
     * @return the engine builder
     */
    public EasyBatchEngineBuilder addRecordFilterEventListener(final RecordFilterEventListener eventListener) {
        easyBatchEngine.getEventManager().addRecordFilterEventListener(eventListener);
        return this;
    }

    /**
     * Register a batch process event listener.
     * See {@link org.easybatch.core.api.event.record.RecordMapperEventListener} for available callback methods.
     *
     * @param eventListener The event listener to add.
     * @return the engine builder
     */
    public EasyBatchEngineBuilder addRecordMapperEventListener(final RecordMapperEventListener eventListener) {
        easyBatchEngine.getEventManager().addRecordMapperListener(eventListener);
        return this;
    }

    /**
     * Register a batch process event listener.
     * See {@link org.easybatch.core.api.event.record.RecordValidatorEventListener} for available callback methods.
     *
     * @param eventListener The event listener to add.
     * @return the engine builder
     */
    public EasyBatchEngineBuilder addRecordValidatorEventListener(final RecordValidatorEventListener eventListener) {
        easyBatchEngine.getEventManager().addRecordValidatorEventListener(eventListener);
        return this;
    }

    /**
     * Register a batch process event listener.
     * See {@link org.easybatch.core.api.event.record.RecordProcessorEventListener} for available callback methods.
     *
     * @param eventListener The event listener to add.
     * @return the engine builder
     */
    public EasyBatchEngineBuilder addRecordProcessorEventListener(final RecordProcessorEventListener eventListener) {
        easyBatchEngine.getEventManager().addRecordProcessorEventListener(eventListener);
        return this;
    }

    /**
     * Build an Easy Batch engine instance.
     * @return an Easy Batch instance
     */
    public EasyBatchEngine build() {
        return easyBatchEngine;
    }
}
