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
import org.easybatch.core.api.listener.JobListener;
import org.easybatch.core.api.listener.PipelineListener;
import org.easybatch.core.api.listener.RecordReaderListener;
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

    private List<JobListener> jobListeners;

    private List<RecordReaderListener> recordReaderListeners;

    private List<PipelineListener> pipelineListeners;

    @Override
    public Engine getObject() throws Exception {
        EngineBuilder engineBuilder = new EngineBuilder();

        registerMainComponents(engineBuilder);

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
        if (jobListeners != null) {
            for (JobListener jobListener : jobListeners) {
                engineBuilder.jobEventListener(jobListener);
            }
        }

        if (recordReaderListeners != null) {
            for (RecordReaderListener recordReaderListener : recordReaderListeners) {
                engineBuilder.readerEventListener(recordReaderListener);
            }
        }

        if (pipelineListeners != null) {
            for (PipelineListener pipelineListener : pipelineListeners) {
                engineBuilder.pipelineEventListener(pipelineListener);
            }
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

    public void setJobListeners(List<JobListener> jobListeners) {
        this.jobListeners = jobListeners;
    }

    public void setRecordReaderListeners(List<RecordReaderListener> recordReaderListeners) {
        this.recordReaderListeners = recordReaderListeners;
    }

    public void setPipelineListeners(List<PipelineListener> pipelineListeners) {
        this.pipelineListeners = pipelineListeners;
    }
    
}
