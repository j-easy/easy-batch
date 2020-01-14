/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.extensions.spring;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobParameters;
import org.easybatch.core.listener.*;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.writer.RecordWriter;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

/**
 * Spring Factory Bean that creates job instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobFactoryBean implements FactoryBean {

    private RecordReader recordReader;
    private RecordWriter recordWriter;
    private List<RecordProcessor> recordProcessors;

    private JobListener jobListener;
    private BatchListener batchListener;
    private RecordReaderListener recordReaderListener;
    private RecordWriterListener recordWriterListener;
    private PipelineListener pipelineListener;

    private String name = JobParameters.DEFAULT_JOB_NAME;
    private long errorThreshold = JobParameters.DEFAULT_ERROR_THRESHOLD;
    private int batchSize = JobParameters.DEFAULT_BATCH_SIZE;
    private boolean enableJmx;

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
        jobBuilder.errorThreshold(errorThreshold);
        jobBuilder.batchSize(batchSize);
        jobBuilder.enableJmx(enableJmx);
    }

    private void registerMainComponents(JobBuilder jobBuilder) {
        if (recordReader != null) {
            jobBuilder.reader(recordReader);
        }
        if (recordWriter != null) {
            jobBuilder.writer(recordWriter);
        }
        if (recordProcessors != null) {
            for (RecordProcessor recordProcessor : recordProcessors) {
                jobBuilder.processor(recordProcessor);
            }
        }
    }

    private void registerCustomListeners(JobBuilder jobBuilder) {
        if (jobListener != null) {
            jobBuilder.jobListener(jobListener);
        }

        if (batchListener != null) {
            jobBuilder.batchListener(batchListener);
        }

        if (recordReaderListener != null) {
            jobBuilder.readerListener(recordReaderListener);
        }

        if (recordWriterListener != null) {
            jobBuilder.writerListener(recordWriterListener);
        }

        if (pipelineListener != null) {
            jobBuilder.pipelineListener(pipelineListener);
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

    public void setRecordWriter(RecordWriter recordWriter) {
        this.recordWriter = recordWriter;
    }

    public void setRecordProcessors(List<RecordProcessor> recordProcessors) {
        this.recordProcessors = recordProcessors;
    }

    public void setJobListener(JobListener jobListener) {
        this.jobListener = jobListener;
    }

    public void setBatchListener(BatchListener batchListener) {
        this.batchListener = batchListener;
    }

    public void setRecordReaderListener(RecordReaderListener recordReaderListener) {
        this.recordReaderListener = recordReaderListener;
    }

    public void setRecordWriterListener(RecordWriterListener recordWriterListener) {
        this.recordWriterListener = recordWriterListener;
    }

    public void setPipelineListener(PipelineListener pipelineListener) {
        this.pipelineListener = pipelineListener;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setErrorThreshold(long errorThreshold) {
        this.errorThreshold = errorThreshold;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setEnableJmx(boolean enableJmx) {
        this.enableJmx = enableJmx;
    }
}
