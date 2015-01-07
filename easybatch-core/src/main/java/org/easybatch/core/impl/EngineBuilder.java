/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.impl;

import org.easybatch.core.api.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Easy batch engine instance builder.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public final class EngineBuilder {

    /**
     * The easy batch engine to build.
     */
    private Engine engine;

    public EngineBuilder() {

        RecordReader recordReader = new NoOpRecordReader();
        List<RecordFilter> filterChain = new ArrayList<RecordFilter>();
        filterChain.add(new NoOpRecordFilter());
        RecordMapper recordMapper = new NoOpRecordMapper();
        RecordValidator recordValidator = new NoOpRecordValidator();
        List<RecordProcessor> processingPipeline = new ArrayList<RecordProcessor>();
        processingPipeline.add(new NoOpRecordProcessor());
        FilteredRecordHandler filteredRecordHandler = new NoOpFilteredRecordHandler();
        IgnoredRecordHandler ignoredRecordHandler = new NoOpIgnoredRecordHandler();
        RejectedRecordHandler rejectedRecordHandler = new NoOpRejectedRecordHandler();
        ErrorRecordHandler errorRecordHandler = new NoOpErrorRecordHandler();
        engine = new Engine(
                        recordReader,
                        filterChain,
                        recordMapper,
                        recordValidator,
                        processingPipeline,
                        filteredRecordHandler,
                        ignoredRecordHandler,
                        rejectedRecordHandler,
                        errorRecordHandler);
    }

    /**
     * Register a record reader.
     * @param recordReader the record reader to register
     * @return the engine builder
     */
    public EngineBuilder reader(final RecordReader recordReader) {
        engine.setRecordReader(recordReader);
        return this;
    }

    /**
     * Register a record filter.
     * @param recordFilter the record filter to register
     * @return the engine builder
     */
    public EngineBuilder filter(final RecordFilter recordFilter) {
        engine.addRecordFilter(recordFilter);
        return this;
    }

    /**
     * Register a record mapper.
     * @param recordMapper the record mapper to register
     * @return the engine builder
     */
    public EngineBuilder mapper(final RecordMapper recordMapper) {
        engine.setRecordMapper(recordMapper);
        return this;
    }

    /**
     * Register a record validator.
     * @param recordValidator the record validator to register
     * @return the engine builder
     */
    public EngineBuilder validator(final RecordValidator recordValidator) {
        engine.setRecordValidator(recordValidator);
        return this;
    }

    /**
     * Register a record processor.
     * @param recordProcessor the record processor to register
     * @return the engine builder
     */
    public EngineBuilder processor(final RecordProcessor recordProcessor) {
        engine.addRecordProcessor(recordProcessor);
        return this;
    }

    /**
     * Register a filtered record handler.
     * @param filteredRecordHandler the handler to process filtered record
     * @return the engine builder
     */
    public EngineBuilder filteredRecordHandler(final FilteredRecordHandler filteredRecordHandler) {
        engine.setFilteredRecordHandler(filteredRecordHandler);
        return this;
    }

    /**
     * Register a ignored record handler.
     * @param ignoredRecordHandler the handler to process ignored record
     * @return the engine builder
     */
    public EngineBuilder ignoredRecordHandler(final IgnoredRecordHandler ignoredRecordHandler) {
        engine.setIgnoredRecordHandler(ignoredRecordHandler);
        return this;
    }
    
    /**
     * Register a rejected record handler.
     * @param rejectedRecordHandler the handler to process rejected record
     * @return the engine builder
     */
    public EngineBuilder rejectedRecordHandler(final RejectedRecordHandler rejectedRecordHandler) {
        engine.setRejectedRecordHandler(rejectedRecordHandler);
        return this;
    }
    
    /**
     * Register a error record handler.
     * @param errorRecordHandler the handler to process error record
     * @return the engine builder
     */
    public EngineBuilder errorRecordHandler(final ErrorRecordHandler errorRecordHandler) {
        engine.setErrorRecordHandler(errorRecordHandler);
        return this;
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
