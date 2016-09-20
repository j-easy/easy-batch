/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.extensions.spring;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.listener.RecordReaderListener;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Spring Factory Bean that creates job instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobFactoryBean implements FactoryBean {

    private RecordReader recordReader;

    private List<RecordProcessor> processingPipeline;

    private List<JobListener> jobListeners;

    private List<RecordReaderListener> recordReaderListeners;

    private List<PipelineListener> pipelineListeners;

    private String name;

    private Long skip;

    private Long limit;

    private boolean strictMode;

    private boolean silentMode;

    private boolean jmxMonitoring;

    private boolean keepAlive;

    private Long timeoutValue;

    private TimeUnit timeoutUnit;

    @Override
    public Job getObject() throws Exception {
        JobBuilder jobBuilder = new JobBuilder();

        registerJobParameters(jobBuilder);

        registerMainComponents(jobBuilder);

        registerCustomListeners(jobBuilder);

        return jobBuilder.build();
    }

    private void registerJobParameters(JobBuilder jobBuilder) {
        if (name != null) {
            jobBuilder.named(name);
        }
        if (skip != null) {
            jobBuilder.skip(skip);
        }
        if (limit != null) {
            jobBuilder.limit(limit);
        }
        if (timeoutValue != null) {
            if (timeoutUnit != null) {
                jobBuilder.timeout(timeoutValue, timeoutUnit);
            } else {
                jobBuilder.timeout(timeoutValue);
            }
        }
        jobBuilder.silentMode(silentMode);
        jobBuilder.strictMode(strictMode);
        jobBuilder.jmxMode(jmxMonitoring);
    }

    private void registerMainComponents(JobBuilder jobBuilder) {
        if (recordReader != null) {
            jobBuilder.reader(recordReader, keepAlive);
        }
        if (processingPipeline != null) {
            for (RecordProcessor recordProcessor : processingPipeline) {
                jobBuilder.processor(recordProcessor);
            }
        }
    }

    private void registerCustomListeners(JobBuilder jobBuilder) {
        if (jobListeners != null) {
            for (JobListener jobListener : jobListeners) {
                jobBuilder.jobListener(jobListener);
            }
        }

        if (recordReaderListeners != null) {
            for (RecordReaderListener recordReaderListener : recordReaderListeners) {
                jobBuilder.readerListener(recordReaderListener);
            }
        }

        if (pipelineListeners != null) {
            for (PipelineListener pipelineListener : pipelineListeners) {
                jobBuilder.pipelineListener(pipelineListener);
            }
        }

    }

    @Override
    public Class<Job> getObjectType() {
        return Job.class;
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

    public void setJmxMonitoring(boolean jmxMonitoring) {
        this.jmxMonitoring = jmxMonitoring;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSilentMode(boolean silentMode) {
        this.silentMode = silentMode;
    }

    public void setSkip(long skip) {
        this.skip = skip;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }

    public void setTimeoutUnit(TimeUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
    }

    public void setTimeoutValue(long timeoutValue) {
        this.timeoutValue = timeoutValue;
    }
}
