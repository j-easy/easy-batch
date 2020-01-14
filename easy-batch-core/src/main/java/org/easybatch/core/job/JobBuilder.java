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
package org.easybatch.core.job;

import org.easybatch.core.filter.RecordFilter;
import org.easybatch.core.listener.*;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.validator.RecordValidator;
import org.easybatch.core.writer.RecordWriter;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Batch job builder.
 * This is the main entry point to configure batch jobs.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public final class JobBuilder {

    private BatchJob job;

    private JobParameters parameters;

    /**
     * Create a new {@link JobBuilder}.
     */
    public JobBuilder() {
        parameters = new JobParameters();
        job = new BatchJob(parameters);
    }

    /**
     * Create a new {@link JobBuilder}.
     *
     * @return a new job builder.
     */
    public static JobBuilder aNewJob() {
        return new JobBuilder();
    }

    /**
     * Set the job name.
     *
     * @param name the job name
     * @return the job builder
     */
    public JobBuilder named(final String name) {
        checkNotNull(name, "job name");
        job.setName(name);
        return this;
    }

    /**
     * Register a record reader.
     *
     * @param recordReader the record reader to register
     * @return the job builder
     */
    public JobBuilder reader(final RecordReader recordReader) {
        checkNotNull(recordReader, "record reader");
        job.setRecordReader(recordReader);
        return this;
    }

    /**
     * Register a record filter.
     *
     * @param recordFilter the record filter to register
     * @return the job builder
     */
    public JobBuilder filter(final RecordFilter recordFilter) {
        checkNotNull(recordFilter, "record filter");
        job.addRecordProcessor(recordFilter);
        return this;
    }

    /**
     * Register a record mapper.
     *
     * @param recordMapper the record mapper to register
     * @return the job builder
     */
    public JobBuilder mapper(final RecordMapper recordMapper) {
        checkNotNull(recordMapper, "record mapper");
        job.addRecordProcessor(recordMapper);
        return this;
    }

    /**
     * Register a record validator.
     *
     * @param recordValidator the record validator to register
     * @return the job builder
     */
    public JobBuilder validator(final RecordValidator recordValidator) {
        checkNotNull(recordValidator, "record validator");
        job.addRecordProcessor(recordValidator);
        return this;
    }

    /**
     * Register a record processor.
     *
     * @param recordProcessor the record processor to register
     * @return the job builder
     */
    public JobBuilder processor(final RecordProcessor recordProcessor) {
        checkNotNull(recordProcessor, "record processor");
        job.addRecordProcessor(recordProcessor);
        return this;
    }

    /**
     * Register a record marshaller.
     *
     * @param recordMarshaller the record marshaller to register
     * @return the job builder
     */
    public JobBuilder marshaller(final RecordMarshaller recordMarshaller) {
        checkNotNull(recordMarshaller, "record marshaller");
        job.addRecordProcessor(recordMarshaller);
        return this;
    }

    /**
     * Register a record writer.
     *
     * @param recordWriter the record writer to register
     * @return the job builder
     */
    public JobBuilder writer(final RecordWriter recordWriter) {
        checkNotNull(recordWriter, "record writer");
        job.setRecordWriter(recordWriter);
        return this;
    }

    /**
     * Set a threshold for errors. The job will be aborted if the threshold is exceeded.
     *
     * @param errorThreshold the error threshold
     * @return the job builder
     */
    public JobBuilder errorThreshold(final long errorThreshold) {
        if (errorThreshold < 0) {
            throw new IllegalArgumentException("error threshold must be >= 0");
        }
        parameters.setErrorThreshold(errorThreshold);
        return this;
    }

    /**
     * Activate JMX monitoring.
     *
     * @param jmx true to enable jmx monitoring
     * @return the job builder
     */
    public JobBuilder enableJmx(final boolean jmx) {
        parameters.setJmxMonitoring(jmx);
        return this;
    }

    /**
     * Set the batch size.
     *
     * @param batchSize the batch size
     * @return the job builder
     */
    public JobBuilder batchSize(final int batchSize) {
        if (batchSize < 1) {
            throw new IllegalArgumentException("Batch size must be >= 1");
        }
        parameters.setBatchSize(batchSize);
        return this;
    }

    /**
     * Register a job listener.
     * See {@link JobListener} for available callback methods.
     *
     * @param jobListener The job listener to add.
     * @return the job builder
     */
    public JobBuilder jobListener(final JobListener jobListener) {
        checkNotNull(jobListener, "job listener");
        job.addJobListener(jobListener);
        return this;
    }

    /**
     * Register a batch listener.
     * See {@link BatchListener} for available callback methods.
     *
     * @param batchListener The batch listener to add.
     * @return the job builder
     */
    public JobBuilder batchListener(final BatchListener batchListener) {
        checkNotNull(batchListener, "batch listener");
        job.addBatchListener(batchListener);
        return this;
    }

    /**
     * Register a record reader listener.
     * See {@link RecordReaderListener} for available callback methods.
     *
     * @param recordReaderListener The record reader listener to add.
     * @return the job builder
     */
    public JobBuilder readerListener(final RecordReaderListener recordReaderListener) {
        checkNotNull(recordReaderListener, "record reader listener");
        job.addRecordReaderListener(recordReaderListener);
        return this;
    }

    /**
     * Register a pipeline listener.
     * See {@link PipelineListener} for available callback methods.
     *
     * @param pipelineListener The pipeline listener to add.
     * @return the job builder
     */
    public JobBuilder pipelineListener(final PipelineListener pipelineListener) {
        checkNotNull(pipelineListener, "pipeline listener");
        job.addPipelineListener(pipelineListener);
        return this;
    }

    /**
     * Register a record writer listener.
     * See {@link RecordWriterListener} for available callback methods.
     *
     * @param recordWriterListener The record writer listener to register.
     * @return the job builder
     */
    public JobBuilder writerListener(final RecordWriterListener recordWriterListener) {
        checkNotNull(recordWriterListener, "record writer listener");
        job.addRecordWriterListener(recordWriterListener);
        return this;
    }

    /**
     * Build a batch job instance.
     *
     * @return a batch job instance
     */
    public Job build() {
        return job;
    }

}
