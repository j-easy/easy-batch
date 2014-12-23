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

import java.util.ArrayList;
import java.util.List;

/**
 * Easy batch engine instance builder.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public final class EngineBuilder {

    /**
     * The easy batch engine to build.
     */
    private Engine engine;

    public EngineBuilder() {
        RecordReader recordReader = new NoOpRecordReader();
        RecordFilter recordFilter = new NoOpRecordFilter();
        RecordMapper recordMapper = new NoOpRecordMapper();
        RecordValidator recordValidator = new NoOpRecordValidator();
        List<RecordProcessor> processingPipeline = new ArrayList<RecordProcessor>();
        processingPipeline.add(new NoOpRecordProcessor());
        IgnoredRecordHandler ignoredRecordHandler = new NoOpIgnoredRecordHandler();
        RejectedRecordHandler rejectedRecordHandler = new NoOpRejectedRecordHandler();
        ErrorRecordHandler errorRecordHandler = new NoOpErrorRecordHandler();
        engine = new Engine(recordReader, recordFilter, recordMapper, recordValidator, processingPipeline, ignoredRecordHandler, rejectedRecordHandler, errorRecordHandler);
    }

    /**
     * Register a record filter.
     * @param recordFilter the record filter to register
     * @return the engine builder
     */
    public EngineBuilder registerRecordFilter(final RecordFilter recordFilter) {
        engine.setRecordFilter(recordFilter);
        return this;
    }

    /**
     * Alias for {@link EngineBuilder#registerRecordFilter(org.easybatch.core.api.RecordFilter)}
     * @param recordFilter the record filter to register
     * @return the engine builder
     */
    public EngineBuilder filterRecordsWith(final RecordFilter recordFilter) {
        return registerRecordFilter(recordFilter);
    }

    /**
     * Register a record reader.
     * @param recordReader the record reader to register
     * @return the engine builder
     */
    public EngineBuilder registerRecordReader(final RecordReader recordReader) {
        engine.setRecordReader(recordReader);
        return this;
    }

    /**
     * Alias for {@link EngineBuilder#registerRecordReader(org.easybatch.core.api.RecordReader)}
     * @param recordReader the record reader to register
     * @return the engine builder
     */
    public EngineBuilder readRecordsWith(final RecordReader recordReader) {
        return registerRecordReader(recordReader);
    }

    /**
     * Register a record mapper.
     * @param recordMapper the record mapper to register
     * @return the engine builder
     */
    public EngineBuilder registerRecordMapper(final RecordMapper recordMapper) {
        engine.setRecordMapper(recordMapper);
        return this;
    }

    /**
     * Alias for {@link EngineBuilder#registerRecordMapper(org.easybatch.core.api.RecordMapper)}
     * @param recordMapper the record mapper to register
     * @return the engine builder
     */
    public EngineBuilder mapRecordsWith(final RecordMapper recordMapper) {
        return registerRecordMapper(recordMapper);
    }

    /**
     * Register a record validator.
     * @param recordValidator the record validator to register
     * @return the engine builder
     */
    public EngineBuilder registerRecordValidator(final RecordValidator recordValidator) {
        engine.setRecordValidator(recordValidator);
        return this;
    }

    /**
     * Alias for {@link EngineBuilder#registerRecordValidator(org.easybatch.core.api.RecordValidator)}
     * @param recordValidator the record validator to register
     * @return the engine builder
     */
    public EngineBuilder validateRecordsWith(final RecordValidator recordValidator) {
        return registerRecordValidator(recordValidator);
    }

    /**
     * Register a record processor.
     * @param recordProcessor the record processor to register
     * @return the engine builder
     */
    public EngineBuilder registerRecordProcessor(final RecordProcessor recordProcessor) {
        engine.addRecordProcessor(recordProcessor);
        return this;
    }

    /**
     * Alias for {@link EngineBuilder#registerRecordProcessor(org.easybatch.core.api.RecordProcessor)}
     * @param recordProcessor the record processor to register
     * @return the engine builder
     */
    public EngineBuilder processRecordsWith(final RecordProcessor recordProcessor) {
        return registerRecordProcessor(recordProcessor);
    }

    /**
     * Alias for {@link EngineBuilder#registerRecordProcessor(org.easybatch.core.api.RecordProcessor)}
     * @param recordProcessor the record processor to register
     * @return the engine builder
     */
    public EngineBuilder thenWith(final RecordProcessor recordProcessor) {
        return registerRecordProcessor(recordProcessor);
    }

    /**
     * Register a ignored record handler.
     * @param ignoredRecordHandler the handler to process ignored record
     * @return the engine builder
     */
    public EngineBuilder registerIgnoredRecordHandler(final IgnoredRecordHandler ignoredRecordHandler) {
        engine.setIgnoredRecordHandler(ignoredRecordHandler);
        return this;
    }

    /**
     * Alias for {@link EngineBuilder#registerIgnoredRecordHandler(org.easybatch.core.api.IgnoredRecordHandler)}
     * @param ignoredRecordHandler the handler to process ignored record
     * @return the engine builder
     */
    public EngineBuilder reportIgnoredRecordsWith(final IgnoredRecordHandler ignoredRecordHandler) {
        return registerIgnoredRecordHandler(ignoredRecordHandler);
    }
    
    /**
     * Register a rejected record handler.
     * @param rejectedRecordHandler the handler to process rejected record
     * @return the engine builder
     */
    public EngineBuilder registerRejectedRecordHandler(final RejectedRecordHandler rejectedRecordHandler) {
        engine.setRejectedRecordHandler(rejectedRecordHandler);
        return this;
    }

    /**
     * Alias for {@link EngineBuilder#registerRejectedRecordHandler(org.easybatch.core.api.RejectedRecordHandler)}
     * @param rejectedRecordHandler the handler to process rejected record
     * @return the engine builder
     */
    public EngineBuilder reportRejectedRecordsWith(final RejectedRecordHandler rejectedRecordHandler) {
        return registerRejectedRecordHandler(rejectedRecordHandler);
    }
    
    /**
     * Register a error record handler.
     * @param errorRecordHandler the handler to process error record
     * @return the engine builder
     */
    public EngineBuilder registerErrorRecordHandler(final ErrorRecordHandler errorRecordHandler) {
        engine.setErrorRecordHandler(errorRecordHandler);
        return this;
    }

    /**
     * Alias for {@link EngineBuilder#registerErrorRecordHandler(org.easybatch.core.api.ErrorRecordHandler)}
     * @param errorRecordHandler the handler to process error record
     * @return the engine builder
     */
    public EngineBuilder reportErrorRecordsWith(final ErrorRecordHandler errorRecordHandler) {
        return registerErrorRecordHandler(errorRecordHandler);
    }
    
    /**
     * Enable strict mode : if true, then the execution will be aborted on first mapping, validating or processing error.
     * @param strictMode true if strict mode should be enabled
     * @return the engine builder
     */
    public EngineBuilder enableStrictMode(final boolean strictMode) {
        engine.setStrictMode(strictMode);
        return this;
    }

    /**
     * Build an Easy Batch engine instance.
     * @return an Easy Batch instance
     */
    public Engine build() {
        return engine;
    }

}
