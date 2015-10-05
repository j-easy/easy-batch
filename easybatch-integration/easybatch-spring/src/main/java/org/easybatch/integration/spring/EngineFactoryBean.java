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

package org.easybatch.integration.spring;

import org.easybatch.core.api.Engine;
import org.easybatch.core.api.RecordProcessor;
import org.easybatch.core.api.RecordReader;
import org.easybatch.core.api.event.EventManager;
import org.easybatch.core.api.event.JobEventListener;
import org.easybatch.core.api.event.PipelineEventListener;
import org.easybatch.core.api.event.RecordReaderEventListener;
import org.easybatch.core.api.handler.ErrorRecordHandler;
import org.easybatch.core.api.handler.FilteredRecordHandler;
import org.easybatch.core.api.handler.RejectedRecordHandler;
import org.easybatch.core.impl.EngineBuilder;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

/**
 * Spring Factory Bean that creates engine instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class EngineFactoryBean implements FactoryBean {

    private RecordReader recordReader;

    private List<RecordProcessor> processingPipeline;

    private FilteredRecordHandler filteredRecordHandler;

    private RejectedRecordHandler rejectedRecordHandler;

    private ErrorRecordHandler errorRecordHandler;

    private List<JobEventListener> jobEventListeners;

    private List<RecordReaderEventListener> recordReaderEventListeners;

    private List<PipelineEventListener> pipelineEventListeners;

    private EventManager eventManager;

    @Override
    public Engine getObject() throws Exception {
        EngineBuilder engineBuilder = new EngineBuilder();

        registerMainComponents(engineBuilder);

        registerCustomHandlers(engineBuilder);

        registerCustomEventListeners(engineBuilder);

        return engineBuilder.build();
    }

    private void registerMainComponents(EngineBuilder engineBuilder) {
        if (recordReader != null) {
            engineBuilder.reader(recordReader);
        }
        if (processingPipeline != null) {
            for (RecordProcessor recordProcessor : processingPipeline) {
                engineBuilder.processor(recordProcessor);
            }
        }
    }

    private void registerCustomEventListeners(EngineBuilder engineBuilder) {
        if (jobEventListeners != null) {
            for (JobEventListener jobEventListener : jobEventListeners) {
                engineBuilder.jobEventListener(jobEventListener);
            }
        }

        if (recordReaderEventListeners != null) {
            for (RecordReaderEventListener recordReaderEventListener : recordReaderEventListeners) {
                engineBuilder.recordReaderEventListener(recordReaderEventListener);
            }
        }

        if (pipelineEventListeners != null) {
            for (PipelineEventListener pipelineEventListener : pipelineEventListeners) {
                engineBuilder.pipelineEventListener(pipelineEventListener);
            }
        }

        if (eventManager != null) {
            engineBuilder.eventManager(eventManager);
        }
    }

    private void registerCustomHandlers(EngineBuilder engineBuilder) {
        if (filteredRecordHandler != null) {
            engineBuilder.filteredRecordHandler(filteredRecordHandler);
        }

        if (rejectedRecordHandler != null) {
            engineBuilder.rejectedRecordHandler(rejectedRecordHandler);
        }

        if (errorRecordHandler != null) {
            engineBuilder.errorRecordHandler(errorRecordHandler);
        }
    }

    @Override
    public Class<Engine> getObjectType() {
        return Engine.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /* Setters for dependency injection */

    public void setRecordReader(RecordReader recordReader) {
        this.recordReader = recordReader;
    }

    public void setProcessingPipeline(List<RecordProcessor> processingPipeline) {
        this.processingPipeline = processingPipeline;
    }

    public void setFilteredRecordHandler(FilteredRecordHandler filteredRecordHandler) {
        this.filteredRecordHandler = filteredRecordHandler;
    }


    public void setRejectedRecordHandler(RejectedRecordHandler rejectedRecordHandler) {
        this.rejectedRecordHandler = rejectedRecordHandler;
    }

    public void setErrorRecordHandler(ErrorRecordHandler errorRecordHandler) {
        this.errorRecordHandler = errorRecordHandler;
    }

    public void setJobEventListeners(List<JobEventListener> jobEventListeners) {
        this.jobEventListeners = jobEventListeners;
    }

    public void setRecordReaderEventListeners(List<RecordReaderEventListener> recordReaderEventListeners) {
        this.recordReaderEventListeners = recordReaderEventListeners;
    }

    public void setPipelineEventListeners(List<PipelineEventListener> pipelineEventListeners) {
        this.pipelineEventListeners = pipelineEventListeners;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
}
